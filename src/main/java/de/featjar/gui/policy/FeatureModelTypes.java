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

import org.eclipse.glsp.graph.DefaultTypes;

/**
 * <p>
 * Use the classes of {@link de.featjar.gui.types} instead.
 *
 * @deprecated
 */
public final class FeatureModelTypes {
    private FeatureModelTypes() {}

    public static final String ROOT_FEATURE = "feature:root";
    public static final String OPTIONAL_FEATURE = "feature:optional";
    public static final String MANDATORY_FEATURE = "feature:manatory";
    public static final String MULTIPLE_FEATURE = "feature:multiple";

    //   public static final String ORXOR_FEATURE = "feature:orxor";

    public static final String MANDATORY_EDGE = "edge:mandatory";
    public static final String OPTIONAL_EDGE = "edge:optional";
    public static final String BASIC_EDGE = DefaultTypes.EDGE;

    public static final String OR_NODE = "node:or";
    public static final String XOR_NODE = "node:xor";
    public static final String CARDINALITY_NODE = "node:cardinality";
    public static final String AND_NODE = "node:and";

    public static final String CONSTRAINT = DefaultTypes.EDGE;
    public static final String EDITABLE_LABEL = "label:heading";
}
