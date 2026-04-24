/********************************************************************************
 * Copyright (c) 2022 EclipseSource and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License v. 2.0 are satisfied:
 * -- GNU General Public License, version 2 with the GNU Classpath Exception
 * which is available at https://www.gnu.org/software/classpath/license.html
 * -- MIT License which is available at https://opensource.org/license/mit.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0 OR MIT
 ********************************************************************************/
package de.featjar.gui.config;

import java.util.List;

import org.eclipse.glsp.server.diagram.BaseDiagramConfiguration;
import org.eclipse.glsp.server.types.EdgeTypeHint;
import org.eclipse.glsp.server.types.ShapeTypeHint;

import de.featjar.gui.types.CardinalityType;
import de.featjar.gui.types.EdgeType;
import de.featjar.gui.types.NodeType;

public class FeatureModelDiagramConfiguration extends BaseDiagramConfiguration {

   // @Override
   // public List<ShapeTypeHint> getShapeTypeHints() {
   // // tasks can be moved, deleted and resized
   // return List.of(new ShapeTypeHint(FeatureModelTypes.MANDATORY_FEATURE, false, true, false, false),
   // new ShapeTypeHint(FeatureModelTypes.OPTIONAL_FEATURE, false, true, false, false),
   // new ShapeTypeHint(FeatureModelTypes.ROOT, false, true, false, false),
   // new ShapeTypeHint(FeatureModelTypes.NODE, false, true, false, false));
   // }

   @Override
   public List<ShapeTypeHint> getShapeTypeHints() {
      // tasks can be moved, deleted and resized
      return List.of(
         new ShapeTypeHint(CardinalityType.MANDATORY_FEATURE.value(), false, true, false, true),
         new ShapeTypeHint(CardinalityType.OPTIONAL_FEATURE.value(), false, true, false, true),
         new ShapeTypeHint(FeatureModelTypes.ROOT_FEATURE, false, false, false, false),
         new ShapeTypeHint(NodeType.AND_NODE.value(), false, true, false, true));
   }

   @Override
   public List<EdgeTypeHint> getEdgeTypeHints() {
      return List.of(
         new EdgeTypeHint(EdgeType.BASIC_EDGE.value(), false, true, false, false, null, null),
         new EdgeTypeHint(EdgeType.MANDATORY_EDGE.value(), false, true, false, false, null, null),
         new EdgeTypeHint(EdgeType.OPTIONAL_EDGE.value(), false, true, false, false, null, null));
   }
}
