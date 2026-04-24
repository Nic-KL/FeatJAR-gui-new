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

import java.util.List;

import org.eclipse.glsp.server.actions.AbstractActionHandler;
import org.eclipse.glsp.server.actions.Action;
import org.eclipse.glsp.server.actions.ActionDispatcher;
import org.eclipse.glsp.server.emf.notation.EMFNotationModelState;
import org.eclipse.glsp.server.features.directediting.ApplyLabelEditOperation;

import com.google.inject.Inject;

public class FeatureNodeLabelEditHandler extends AbstractActionHandler<ApplyLabelEditOperation> {

   @Inject
   protected EMFNotationModelState modelState;

   @Inject
   protected ActionDispatcher actionDispatcher;

   @Override
   protected List<Action> executeAction(final ApplyLabelEditOperation actualAction) {

      actionDispatcher.dispatch(
         new FeatureNodeLabelEditOperation("labelEdit", actualAction.getLabelId(), actualAction.getText()));

      return List.of();
   }

}
