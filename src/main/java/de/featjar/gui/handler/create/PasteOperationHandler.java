package de.featjar.gui.handler.create;

import java.util.Map;
import java.util.Optional;

import org.eclipse.emf.common.command.Command;
import org.eclipse.glsp.server.emf.EMFIdGenerator;
import org.eclipse.glsp.server.emf.EMFOperationHandler;
import org.eclipse.glsp.server.emf.notation.EMFNotationModelState;
import org.eclipse.glsp.server.operations.PasteOperation;

import com.google.inject.Inject;

import de.featjar.gui.handler.utils.IConstraintUtils;

public class PasteOperationHandler extends EMFOperationHandler<PasteOperation> implements IConstraintUtils{

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
      String constraintLabel = clipboardData.entrySet()
         .stream()
         .skip(clipboardData.size() - 1)
         .findFirst()
         .map(Map.Entry::getValue).orElse("");
      return Optional
         .of(createConstraintAndPlaceInsideBox(modelState, constraintLabel, idGenerator));
   }

   @Override
   public String getLabel() { return "New Constraint"; }
}
