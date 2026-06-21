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
package de.featjar.gui.handler.create.feature;

import com.google.inject.Inject;
import de.featjar.base.data.Result;
import de.featjar.gui.handler.utils.AttributeKeysUtils;
import de.featjar.gui.handler.utils.CardinialityUtils;
import de.featjar.gui.handler.utils.HandlerUtils;
import de.featjar.gui.types.CardinalityType;
import de.featjar.gui.types.FeatureImplementationTypes;
import featJAR.FeatJARFactory;
import featJAR.FeatJARPackage;
import featJAR.Feature;
import featJAR.FeatureModel;
import featJAR.Identifiable;
import java.util.Optional;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.glsp.server.emf.EMFCreateOperationHandler;
import org.eclipse.glsp.server.emf.EMFIdGenerator;
import org.eclipse.glsp.server.emf.notation.EMFNotationModelState;
import org.eclipse.glsp.server.operations.CreateNodeOperation;

public abstract class ACreateFeatureNodeHandler extends EMFCreateOperationHandler<CreateNodeOperation> {

    @Inject
    protected EMFNotationModelState modelState;

    @Inject
    protected EMFIdGenerator idGenerator;

    protected static int featureCounter = 1;

    @Override
    public Optional<Command> createCommand(final CreateNodeOperation operation) {
        return Optional.of(createFeatureAndNode());
    }

    protected ACreateFeatureNodeHandler(final String elementTypeId) {
        super(elementTypeId);
    }

    protected Optional<Feature> getRoot() {
        return modelState.getSemanticModel(FeatureModel.class).flatMap(model -> model.getRoots().stream()
                .findFirst());
    }

    @Override
    public String getLabel() {
        return "New Feature";
    }

    protected FeatureModel getFeatureModel() {
        return modelState.getSemanticModel(FeatureModel.class).get();
    }

    protected Command createFeatureAndNode() {
        // If no element selected, default to root model

        Optional<Identifiable> parentElement = modelState.getProperty("currentSelection", Identifiable.class);
        // if another node is selected, do nothing
        if (parentElement.isEmpty() || parentElement.get() instanceof Feature) {
            return IdentityCommand.INSTANCE;
        }

        //      Optional<GroupNode> parentElement = modelState
        //    	         .getProperty("currentSelection", GroupNode.class);

        // If no element selected, default to root model
        EObject eParent = parentElement.isPresent() ? parentElement.get() : getFeatureModel();

        Object reference = parentElement.isPresent()
                ? FeatJARPackage.Literals.GROUP_NODE__FEATURE_LIST
                : FeatJARPackage.Literals.FEATURE_MODEL__ROOTS;
        // Create the new feature instance
        // TODO Default value is Abstract
        FeatureImplementationTypes defaultImplType = FeatureImplementationTypes.ABSTRACT;
        Feature newFeature = createFeature(getLabel(), defaultImplType).orElseThrow();

        System.err.println("Created Feature: " + newFeature.getName());
        System.err.println(" Where to add: " + eParent.getClass() + " Containment Reference: " + reference.getClass()
                + " What to add (type): " + AttributeKeysUtils.getFeatureType(newFeature));

        return AddCommand.create(
                modelState.getEditingDomain(),
                eParent, // where to add
                reference, // the containment reference
                newFeature // what to add
                );
    }

    private Result<Feature> createFeature(final String label, FeatureImplementationTypes featureType) {
        Feature newFeature = FeatJARFactory.eINSTANCE.createFeature();

        AttributeKeysUtils.setFeatureImplementationType(newFeature, featureType);

        idGenerator.getOrCreateId(newFeature); // sets ID if not already set
        newFeature.setName(label + "-" + featureCounter++);

        HandlerUtils.debugPrint(idGenerator, newFeature);

        Result<CardinalityType> t =
                CardinalityType.fromValue(getHandledElementTypeIds().get(0));
        if (t.isEmpty()) {
            return Result.empty(t.getProblems());
        }

        CardinalityType type = t.get();

        switch (type) {
            case MANDATORY_FEATURE:
                newFeature.setCardinality(CardinialityUtils.createCardinality(1, 1));
                break;
            case OPTIONAL_FEATURE:
                newFeature.setCardinality(CardinialityUtils.createCardinality(0, 1));
                break;
            case MULTIPLE_FEATURE: // TODO dummy values change !
                newFeature.setCardinality(CardinialityUtils.createCardinality(13, 37));
                break;
            default:
                break;
        }
        return Result.of(newFeature);
    }
}
