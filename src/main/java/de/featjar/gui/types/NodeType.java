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
package de.featjar.gui.types;

import featJAR.Cardinality;

public enum NodeType {
    OR_NODE("node-or"),
    XOR_NODE("node-xor"),
    AND_NODE("node-and"),
    CARDINALITY_NODE("node-cardinality");

    private final String value;

    private NodeType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static NodeType of(Cardinality c) {
        int lower = c.getLowerBound();
        int upper = c.getUpperBound();
        if (lower == 1 && upper == -1) return OR_NODE;
        if (lower == 1 && upper == 1) return XOR_NODE;
        if (lower == 0 && upper == -1) return AND_NODE;
        return CARDINALITY_NODE;
    }

    public static NodeType fromGlspId(String glspId) {
        for (NodeType t : values()) {
            if (t.value().equals(glspId)) return t;
        }
        throw new IllegalArgumentException("Unknown NodeType: " + glspId);
    }
}
