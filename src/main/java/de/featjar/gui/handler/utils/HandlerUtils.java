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

import de.featjar.gui.model.FeatureModelGModelFactory;
import featJAR.Constraint;
import featJAR.Feature;
import featJAR.GroupNode;
import featJAR.Identifiable;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.glsp.graph.GLabel;
import org.eclipse.glsp.server.emf.EMFIdGenerator;

public final class HandlerUtils {

    public static <T> Optional<T> findById(final String id, final List<T> items, final Function<T, String> getID) {
        if (items == null || items.isEmpty() || id == null || id.isBlank()) {
            return Optional.empty();
        }
        for (T item : items) {
            if (id.equals(getID.apply(item))) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    public static Optional<Feature> findFeatureById(final String id) {
        return findById(id, FeatureModelGModelFactory.emfFeatures, Feature::getId);
    }

    public static Optional<GroupNode> findGroupNodeById(final String id) {
        return findById(id, FeatureModelGModelFactory.emfGroupNodes, GroupNode::getId);
    }

    public static Optional<Identifiable> findIdentifiableById(final String id) {
        return findById(
                id,
                Stream.<Identifiable>concat(
                                FeatureModelGModelFactory.emfFeatures.stream(),
                                Stream.concat(
                                        FeatureModelGModelFactory.emfGroupNodes.stream(),
                                        FeatureModelGModelFactory.emfConstraints.stream()))
                        .collect(Collectors.toList()),
                Identifiable::getId);
    }

    public static Optional<Constraint> findConstraintById(final String id) {
        return findById(id, FeatureModelGModelFactory.emfConstraints, Constraint::getId);
    }

    public static Optional<GLabel> findLabelById(final String id, final List<GLabel> allLabels) {
        return findById(id, allLabels, GLabel::getId);
    }

    @SuppressWarnings("unused")
    public static void debugPrint(final EMFIdGenerator generator, final Identifiable newIdentifiable) {
        System.err.println("Generator class: " + generator.getClass().getName());
        System.err.println("ID_Generator: " + generator.getOrCreateId(newIdentifiable));
        System.err.println("ID_FeatureGET: " + newIdentifiable.getId());
        System.err.println("ID_Ecore: " + EcoreUtil.getID(newIdentifiable));
    }
}
