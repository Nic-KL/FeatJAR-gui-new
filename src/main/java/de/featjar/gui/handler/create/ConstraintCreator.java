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

import featJAR.Constraint;
import featJAR.FeatJARFactory;
import featJAR.FeatJARPackage;
import featJAR.FeatureModel;
import java.util.Optional;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.glsp.server.emf.EMFIdGenerator;
import org.eclipse.glsp.server.emf.notation.EMFNotationModelState;

import de.featjar.gui.handler.utils.HandlerUtils;

public final class ConstraintCreator {

    static int constraintCounter = 1;

    public static Optional<Constraint> createConstraint(final String label, final EMFIdGenerator generator) {
        Constraint newConstraint = FeatJARFactory.eINSTANCE.createConstraint();

        HandlerUtils.debugPrint(generator, newConstraint);

        generator.getOrCreateId(newConstraint); // sets ID if not already set
        //	      newConstraint.setName(label + "_" + constraintCounter++);
        newConstraint.setName(label + "_" + constraintCounter++);
        return Optional.of(newConstraint);
    }

    public static Command createConstraintAndPlaceInsideBox(
            final EMFNotationModelState modelState, final String label, final EMFIdGenerator generator) {
        // If no element selected, default to root model
        EObject parentElement = modelState.getSemanticModel(FeatureModel.class).get();

        // Create the new feature instance
        Constraint newConstraint = ConstraintCreator.createConstraint(label, generator).orElseThrow();

        return AddCommand.create(
                modelState.getEditingDomain(),
                parentElement, // where to add
                FeatJARPackage.Literals.FEATURE_MODEL__CONSTRAINTS, // the containment reference
                newConstraint // what to add
                );
    }
}
