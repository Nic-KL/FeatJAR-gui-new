/********************************************************************************
 * Copyright (c) 2020 EclipseSource and others.
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
package de.featjar.gui.handler.modify;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.glsp.graph.GLabel;
import org.eclipse.glsp.graph.GNode;
import org.eclipse.glsp.server.emf.EMFOperationHandler;

import de.featjar.gui.handler.utils.HandlerUtils;
import de.featjar.gui.model.FeatureModelGModelFactory;
import de.featjar.gui.types.FeatureModelTypes;
import featJAR.Constraint;
import featJAR.Feature;

public class FeatureNodeLabelEditOperationHandler extends EMFOperationHandler<FeatureNodeLabelEditOperation> {

   @Override
   public Optional<Command> createCommand(final FeatureNodeLabelEditOperation operation) {

      Set<GNode> featureNodes = modelState.getIndex().getAllByClass(GNode.class);

      Stream<GLabel> allLabels = featureNodes.stream()
         .flatMap(n -> n.getChildren().stream())
         .filter(c -> FeatureModelTypes.EDITABLE_LABEL.equals(c.getType()))
         .filter(GLabel.class::isInstance)
         .map(GLabel.class::cast);

       List<GLabel> allLabelsList = allLabels.toList();
//      List<GLabel> allLabelsList = allLabels.collect(Collectors.toList()); // Java 11 

      GLabel foundLabel = HandlerUtils.findLabelById(operation.gLabelId, allLabelsList)
         .orElseThrow(NoSuchElementException::new);

      if (foundLabel.getParent().getId() == "cross-tree-contraints") {
         Constraint element = HandlerUtils.findConstraintById(
            FeatureModelGModelFactory.constraintIdMap.get(foundLabel.getId())).orElseThrow(NoSuchElementException::new);
         element.setName(operation.newText);
      } else {
         Feature element = HandlerUtils.findFeatureById(
            FeatureModelGModelFactory.featureIdMap.get(foundLabel
               .getParent().getId()))
            .orElseThrow(NoSuchElementException::new);
         element.setName(operation.newText);

      }

      return Optional.of(new IdentityCommand());

   }

}
