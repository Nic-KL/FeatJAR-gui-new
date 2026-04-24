/********************************************************************************
 * Copyright (c) 2022-2023 EclipseSource and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License v. 2.0 are satisfied:
 * -- GNU General Public License, version 2 with the GNU Classpath Exception
 * which is available at https://www.gnu.org/software/classpath/license.html
 * -- MIT License which is available at https://opensource.org/license/mit.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0 OR MIT
 ********************************************************************************/
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

import de.featjar.gui.config.FeatureModelDiagramModule;

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
         ServerModule featureModelServerModule = new ServerModule()
            .configureDiagramModule(new FeatureModelDiagramModule());

         GLSPServerLauncher launcher = !parser.isServer()
            ? new WebsocketServerLauncher(featureModelServerModule, "/featuremodel", parser.parseWebsocketLogLevel())
            : new SocketGLSPServerLauncher(featureModelServerModule);
         
         launcher.start(host, port, parser);
      } catch (ParseException ex) {
         ex.printStackTrace();
         LaunchUtil.printHelp(processName, DefaultCLIParser.getDefaultOptions());
      }
   }
}
