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
package de.featjar.gui.handler.create.group;

import com.google.inject.Inject;

import de.featjar.base.data.Result;
import de.featjar.gui.handler.utils.CardinialityUtils;
import de.featjar.gui.handler.utils.HandlerUtils;
import de.featjar.gui.types.NodeType;
import featJAR.FeatJARFactory;
import featJAR.FeatJARPackage;
import featJAR.Feature;
import featJAR.FeatureModel;
import featJAR.GroupNode;
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

public class ACreateGroupNodeHandler extends EMFCreateOperationHandler<CreateNodeOperation> {

    @Inject
    protected EMFNotationModelState modelState;

    @Inject
    protected EMFIdGenerator idGenerator;

    protected ACreateGroupNodeHandler(final String elementTypeId) {
        super(elementTypeId);
    }

    @Override
    public Optional<Command> createCommand(final CreateNodeOperation operation) {
        return Optional.of(createGroupNode());
    }

    protected Optional<Feature> getRoot() {
        return modelState.getSemanticModel(FeatureModel.class).flatMap(model -> model.getRoots().stream()
                .findFirst());
    }

    @Override
    public String getLabel() {
        return "";
    }

    protected FeatureModel getFeatureModel() {
        return modelState.getSemanticModel(FeatureModel.class).get();
    }

    protected Command createGroupNode() {
        // If no element selected, default to root model

        Optional<Identifiable> selection = modelState.getProperty("currentSelection", Identifiable.class);

        // if another feature is selected, do nothing
        if (selection.isEmpty() || selection.get() instanceof GroupNode) {
            return IdentityCommand.INSTANCE;
        }

        Optional<Feature> parentElement =
                modelState.getProperty("currentSelection", Feature.class).or(() -> getRoot());

        // If no element selected, default to root model
        EObject eParent = parentElement.isPresent() ? parentElement.get() : getFeatureModel();

        Object reference = parentElement.isPresent()
                ? FeatJARPackage.Literals.FEATURE__GROUP_NODE_LIST
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

    private Result<GroupNode> createNode() {

        GroupNode gn = FeatJARFactory.eINSTANCE.createGroupNode();

        HandlerUtils.debugPrint(idGenerator, gn);

        idGenerator.getOrCreateId(gn); // sets ID if not already set
        gn.setName(getLabel());

        Result<NodeType> t = NodeType.fromValue(getHandledElementTypeIds().get(0));
        if(t.isEmpty()) {
        	return Result.empty(t.getProblems());
        }
        
        NodeType type = t.get();

        switch (type) {
            case OR_NODE:
                gn.setCardinality(CardinialityUtils.createOrCardinality());
                break;
            case XOR_NODE:
                gn.setCardinality(CardinialityUtils.createXorCardinality());
                break;
            case AND_NODE:
                gn.setCardinality(CardinialityUtils.createAndCardinality());
                break;
            case CARDINALITY_NODE: // TODO dummy var change !
                gn.setCardinality(CardinialityUtils.createCardinality(2, 5));
                break;
            default:
                break;
        }

        return Result.of(gn);
    }
}
