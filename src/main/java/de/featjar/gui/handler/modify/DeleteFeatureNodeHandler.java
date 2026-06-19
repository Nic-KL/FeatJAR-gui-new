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
package de.featjar.gui.handler.modify;

import com.google.inject.Inject;
import de.featjar.gui.handler.utils.HandlerUtils;
import de.featjar.gui.model.FeatureModelGModelFactory;
import featJAR.Feature;
import featJAR.Identifiable;

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

        Identifiable element = HandlerUtils.findIdentifiableById(FeatureModelGModelFactory.featureGroupIdMap.get(gModelId))
                .orElseThrow(() -> new NoSuchElementException(gModelId));

        EditingDomain editingDomain = modelState.getEditingDomain();
        return RemoveCommand.create(
                editingDomain, EObject.class.cast(element.eContainer()), element.eContainingFeature(), element);
    }
}
