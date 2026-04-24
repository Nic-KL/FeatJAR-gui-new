package de.featjar.gui.handler.utils;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.glsp.server.emf.EMFIdGenerator;
import org.eclipse.glsp.server.emf.notation.EMFNotationModelState;

import featJAR.Constraint;
import featJAR.FeatJARFactory;
import featJAR.FeatJARPackage;
import featJAR.FeatureModel;

public interface IConstraintUtils {
	
		AtomicInteger constraintCounter = new AtomicInteger(1);

	   default Optional<Constraint> createConstraint(final String label, final EMFIdGenerator generator) {
	      Constraint newConstraint = FeatJARFactory.eINSTANCE.createConstraint();

	      HandlerUtils.debugPrint(generator, newConstraint);

	      generator.getOrCreateId(newConstraint); // sets ID if not already set
//	      newConstraint.setName(label + "_" + constraintCounter++);
	      newConstraint.setName(label + "_" + constraintCounter.getAndIncrement());
	      return Optional.of(newConstraint);
	   }

	   default Command createConstraintAndPlaceInsideBox(final EMFNotationModelState modelState, final String label,
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
