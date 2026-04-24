/********************************************************************************
 * Copyright (c) 2020 EclipseSource and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License v. 2.0 are satisfied: GNU General Public License, version 2
 * with the GNU Classpath Exception which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 ********************************************************************************/
package de.featjar.gui.handler.modify;

import org.eclipse.glsp.server.operations.Operation;

public class FeatureNodeLabelEditOperation extends Operation {

   public static final String KIND = "editLabel";
   public String gLabelId;
   public String newText;

   public FeatureNodeLabelEditOperation() {
      super(KIND);
   }

   public FeatureNodeLabelEditOperation(final String operationKind, final String id, final String newText) {
      super(operationKind);
      this.gLabelId = id;
      this.newText = newText;
   }

}
