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
import de.featjar.gui.handler.utils.IAttributesUtils;
import de.featjar.gui.handler.utils.ICardinialityUtils;
import de.featjar.gui.types.CardinalityType;
import de.featjar.gui.types.FeatureType;
import featJAR.FeatJARFactory;
import featJAR.FeatJARPackage;
import featJAR.Feature;
import featJAR.FeatureModel;
import featJAR.Identifiable;

public abstract class AFeatureNodeHandler extends EMFCreateOperationHandler<CreateNodeOperation> implements IAttributesUtils, ICardinialityUtils{

   @Inject
   protected EMFNotationModelState modelState;

   @Inject
   protected EMFIdGenerator idGenerator;

   protected static int featureCounter = 1;

   @Override
   public Optional<Command> createCommand(final CreateNodeOperation operation) {
      return Optional.of(createFeatureAndNode());
   }

   protected AFeatureNodeHandler(final String elementTypeId) {
      super(elementTypeId);
   }

   protected Optional<Feature> getRoot() {
      return modelState.getSemanticModel(FeatureModel.class)
         .flatMap(model -> model.getRoots().stream().findFirst());
   }

   @Override
   public String getLabel() { return "New Feature"; }

   protected FeatureModel getFeatureModel() { return modelState.getSemanticModel(FeatureModel.class).get(); }

   protected Command createFeatureAndNode() {
      // If no element selected, default to root model
	  
	   Optional<Identifiable> parentElement = modelState
	            .getProperty("currentSelection", Identifiable.class);
	   // if another node is selected, do nothing
	   if(parentElement.isEmpty() || parentElement.get() instanceof Feature) {
		   return IdentityCommand.INSTANCE;
	   }
	   
//      Optional<GroupNode> parentElement = modelState
//    	         .getProperty("currentSelection", GroupNode.class);

      // If no element selected, default to root model
      EObject eParent = parentElement.isPresent() ? parentElement.get() : getFeatureModel();

      Object reference = parentElement.isPresent() ? FeatJARPackage.Literals.GROUP_NODE__FEATURE_LIST
         : FeatJARPackage.Literals.FEATURE_MODEL__ROOTS;
      // Create the new feature instance
      FeatureType TODO = FeatureType.ABSTRACT;
      Feature newFeature = createFeature(getLabel(), TODO).orElseThrow();
      
      System.err.println("Created Feature: " + newFeature.getName());
      System.err.println(" Where to add: " + eParent.getClass() 
      		+ " Containment Reference: " 
    		+ reference.getClass() + " What to add (type): "+ getFeatureType(newFeature));
      
      
      return AddCommand.create(
         modelState.getEditingDomain(),
         eParent, // where to add
         reference, // the containment reference
         newFeature // what to add
      );
   }

   private Optional<Feature> createFeature(final String label, FeatureType featureType) {
      Feature newFeature = FeatJARFactory.eINSTANCE.createFeature();
      
      setFeatureType(newFeature, featureType);
      
      idGenerator.getOrCreateId(newFeature); // sets ID if not already set
      newFeature.setName(label + "-" + featureCounter++);   
      
      HandlerUtils.debugPrint(idGenerator, newFeature); 
      
//      FeatureCardinalityType type = FeatureCardinalityType.valueOf(getHandledElementTypeIds().get(0));
      CardinalityType type = CardinalityType.fromGlspId(getHandledElementTypeIds().get(0));
      
      switch (type) {
         case MANDATORY_FEATURE:
        	 newFeature.setCardinality(createCardinality(1, 1));
            break;
         case OPTIONAL_FEATURE:
        	 newFeature.setCardinality(createCardinality(0, 1));
            break;
		case MULTIPLE_FEATURE: // TODO dummy vars change !
			newFeature.setCardinality(createCardinality(2, 5));
			break;
		default:
			break;
      }
      return Optional.of(newFeature);
   }
}
