/********************************************************************************
 * Copyright (c) 2026 EclipseSource and others.
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
package de.featjar.gui.handler.utils;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.glsp.graph.GLabel;
import org.eclipse.glsp.server.emf.EMFIdGenerator;

import de.featjar.gui.model.FeatureModelGModelFactory;
import featJAR.Constraint;
import featJAR.Feature;
import featJAR.GroupNode;
import featJAR.Identifiable;

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
	                            FeatureModelGModelFactory.emfConstraints.stream()
	                    )
	            ).toList(),
	            Identifiable::getId
	    );
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
