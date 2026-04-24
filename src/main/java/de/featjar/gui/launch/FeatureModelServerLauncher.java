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
package de.featjar.gui.launch;

import org.apache.commons.cli.ParseException;
import org.eclipse.elk.alg.layered.options.LayeredMetaDataProvider;
import org.eclipse.glsp.layout.ElkLayoutEngine;
import org.eclipse.glsp.server.di.ServerModule;
import org.eclipse.glsp.server.launch.DefaultCLIParser;
import org.eclipse.glsp.server.launch.GLSPServerLauncher;
import org.eclipse.glsp.server.launch.SocketGLSPServerLauncher;
import org.eclipse.glsp.server.utils.LaunchUtil;
import org.eclipse.glsp.server.websocket.WebsocketServerLauncher;

import de.featjar.gui.policy.FeatureModelDiagramModule;

public final class FeatureModelServerLauncher {

    static final int DEFAULT_PORT = 8081;

    private FeatureModelServerLauncher() {}

    public static void main(final String[] args) {
        String processName = "FeatureModelGlspServer";
        try {
            FeatureModelCLIParser parser = new FeatureModelCLIParser(args, processName);
            ElkLayoutEngine.initialize(new LayeredMetaDataProvider());

            int parsedPort = parser.parsePort();
            int port = parsedPort == 0 ? DEFAULT_PORT : parsedPort;
            String host = parser.parseHostname();
            ServerModule featureModelServerModule =
                    new ServerModule().configureDiagramModule(new FeatureModelDiagramModule());

            GLSPServerLauncher launcher = !parser.isServer()
                    ? new WebsocketServerLauncher(
                            featureModelServerModule, "/featuremodel", parser.parseWebsocketLogLevel())
                    : new SocketGLSPServerLauncher(featureModelServerModule);

            launcher.start(host, port, parser);
        } catch (ParseException ex) {
            ex.printStackTrace();
            LaunchUtil.printHelp(processName, DefaultCLIParser.getDefaultOptions());
        }
    }
}
