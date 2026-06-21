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

import de.featjar.gui.policy.FeatureModelDiagramModule;
import org.eclipse.elk.alg.layered.options.LayeredMetaDataProvider;
import org.eclipse.glsp.layout.ElkLayoutEngine;
import org.eclipse.glsp.server.di.ServerModule;
import org.eclipse.glsp.server.launch.DefaultCLIParser;
import org.eclipse.glsp.server.launch.GLSPServerLauncher;

public abstract class AFeatureModelServerLauncher {

    private static final int DEFAULT_PORT = 8081;

    protected void start(String[] args) throws Exception {
        DefaultCLIParser parser = new DefaultCLIParser(args, getProcessName());

        String hostname = parser.parseHostname();
        int parsedPort = parser.parsePort();
        int port = parsedPort == 0 ? DEFAULT_PORT : parsedPort;

        getLauncher().start(hostname, port, parser);
    }

    private GLSPServerLauncher getLauncher() {
        ElkLayoutEngine.initialize(new LayeredMetaDataProvider());
        return createLauncher(new ServerModule().configureDiagramModule(new FeatureModelDiagramModule()));
    }

    protected abstract String getProcessName();

    protected abstract GLSPServerLauncher createLauncher(ServerModule serverModule);
}
