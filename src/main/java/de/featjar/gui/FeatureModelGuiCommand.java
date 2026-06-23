/*
 * Copyright (C) 2026 FeatJAR-Development-Team
 *
 * This file is part of FeatJAR-FeatJAR-gui-new.
 *
 * FeatJAR-gui-new is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3.0 of the License,
 * or (at your option) any later version.
 *
 * FeatJAR-gui-new is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FeatJAR-gui-new. If not, see <https://www.gnu.org/licenses/>.
 *
 * See <https://github.com/FeatureIDE> for further information.
 */
package de.featjar.gui;

import de.featjar.base.FeatJAR;
import de.featjar.base.cli.ACommand;
import de.featjar.base.cli.Option;
import de.featjar.base.cli.OptionList;
import de.featjar.base.cli.Options;
import de.featjar.base.data.Result;
import de.featjar.base.env.HostEnvironment;
import de.featjar.base.env.HostEnvironment.OperatingSystem;
import de.featjar.base.io.IO;
import de.featjar.base.io.format.IFormat;
import de.featjar.base.io.format.IFormatSupplier;
import de.featjar.feature.model.IFeatureModel;
import de.featjar.feature.model.io.FeatureModelFormats;
import de.featjar.feature.model.io.xml.XMLFeatureModelFormat;
import de.featjar.gui.io.EMFFeatureModelFormat;
import de.featjar.gui.launch.FeatureModelWebsocketLauncher;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Starts a GUI for modifying a feature model.
 *
 * @author Niclas Kleinert
 * @author Sebastian Krieter
 */
public class FeatureModelGuiCommand extends ACommand {

    public static final Option<IFormatSupplier<IFeatureModel>> INPUT_FORMAT =
            Options.newInputFormatOption(FeatureModelFormats.class);

    public static final Option<IFormat<IFeatureModel>> OUTPUT_FORMAT =
            Options.newOutputFormatOption(FeatureModelFormats.class, new XMLFeatureModelFormat().getName());

    public static final Option<Integer> PORT_OPTION = Options.newOption("port", Options.IntegerParser, "8081")
            .setDescription("Port of the local graphical language server.");

    private static final Path CLIENT_PATH =
            Path.of("..").resolve("FeatJAR-gui-client").resolve("app");
    private static final Path CLIENT_HTML_PATH = CLIENT_PATH.resolve("diagram.html");
    private static final Path CLIENT_ABSOLUTE_EMF_FILE_PATH = CLIENT_PATH.resolve("gui_model.featuremodel");

    private boolean serverStarted;
    private boolean serverStopped;

    @Override
    public Optional<String> getDescription() {
        return Optional.of("Opens a GUI for feature modeling.");
    }

    @Override
    public Optional<String> getShortName() {
        return Optional.of("gui");
    }

    @Override
    public int run(OptionList optionList) {
        Result<IFeatureModel> fromInput = readFromInput(optionList, optionList.get(INPUT_FORMAT));
        if (fromInput.isEmpty()) {
            FeatJAR.log().problems(fromInput);
            return FeatJAR.ERROR_COMPUTING_RESULT;
        }

        Process server;
        try {
            IO.save(fromInput.get(), CLIENT_ABSOLUTE_EMF_FILE_PATH, new EMFFeatureModelFormat());

            FeatJAR.log().info("Server is starting...");
            server = launchServer(optionList);

            waitForStartSignal();

            if (!server.isAlive()) {
                return FeatJAR.ERROR_COMPUTING_RESULT;
            }
            FeatJAR.log().info("Server is runnig and listens on port " + optionList.get(PORT_OPTION));

            openBrowser();
        } catch (IOException e) {
            FeatJAR.log().error(e);
            return FeatJAR.ERROR_COMPUTING_RESULT;
        }

        Thread listenForUserThread = new Thread(() -> {
            try {
                System.in.read();
            } catch (IOException e) {
            }
            processServerSignals(FeatureModelWebsocketLauncher.SIGNAL_STOP);
        });
        listenForUserThread.start();

        waitForStopSignal();

        try {
            return writeResult(
                    optionList,
                    IO.load(CLIENT_ABSOLUTE_EMF_FILE_PATH, new EMFFeatureModelFormat()),
                    optionList.get(OUTPUT_FORMAT));
        } finally {
            FeatJAR.log().info("Server is shutting down.....");
            server.destroy();
        }
    }

    private synchronized boolean processServerSignals(String signal) {
        switch (signal) {
            case FeatureModelWebsocketLauncher.SIGNAL_STOP:
                serverStarted = true;
                serverStopped = true;
                notifyAll();
                return true;
            case FeatureModelWebsocketLauncher.SIGNAL_START:
                serverStarted = true;
                notifyAll();
                return false;
            default:
                notifyAll();
                return false;
        }
    }

    private synchronized void waitForStartSignal() {
        while (!serverStarted) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                FeatJAR.log().error(e);
            }
        }
    }

    private synchronized void waitForStopSignal() {
        while (!serverStopped) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                FeatJAR.log().error(e);
            }
        }
    }

    private void openBrowser() throws IOException {
        String openCommand =
                switch (HostEnvironment.OPERATING_SYSTEM) {
                    case OperatingSystem.LINUX -> "xdg-open";
                    case OperatingSystem.MAC_OS -> "open";
                    case OperatingSystem.WINDOWS -> "start";
                    case OperatingSystem.UNKNOWN -> null;
                    default ->
                        throw new IllegalArgumentException("Unexpected value: " + HostEnvironment.OPERATING_SYSTEM);
                };
        if (openCommand == null) {
            FeatJAR.log().message("Open: " + CLIENT_HTML_PATH.toAbsolutePath().toUri());
        } else {
            List<String> command = new ArrayList<>(2);
            command.add(openCommand);
            command.add(CLIENT_HTML_PATH.toAbsolutePath().toUri().toString());
            new ProcessBuilder(command).start();
        }
    }

    private Process launchServer(OptionList optionList) throws IOException {
        List<String> command = new ArrayList<>(4);
        command.add("./gradlew");
        command.add("runServer");
        command.add("-q");
        command.add("--args=\"-p " + String.valueOf(optionList.get(PORT_OPTION)) + "\"");

        ProcessBuilder pb = new ProcessBuilder(command);

        pb.redirectError(ProcessBuilder.Redirect.DISCARD);

        final Process server = pb.start();

        Thread listeningThread = new Thread(() -> {
            try (BufferedReader reader =
                    new BufferedReader(new InputStreamReader(server.getInputStream(), StandardCharsets.UTF_8))) {
                for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                    if (processServerSignals(line)) {
                        return;
                    }
                }
            } catch (final IOException e) {
                FeatJAR.log().error(e);
            } finally {
                processServerSignals(FeatureModelWebsocketLauncher.SIGNAL_STOP);
            }
        });
        listeningThread.start();

        return server;
    }
}
