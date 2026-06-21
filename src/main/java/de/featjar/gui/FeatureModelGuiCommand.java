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

    public static final Option<Boolean> SERVER_LAUNCHER_OPTION =
            Options.newFlag("server").setDescription("Use websocket launcher.");

    public static final Option<String> JETTY_LOG_LEVEL_OPTION =
            Options.newOption("jetty", Options.StringParser, "INFO").setDescription("Set the log level for the Jetty.");

    //    public static Options getDefaultOptions() {
    //        Options options = new Options();
    //        options.addOption("h", OPTION_HELP, false, "Display usage information about GLSPServerLauncher");
    //        options.addOption("n", OPTION_HOST_NAME, true, String.format("Set server host name. [default='%s']",
    //           DefaultOptions.HOST_NAME));
    //        options.addOption("p", OPTION_PORT, true,
    //           String.format("Set server port. [default='%s']", DefaultOptions.SERVER_PORT));
    //        options.addOption("c", OPTION_CONSOLE_LOG, true,
    //           String.format("Enable/Disable console logging. [default='%s']", DefaultOptions.CONSOLE_LOG_ENABLED));
    //        options.addOption("f", OPTION_FILE_LOG, true,
    //           String.format("Enable/Disable file logging. [default='%s']", DefaultOptions.FILE_LOG_ENABLED));
    //        options.addOption("d", OPTION_LOG_DIR, true,
    //           String.format("Set the directory for log files (File logging has to be enabled)",
    //              DefaultOptions.LOG_DIR));
    //        options.addOption("l", OPTION_LOG_LEVEL, true,
    //           String.format("Set the log level. [default='%s']", DefaultOptions.LOG_LEVEL));
    //        return options;

    private static final int MAX_LOG_FILE_SIZE = 1024 * 50; // 50 KiB
    private static final String LOG_FILE_NAME = "server_logs.log";
    private static final String CLIENT_EMF_FILE_NAME = "My Model.featuremodel";
    private static final Path LOG_FILE_PATH = Path.of("logs").resolve(LOG_FILE_NAME);
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
            server = launchServer(FeatureModelWebsocketLauncher.class.getCanonicalName(), true, optionList);
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

    private static Process launchServer(String className, boolean log, OptionList optionList) throws IOException {
        String classpath = System.getProperty("java.class.path");

        List<String> command = new ArrayList<>(5);
        command.add("java");
        command.add("-cp");
        command.add(classpath);
        command.add(className);
        command.add("-p");
        command.add(String.valueOf(optionList.get(PORT_OPTION)));

        ProcessBuilder pb = new ProcessBuilder(command);

        if (log) {
            createLogFile();

            pb.redirectOutput(ProcessBuilder.Redirect.appendTo(LOG_FILE_PATH.toFile()));
            pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        }

        return pb.start();
    }
}
