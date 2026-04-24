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
package de.featjar.gui.palette;

import java.util.Optional;

import org.eclipse.glsp.graph.DefaultTypes;
import org.eclipse.glsp.graph.GHtmlRoot;
import org.eclipse.glsp.graph.GLabel;
import org.eclipse.glsp.graph.GModelElement;
import org.eclipse.glsp.graph.builder.impl.GHtmlRootBuilder;
import org.eclipse.glsp.graph.builder.impl.GLabelBuilder;
import org.eclipse.glsp.server.features.popup.PopupModelFactory;
import org.eclipse.glsp.server.features.popup.RequestPopupModelAction;

public class FeaturesPopupModelFactory implements PopupModelFactory {

   @Override
   public Optional<GHtmlRoot> createPopupModel(final GModelElement element, final RequestPopupModelAction action) {

      String type = element.getType();

      System.err.println("type: " + type);

      String tooltipText = null;

      switch (type) {
         case "node":
            tooltipText = "<b>Root Feature</b><br>This is the main entry point of your model.";
            break;
         case "feature-node-mandatory":
            tooltipText = "<b>Mandatory Feature</b><br>This feature must always be included.";
            break;
         case "feature-node-optional":
            tooltipText = "<b>Optional Feature</b><br>This feature can be optionally included.";
            break;
         default:
            tooltipText = "<b>Feature</b><br>Type: " + type;
      }

      int tooltip_id = 0;

      GHtmlRoot popup = new GHtmlRootBuilder()
         .id("popup-" + element.getId())
         .addCssClass("glsp-tooltip")
         .add(createTooltip(tooltipText, tooltip_id++))
         .build();

      return Optional.of(popup);

   }

   public GLabel createTooltip(final String label, final int id) {

      return new GLabelBuilder(DefaultTypes.LABEL)
         .id("tooltip-label-" + id)
         .text(label)
         .addCssClass("tooltip")
         // .addArgument("wrap", true)
         .build();

   }

}
