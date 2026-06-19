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
package de.featjar.gui.handler.modify;

import de.featjar.gui.handler.utils.HandlerUtils;
import de.featjar.gui.model.FeatureModelGModelFactory;
import de.featjar.gui.types.FeatureModelLables;
import featJAR.Constraint;
import featJAR.Feature;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.glsp.graph.GLabel;
import org.eclipse.glsp.graph.GNode;
import org.eclipse.glsp.server.emf.EMFOperationHandler;

public class FeatureNodeLabelEditOperationHandler extends EMFOperationHandler<FeatureNodeLabelEditOperation> {

    @Override
    public Optional<Command> createCommand(final FeatureNodeLabelEditOperation operation) {

        Set<GNode> featureNodes = modelState.getIndex().getAllByClass(GNode.class);

        Stream<GLabel> allLabels = featureNodes.stream()
                .flatMap(n -> n.getChildren().stream())
                .filter(c -> FeatureModelLables.EDITABLE_LABEL.equals(c.getType()))
                .filter(GLabel.class::isInstance)
                .map(GLabel.class::cast);

        //        List<GLabel> allLabelsList = allLabels.toList();
        List<GLabel> allLabelsList = allLabels.collect(Collectors.toList()); // Java 11

        GLabel foundLabel =
                HandlerUtils.findLabelById(operation.gLabelId, allLabelsList).orElseThrow(NoSuchElementException::new);

        if (foundLabel.getParent().getId() == "cross-tree-contraints") {
            Constraint element = HandlerUtils.findConstraintById(
                            FeatureModelGModelFactory.constraintIdMap.get(foundLabel.getId()))
                    .orElseThrow(NoSuchElementException::new);
            element.setName(operation.newText);
        } else {
            Feature element = HandlerUtils.findFeatureById(FeatureModelGModelFactory.featureGroupIdMap.get(
                            foundLabel.getParent().getId()))
                    .orElseThrow(NoSuchElementException::new);
            element.setName(operation.newText);
        }

        return Optional.of(new IdentityCommand());
    }
}
