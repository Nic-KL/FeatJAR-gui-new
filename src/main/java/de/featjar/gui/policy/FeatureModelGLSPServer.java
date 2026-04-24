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
package de.featjar.gui.policy;

import static org.eclipse.glsp.server.types.GLSPServerException.getOrThrow;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.glsp.server.protocol.DefaultGLSPServer;
import org.eclipse.glsp.server.protocol.InitializeResult;
import org.eclipse.glsp.server.utils.MapUtil;

public class FeatureModelGLSPServer extends DefaultGLSPServer {
    protected static final Logger LOGGER = LogManager.getLogger(FeatureModelGLSPServer.class);
    private static final String MESSAGE_KEY = "message";
    private static final String TIMESTAMP_KEY = "timestamp";

    @Override
    public CompletableFuture<InitializeResult> handleIntializeArgs(
            final InitializeResult result, final Map<String, String> args) {
        CompletableFuture<InitializeResult> completableResult = CompletableFuture.completedFuture(result);
        if (args.isEmpty()) {
            return completableResult;
        }

        String timestamp = getOrThrow(
                MapUtil.getValue(args, TIMESTAMP_KEY), "No value present for the given key: " + TIMESTAMP_KEY);
        String message =
                getOrThrow(MapUtil.getValue(args, MESSAGE_KEY), "No value present for the given key: " + MESSAGE_KEY);
        LOGGER.debug(timestamp + ": " + message);

        return completableResult;
    }
}
