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
package de.featjar.gui.handler.utils;

import featJAR.Cardinality;
import featJAR.FeatJARFactory;

public final class CardinialityUtils {
    public static Cardinality createCardinality(int lowerBound, int upperBound) {
        Cardinality cardinality = FeatJARFactory.eINSTANCE.createCardinality();
        cardinality.setLowerBound(lowerBound);
        cardinality.setUpperBound(upperBound);
        return cardinality;
    }

    // CREATE FEATURES

    public static Cardinality createAndCardinality() {
        return CardinialityUtils.createCardinality(0, -1);
    }

    public static Cardinality createOrCardinality() {
        return CardinialityUtils.createCardinality(1, -1);
    }

    public static Cardinality createXorCardinality() {
        return CardinialityUtils.createCardinality(1, 1);
    }

    // FEATURES

    public static boolean isOptional(Cardinality cardinality) {
        return cardinality.getLowerBound() <= 0;
    }

    public static boolean isMandatory(Cardinality cardinality) {
        return cardinality.getLowerBound() > 0;
    }

    public static boolean isMultiple(Cardinality cardinality) {
        return cardinality.getUpperBound() > 1;
    }

    // GROUPS

    public static boolean isAnd(Cardinality c) {
        return c.getLowerBound() == 0 && c.getUpperBound() == -1;
    }

    public static boolean isOr(Cardinality c) {
        return c.getLowerBound() == 1 && c.getUpperBound() == -1;
    }

    public static boolean isXor(Cardinality c) {
        return c.getLowerBound() == 1 && c.getUpperBound() == 1;
    }

    public static boolean isCardinality(Cardinality c) {
        return !isAnd(c) && isOr(c) && isXor(c);
    }
}
