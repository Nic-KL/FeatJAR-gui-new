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
package de.featjar.gui.policy;

import de.featjar.gui.types.CardinalityType;
import de.featjar.gui.types.EdgeType;
import de.featjar.gui.types.NodeType;
import java.util.List;
import org.eclipse.glsp.server.diagram.BaseDiagramConfiguration;
import org.eclipse.glsp.server.types.EdgeTypeHint;
import org.eclipse.glsp.server.types.ShapeTypeHint;

public class FeatureModelDiagramConfiguration extends BaseDiagramConfiguration {

    // @Override
    // public List<ShapeTypeHint> getShapeTypeHints() {
    // // tasks can be moved, deleted and resized
    // return List.of(new ShapeTypeHint(FeatureModelTypes.MANDATORY_FEATURE, false,
    // true, false, false),
    // new ShapeTypeHint(FeatureModelTypes.OPTIONAL_FEATURE, false, true, false,
    // false),
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
