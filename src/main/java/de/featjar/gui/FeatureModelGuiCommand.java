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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private static final int MAX_LOG_FILE_SIZE = 1024 * 50; // 50 KiB
    private static final String LOG_FILE_NAME = "server_logs.log";
    private static final String CLIENT_EMF_FILE_NAME = "My Model.featuremodel";
    private static final Path LOG_FILE_PATH = Path.of("logs").resolve(LOG_FILE_NAME);
    private static final Path CLASSPATH_FILE_PATH = Path.of("build").resolve("classpath");
    private static final Path CLIENT_PATH =
            Path.of("..").resolve("FeatJAR-gui-client").resolve("app");
    private static final Path CLIENT_HTML_PATH = CLIENT_PATH.resolve("diagram.html");
    private static final Path CLIENT_ABSOLUTE_EMF_FILE_PATH = CLIENT_PATH.resolve(CLIENT_EMF_FILE_NAME);

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
            server = launchServer(optionList);
            FeatJAR.log().info("Server is runnig");
            openBrowser();
        } catch (IOException e) {
            FeatJAR.log().error(e);
            return FeatJAR.ERROR_COMPUTING_RESULT;
        }

        try {
            System.in.read();
            return writeResult(
                    optionList,
                    IO.load(CLIENT_ABSOLUTE_EMF_FILE_PATH, new EMFFeatureModelFormat()),
                    optionList.get(OUTPUT_FORMAT));
        } catch (IOException e) {
            FeatJAR.log().error(e);
            return FeatJAR.ERROR_WRITING_RESULT;
        } finally {
            FeatJAR.log().info("Server is shutting down.....");
            server.destroy();
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

    private static void createLogFile() throws IOException {
        if (Files.exists(LOG_FILE_PATH)) {
            if (Files.size(LOG_FILE_PATH) > MAX_LOG_FILE_SIZE) {
                Files.delete(LOG_FILE_PATH);
                Files.createFile(LOG_FILE_PATH);
            }
        } else {
            Files.createFile(LOG_FILE_PATH);
        }
    }

    private static Process launchServer(OptionList optionList) throws IOException {
        final String classpath = Files.lines(CLASSPATH_FILE_PATH).collect(Collectors.joining(getDelimiter()));

        List<String> command = new ArrayList<>(6);
        command.add("java");
        command.add("-cp");
        command.add(classpath);
        command.add(FeatureModelWebsocketLauncher.class.getCanonicalName());
        command.add("-p");
        command.add(String.valueOf(optionList.get(PORT_OPTION)));

        ProcessBuilder pb = new ProcessBuilder(command);

        createLogFile();

        pb.redirectOutput(ProcessBuilder.Redirect.appendTo(LOG_FILE_PATH.toFile()));
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);

        return pb.start();
    }

    private static CharSequence getDelimiter() {
        return switch (HostEnvironment.OPERATING_SYSTEM) {
            case OperatingSystem.LINUX, OperatingSystem.MAC_OS, OperatingSystem.UNKNOWN -> ":";
            case OperatingSystem.WINDOWS -> ";";
            default -> throw new IllegalArgumentException("Unexpected value: " + HostEnvironment.OPERATING_SYSTEM);
        };
    }
}
