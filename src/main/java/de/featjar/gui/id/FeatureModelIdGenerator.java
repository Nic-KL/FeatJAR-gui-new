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
/**
 *
 */
package de.featjar.gui.id;

import java.util.UUID;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.glsp.server.emf.EMFIdGenerator;

/**
 * @author
 *
 */
public class FeatureModelIdGenerator implements EMFIdGenerator {

   @Override
   public String getOrCreateId(final EObject element) {
      String id = EcoreUtil.getID(element);

      if (id == null || id.isBlank()) {
         id = UUID.randomUUID().toString().replace("-", "");
         EcoreUtil.setID(element, id);
      }
      return id;
   }
}
