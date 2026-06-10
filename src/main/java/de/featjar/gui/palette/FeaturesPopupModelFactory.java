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
