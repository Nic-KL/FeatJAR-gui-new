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

import de.featjar.gui.types.AttributeKeys;
import de.featjar.gui.types.CardinalityType;
import de.featjar.gui.types.FeatureImplementationTypes;
import de.featjar.gui.types.FeatureModelLables;
import de.featjar.gui.types.NodeType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.eclipse.glsp.server.actions.TriggerNodeCreationAction;
import org.eclipse.glsp.server.features.toolpalette.PaletteItem;
import org.eclipse.glsp.server.features.toolpalette.ToolPaletteItemProvider;

public class FeatureModelToolPaletteItemProvider implements ToolPaletteItemProvider {

    @Override
    public List<PaletteItem> getItems(final Map<String, String> args) {
        return combinePalletes();
    }

    private PaletteItem featureModificationn() {
        List<PaletteItem> nodes = new ArrayList<>();
        PaletteItem makeAbstract = node(FeatureImplementationTypes.ABSTRACT.value(), "Make Abstract");
        PaletteItem makeConcrete = node(FeatureImplementationTypes.CONCRETE.value(), "Make Concrete");
        PaletteItem makeHidden = node(AttributeKeys.HIDDEN, "Make Hidden");

        nodes.add(makeAbstract);
        nodes.add(makeConcrete);
        nodes.add(makeHidden);

        return PaletteItem.createPaletteGroup("nodes", "Modify Feature", nodes, "symbol-property");
    }

    private PaletteItem featureCreation() {
        List<PaletteItem> nodes = new ArrayList<>();
        PaletteItem createOptionalFeature = node(CardinalityType.OPTIONAL_FEATURE.value(), "Optional Feature");
        PaletteItem createMandatoryFeature = node(CardinalityType.MANDATORY_FEATURE.value(), "Mandatory Feature");
        PaletteItem createCarinalityFeature = node(CardinalityType.MULTIPLE_FEATURE.value(), "Multiple Feature");
        //      PaletteItem createOrXorFeature = node(FeatureModelTypes.ORXOR_FEATURE, "OR-XOR Feature");

        nodes.add(createOptionalFeature);
        nodes.add(createMandatoryFeature);
        nodes.add(createCarinalityFeature);

        return PaletteItem.createPaletteGroup("nodes", "Add Features", nodes, "symbol-property");
    }

    private PaletteItem nodeCreation() {
        List<PaletteItem> nodes = new ArrayList<>();
        PaletteItem createOrGroupNode = node(NodeType.OR_NODE.value(), "OR Node");
        PaletteItem createXorGroupNode = node(NodeType.XOR_NODE.value(), "XOR Node");
        PaletteItem createAndGroupNode = node(NodeType.AND_NODE.value(), "AND Node");
        PaletteItem createcardinalityGroupNode = node(NodeType.CARDINALITY_NODE.value(), "CARDINALITY Node");

        nodes.add(createAndGroupNode);
        nodes.add(createcardinalityGroupNode);
        nodes.add(createOrGroupNode);
        nodes.add(createXorGroupNode);

        return PaletteItem.createPaletteGroup("nodes", "Add Nodes", nodes, "symbol-property");
    }

    private PaletteItem constraintCreation() {
        List<PaletteItem> nodes = new ArrayList<>();
        PaletteItem createConstraint = node(FeatureModelLables.CONSTRAINT, "Constraint");

        nodes.add(createConstraint);

        return PaletteItem.createPaletteGroup("nodes", "Add Constraints", nodes, "symbol-property");
    }

    private PaletteItem node(final String elementTypeId, final String label) {
        return new PaletteItem(elementTypeId, label, new TriggerNodeCreationAction(elementTypeId));
    }

    private List<PaletteItem> combinePalletes() {
        List<PaletteItem> palettes = new ArrayList<>();

        palettes.add(featureCreation());
        palettes.add(nodeCreation());
        palettes.add(featureModificationn());
        palettes.add(constraintCreation());

        // palletes.add(informationLegend());

        return palettes;
    }

    private PaletteItem informationLegend() {

        // info about node types
        PaletteItem root_node = new PaletteItem("root_node", "🟩 Root");
        // root_node.setIcon("root_node_icon");
        root_node.setSortString("1");

        PaletteItem mandatory_node = new PaletteItem("mandatory_node", "🟥 Mandatory");
        // mandatory_node.setIcon("mandatory_node_icon");
        mandatory_node.setSortString("2");

        PaletteItem optional_node = new PaletteItem("optional_node", "🟪 Optional");
        // optional_node.setIcon("optional_node_icon");
        optional_node.setSortString("3");

        // Info about group types

        PaletteItem true_group = new PaletteItem("TRUE", "True Group");
        // true_group.setIcon("true_group_icon");
        true_group.setIcon("loading");
        true_group.setSortString("4");

        PaletteItem or_group = new PaletteItem("OR", "Or Group");
        // or_group.setIcon("or_group_icon");
        or_group.setIcon("triangle-up");
        or_group.setSortString("5");

        PaletteItem xor_group = new PaletteItem("XOR", "Xor Group");
        // xor_group.setIcon("xor_group_icon");
        xor_group.setIcon("debug-breakpoint-log-unverified");
        xor_group.setSortString("6");

        PaletteItem speical_group = new PaletteItem("SPECIAL", "Special Group");
        // speical_group.setIcon("special_group_icon");
        speical_group.setIcon("loading");
        speical_group.setSortString("7");

        List<PaletteItem> legend = new ArrayList<>();

        legend.add(xor_group);
        legend.add(true_group);
        legend.add(or_group);
        legend.add(speical_group);
        legend.add(root_node);
        legend.add(mandatory_node);
        legend.add(optional_node);

        return PaletteItem.createPaletteGroup("information - Currently quite useless", "Information", legend, "book");
    }
}
