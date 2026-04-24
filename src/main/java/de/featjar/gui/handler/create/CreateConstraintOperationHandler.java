package de.featjar.gui.handler.create;

import java.util.Optional;

import org.eclipse.emf.common.command.Command;
import org.eclipse.glsp.server.emf.EMFCreateOperationHandler;
import org.eclipse.glsp.server.emf.EMFIdGenerator;
import org.eclipse.glsp.server.emf.notation.EMFNotationModelState;
import org.eclipse.glsp.server.operations.CreateNodeOperation;

import com.google.inject.Inject;

import de.featjar.gui.handler.utils.IConstraintUtils;
import de.featjar.gui.types.FeatureModelTypes;


public class CreateConstraintOperationHandler extends EMFCreateOperationHandler<CreateNodeOperation> implements IConstraintUtils{

   @Inject
   protected EMFNotationModelState modelState;

   @Inject
   protected EMFIdGenerator idGenerator;

   public CreateConstraintOperationHandler() {
      super(FeatureModelTypes.CONSTRAINT);
   }

   @Override
   public Optional<Command> createCommand(final CreateNodeOperation operation) {

      return Optional.of(createConstraintAndPlaceInsideBox(modelState, getLabel(), idGenerator));

   }

   @Override
   public String getLabel() { return "New Constraint"; }
}
