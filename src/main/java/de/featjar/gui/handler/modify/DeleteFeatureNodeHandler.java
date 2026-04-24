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
import java.util.NoSuchElementException;
import java.util.Optional;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.glsp.server.emf.EMFOperationHandler;
import org.eclipse.glsp.server.emf.notation.EMFNotationModelState;
import org.eclipse.glsp.server.operations.DeleteOperation;

import com.google.inject.Inject;

import de.featjar.gui.handler.utils.HandlerUtils;
import de.featjar.gui.model.FeatureModelGModelFactory;
import featJAR.Feature;

// AbstractEMFOperationHandler<DeleteOperation>
public class DeleteFeatureNodeHandler extends EMFOperationHandler<DeleteOperation> {

   @Inject
   protected EMFNotationModelState modelState;

   @Override
   public Optional<Command> createCommand(final DeleteOperation operation) {

      List<String> gModelIds = operation.getElementIds();
      CompoundCommand command = new CompoundCommand();

      for (String id : gModelIds) {
         command.append(delete(id));
      }
      return Optional.of(command);
   }

   protected Command delete(final String gModelId) {

      Feature element = HandlerUtils.findFeatureById(FeatureModelGModelFactory.featureIdMap.get(gModelId))
         .orElseThrow(() -> new NoSuchElementException(gModelId));

      EditingDomain editingDomain = modelState.getEditingDomain();
      return RemoveCommand.create(editingDomain, EObject.class.cast(element.eContainer()), element.eContainingFeature(),
         element);

   }

}
