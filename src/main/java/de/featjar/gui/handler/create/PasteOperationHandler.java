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

import java.util.Map;
import java.util.Optional;
import org.eclipse.emf.common.command.Command;
import org.eclipse.glsp.server.emf.EMFIdGenerator;
import org.eclipse.glsp.server.emf.EMFOperationHandler;
import org.eclipse.glsp.server.emf.notation.EMFNotationModelState;
import org.eclipse.glsp.server.operations.PasteOperation;

public class PasteOperationHandler extends EMFOperationHandler<PasteOperation> {

    @Inject
    protected EMFNotationModelState modelState;

    @Inject
    protected EMFIdGenerator idGenerator;

    public PasteOperationHandler() {
        super();
    }

    @Override
    public Optional<Command> createCommand(final PasteOperation operation) {

        Map<String, String> clipboardData = operation.getClipboardData();

        if (clipboardData.isEmpty()) {
            return Optional.empty();
        }
        String constraintLabel = clipboardData.entrySet().stream()
                .skip(clipboardData.size() - 1)
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse("");
        return Optional.of(ConstraintCreator.createConstraintAndPlaceInsideBox(modelState, constraintLabel, idGenerator));
    }

    @Override
    public String getLabel() {
        return "New Constraint";
    }
}
