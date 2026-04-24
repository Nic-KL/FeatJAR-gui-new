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
package de.featjar.gui.handler.create;

import com.google.inject.Inject;

import de.featjar.gui.types.FeatureModelLables;
import java.util.Optional;
import org.eclipse.emf.common.command.Command;
import org.eclipse.glsp.server.emf.EMFCreateOperationHandler;
import org.eclipse.glsp.server.emf.EMFIdGenerator;
import org.eclipse.glsp.server.emf.notation.EMFNotationModelState;
import org.eclipse.glsp.server.operations.CreateNodeOperation;

public class CreateConstraintOperationHandler extends EMFCreateOperationHandler<CreateNodeOperation>
        {

    @Inject
    protected EMFNotationModelState modelState;

    @Inject
    protected EMFIdGenerator idGenerator;

    public CreateConstraintOperationHandler() {
        super(FeatureModelLables.CONSTRAINT);
    }

    @Override
    public Optional<Command> createCommand(final CreateNodeOperation operation) {

        return Optional.of(ConstraintCreator.createConstraintAndPlaceInsideBox(modelState, getLabel(), idGenerator));
    }

    @Override
    public String getLabel() {
        return "New Constraint";
    }
}
