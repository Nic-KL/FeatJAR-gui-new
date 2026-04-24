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
package de.featjar.gui.handler;

import java.util.List;
import java.util.NoSuchElementException;

import org.eclipse.glsp.server.actions.Action;
import org.eclipse.glsp.server.actions.ActionHandler;
import org.eclipse.glsp.server.actions.SelectAction;
import org.eclipse.glsp.server.emf.EMFIdGenerator;
import org.eclipse.glsp.server.emf.notation.EMFNotationModelState;

import com.google.inject.Inject;

import de.featjar.gui.handler.utils.HandlerUtils;
import de.featjar.gui.model.FeatureModelGModelFactory;
import featJAR.Identifiable;

public class SelectionActionHandler implements ActionHandler {

   @Inject
   protected EMFNotationModelState modelState;

   @Inject
   protected EMFIdGenerator idGenerator;

   @Override
   public boolean handles(final Action action) {
      return action instanceof SelectAction;
   }

   @Override
   public List<Class<? extends Action>> getHandledActionTypes() { return List.of(SelectAction.class); }

   @Override
   public List<Action> execute(final Action action) {

      SelectAction selectAction = (SelectAction) action;

      // Get the selected element IDs from the action
      List<String> selectedIds = selectAction.getSelectedElementsIDs();

      if (selectedIds.size() > 0) {
    	  // TODO DOES NOT WORK !
         Identifiable element = HandlerUtils
        		 .findIdentifiableById(FeatureModelGModelFactory.featureIdMap.get(selectedIds.get(0)))        		 
        		 .orElseThrow(NoSuchElementException::new);
         modelState.setProperty("currentSelection", element);
      }

      return List.of();
   }

}
