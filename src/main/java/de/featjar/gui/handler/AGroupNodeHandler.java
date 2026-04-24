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
package de.featjar.gui.handler;

import java.util.Optional;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.glsp.server.emf.EMFCreateOperationHandler;
import org.eclipse.glsp.server.emf.EMFIdGenerator;
import org.eclipse.glsp.server.emf.notation.EMFNotationModelState;
import org.eclipse.glsp.server.operations.CreateNodeOperation;

import com.google.inject.Inject;

import de.featjar.gui.handler.utils.HandlerUtils;
import de.featjar.gui.handler.utils.ICardinialityUtils;
import de.featjar.gui.types.NodeType;
import featJAR.FeatJARFactory;
import featJAR.FeatJARPackage;
import featJAR.Feature;
import featJAR.FeatureModel;
import featJAR.GroupNode;
import featJAR.Identifiable;

public class AGroupNodeHandler extends EMFCreateOperationHandler<CreateNodeOperation> implements ICardinialityUtils{

   @Inject
   protected EMFNotationModelState modelState;

   @Inject
   protected EMFIdGenerator idGenerator;

   protected AGroupNodeHandler(final String elementTypeId) {
      super(elementTypeId);
   }

   @Override
   public Optional<Command> createCommand(final CreateNodeOperation operation) {
      return Optional.of(createOrXorNode());
   }

   protected Optional<Feature> getRoot() {
      return modelState.getSemanticModel(FeatureModel.class)
         .flatMap(model -> model.getRoots().stream().findFirst());
   }

   @Override
   public String getLabel() { return ""; } // not needed for helper OR/XOR node

   protected FeatureModel getFeatureModel() { return modelState.getSemanticModel(FeatureModel.class).get(); }

   protected Command createOrXorNode() {
      // If no element selected, default to root model
	   
	   Optional<Identifiable> selection = modelState
	            .getProperty("currentSelection", Identifiable.class);
	   
	   // if another feature is selected, do nothing
	   if(selection.isEmpty() || selection.get() instanceof GroupNode) {
		   return IdentityCommand.INSTANCE;
	   }
	   
      Optional<Feature> parentElement = modelState
         .getProperty("currentSelection", Feature.class)
         .or(() -> getRoot());

      // If no element selected, default to root model
      EObject eParent = parentElement.isPresent() ? parentElement.get() : getFeatureModel();

      Object reference = parentElement.isPresent() ? FeatJARPackage.Literals.FEATURE__GROUP_NODE_LIST
         : FeatJARPackage.Literals.FEATURE_MODEL__ROOTS;
      // Create the new feature instance
//      Feature newFeature = createNode().orElseThrow();
      
      GroupNode newGroupNode = createNode().orElseThrow();

      return AddCommand.create(
         modelState.getEditingDomain(),
         eParent, // where to add
         reference, // the containment reference
         newGroupNode // what to add
      );
   }

   private Optional<GroupNode> createNode() {
	  
	  GroupNode gn = FeatJARFactory.eINSTANCE.createGroupNode();

      HandlerUtils.debugPrint(idGenerator, gn);

      idGenerator.getOrCreateId(gn); // sets ID if not already set
      gn.setName(getLabel());
      
//      NodeType type = NodeType.valueOf(getHandledElementTypeIds().get(0));
      NodeType type = NodeType.fromGlspId(getHandledElementTypeIds().get(0));
      
      switch (type) {
         case OR_NODE:
        	gn.setCardinality(createOrCardinality());
            break;
         case XOR_NODE:
            gn.setCardinality(createXorCardinality());
            break;
		case AND_NODE:
			gn.setCardinality(createAndCardinality());
			break;
		case CARDINALITY_NODE: // TODO dummy var change !
			gn.setCardinality(createCardinality(2, 5));
			break;
		default:
			break;
      }
      
      return Optional.of(gn);
   }

}
