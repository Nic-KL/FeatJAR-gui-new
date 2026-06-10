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

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.glsp.server.launch.DefaultCLIParser;

public class FeatureModelCLIParser extends DefaultCLIParser {
    public static final String OPTION_WEBSOCKET = "websocket";
    public static final String OPTION_SERVER = "server";
    public static final String OPTION_JETTY_LOG_LEVEL = "jettyLogLevel";

    public FeatureModelCLIParser(final String[] args, final String processName) throws ParseException {
        super(args, FeatureModelCLIParser.getDefaultOptions(), processName);
    }

    public boolean isWebsocket() {
        return hasOption(OPTION_WEBSOCKET);
    }

    public boolean isServer() {
        return hasOption(OPTION_SERVER);
    }

    public static Options getDefaultOptions() {
        Options options = DefaultCLIParser.getDefaultOptions();
        options.addOption(
                "w", OPTION_WEBSOCKET, false, "Use websocket launcher which is the curreent default launcher.");
        options.addOption("s", OPTION_SERVER, false, "Use server launcher instead of default launcher.");
        options.addOption(
                "j",
                OPTION_JETTY_LOG_LEVEL,
                true,
                "Set the log level for the Jetty websocket server. [default='INFO']");
        return options;
    }
}
