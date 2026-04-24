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

import org.eclipse.glsp.graph.DefaultTypes;
/**
 * <p> Use the classes of {@link de.featjar.gui.types} instead.
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
