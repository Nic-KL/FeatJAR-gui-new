/********************************************************************************
 * Copyright (c) 2026 EclipseSource and others.
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
package de.featjar.gui.handler.utils;

import java.util.Optional;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.glsp.server.emf.EMFIdGenerator;
import org.eclipse.glsp.server.emf.notation.EMFNotationModelState;

import featJAR.Constraint;
import featJAR.FeatJARFactory;
import featJAR.FeatJARPackage;
import featJAR.FeatureModel;

/**
 * <p> Use {@link IConstraintUtils} instead.
 * @deprecated
 */
public final class ConstraintCreatorUtils {

   private static int constraintCounter = 1;

   public static Optional<Constraint> createConstraint(final String label, final EMFIdGenerator generator) {
      Constraint newConstraint = FeatJARFactory.eINSTANCE.createConstraint();

      HandlerUtils.debugPrint(generator, newConstraint);

      generator.getOrCreateId(newConstraint); // sets ID if not already set
      newConstraint.setName(label + "_" + constraintCounter++);
      return Optional.of(newConstraint);
   }

   public static Command createConstraintAndPlaceInsideBox(final EMFNotationModelState modelState, final String label,
      final EMFIdGenerator generator) {
      // If no element selected, default to root model
      EObject parentElement = modelState.getSemanticModel(FeatureModel.class).get();

      // Create the new feature instance
      Constraint newConstraint = createConstraint(label, generator).orElseThrow();

      return AddCommand.create(
         modelState.getEditingDomain(),
         parentElement, // where to add
         FeatJARPackage.Literals.FEATURE_MODEL__CONSTRAINTS, // the containment reference
         newConstraint // what to add
      );
   }
}
