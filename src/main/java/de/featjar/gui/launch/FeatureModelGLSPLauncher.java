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

import org.eclipse.glsp.server.di.ServerModule;
import org.eclipse.glsp.server.launch.GLSPServerLauncher;
import org.eclipse.glsp.server.launch.SocketGLSPServerLauncher;

public final class FeatureModelGLSPLauncher extends AFeatureModelServerLauncher {

    private FeatureModelGLSPLauncher() {}

    public static void main(String[] args) throws Exception {
        new FeatureModelGLSPLauncher().start(args);
    }

    @Override
    protected String getProcessName() {
        return "FeatureModelGlspServer";
    }

    @Override
    protected GLSPServerLauncher createLauncher(ServerModule serverModule) {
        return new SocketGLSPServerLauncher(serverModule);
    }
}
