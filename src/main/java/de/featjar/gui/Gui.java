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
import de.featjar.base.data.Problem;
import de.featjar.base.data.Problem.Severity;
import de.featjar.base.data.Result;
import de.featjar.base.io.IO;
import de.featjar.base.io.format.IFormat;
import de.featjar.feature.model.IFeatureModel;
import de.featjar.feature.model.io.FeatureModelFormats;
import de.featjar.gui.io.EMFFeatureModelFormat;
import de.featjar.gui.launch.FeatureModelServerLauncher;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Gui {

    private static final int MAX_LOG_FILE_SIZE = 1024 * 50; // 50 KiB
    private static final String LOG_FILE_NAME = "server_logs.log";
    private static final Path LOG_FILE_PATH = Path.of("logs" + File.separator + LOG_FILE_NAME);
    private static final String CLIENT_PATH = ".." + File.separator + "FeatJAR-gui-client" + File.separator + "app";
    private static final Path CLIENT_HTML_PATH = Path.of(CLIENT_PATH, "diagram.html");
    private static final String CLIENT_EMF_FILE_NAME = "My Model.featuremodel";
    private static final Path CLIENT_ABSOLUTE_EMF_FILE_PATH = Path.of(CLIENT_PATH, CLIENT_EMF_FILE_NAME);

    public static void main(String[] args) {
        // TODO remove when completely connected to FeatJAR and FeatJAR Shell !
        FeatJAR.initialize();

        try (Scanner scanner = new Scanner(System.in)) {
            try {
                Result<String> path = parseArgs(args, "-path");
                Result<String> portNumber = parseArgs(args, "-p");
                String originalFileName = null;
                IFeatureModel fm = null;
                IFormat<IFeatureModel> format = null;

                if (path.isPresent()) {
                    String p = path.get();
                    originalFileName = Path.of(p).getFileName().toString();

                    fm = IO.load(Path.of(p), FeatureModelFormats.getInstance()).orElseThrow();
                    format = FeatureModelFormats.getInstance().getFormatList(Path.of(p)).stream()
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("No format for: " + p));

                    try {
                        IO.save(fm, CLIENT_ABSOLUTE_EMF_FILE_PATH, new EMFFeatureModelFormat());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    path.ifPresent(null).orElseThrow();
                }
                Process backroundServer = launchBackroundServer(portNumber);

                FeatJAR.log().message("Server is runnig");
                FeatJAR.log().message("Open the client file via CTRL + left click");
                FeatJAR.log().message(CLIENT_HTML_PATH.toAbsolutePath().toUri());

                while (true) {
                    if (scanner.hasNextLine()) {
                        String input = scanner.nextLine().trim().toLowerCase();
                        if (Objects.equals(input, "exit")) {
                            FeatJAR.log().message("Server is shutting down.....");
                            backroundServer.destroy();
                            String cwd = Paths.get("").toAbsolutePath().toString();

                            FeatJAR.log().message("Saving to %s", cwd);
                            fm = IO.load(CLIENT_ABSOLUTE_EMF_FILE_PATH, new EMFFeatureModelFormat())
                                    .orElseThrow();
                            IO.save(fm, Path.of(cwd, originalFileName), format);

                            System.exit(0);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Result<String> parseArgs(String[] cmdArgs, String flag) {
        for (int i = 0; i < cmdArgs.length; i++) {
            if (flag.equals(cmdArgs[i])) {
                if (i + 1 >= cmdArgs.length) {
                    return Result.empty(new IllegalArgumentException("Missing value after " + flag));
                    // addProblem(Severity.ERROR, "Missing value after argument ", flag)
                    //                     throw new IllegalArgumentException("Missing value after " + flag);
                }
                return Result.of(cmdArgs[i + 1]);
            }
        }
        return Result.empty(addProblem(Severity.ERROR, "Missing value after argument ", flag));
    }

    private static Result<File> createLogFile() throws IOException {

        if (Files.exists(LOG_FILE_PATH)) {
            if (Files.size(LOG_FILE_PATH) > MAX_LOG_FILE_SIZE) {
                Files.delete(LOG_FILE_PATH);
                return Result.of(Files.createFile(LOG_FILE_PATH).toFile());
            }
        } else {
            return Result.of(Files.createFile(LOG_FILE_PATH).toFile());
        }
        return Result.of(LOG_FILE_PATH.toFile());
    }

    private static Process launchBackroundServer(Result<String> portNumber) throws IOException {
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
        String classpath = System.getProperty("java.class.path");
        String className = "de.featjar.gui.launch.FeatureModelServerLauncher";
        List<String> command = new ArrayList<>();

        new Thread(() -> {
                    FeatureModelServerLauncher.main(null);
                })
                .start();

        command.add(javaBin);
        command.add("-cp");
        command.add(classpath);
        command.add(className);
        portNumber.ifPresent(p -> command.add(String.format("-p ", p)));

        ProcessBuilder pb = new ProcessBuilder(command);

        File log = createLogFile()
                .orElse(
                        Files.exists(Path.of("fallback_logger.log"))
                                ? Path.of("fallback_logger.log").toFile()
                                : Files.createFile(Path.of("fallback_logger.log"))
                                        .toFile());

        pb.redirectOutput(ProcessBuilder.Redirect.appendTo(log));
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        //       pb.redirectError(ProcessBuilder.Redirect.appendTo(log));

        return pb.start();
    }

    private static Problem addProblem(Severity severity, String message, Object... arguments) {
        return new Problem(String.format(message, arguments), severity);
    }
}
