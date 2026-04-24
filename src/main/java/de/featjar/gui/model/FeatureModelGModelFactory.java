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
package de.featjar.gui.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.glsp.graph.DefaultTypes;
import org.eclipse.glsp.graph.GDimension;
import org.eclipse.glsp.graph.GGraph;
import org.eclipse.glsp.graph.GLabel;
import org.eclipse.glsp.graph.GModelElement;
import org.eclipse.glsp.graph.GModelRoot;
import org.eclipse.glsp.graph.GNode;
import org.eclipse.glsp.graph.GPoint;
import org.eclipse.glsp.graph.builder.impl.GEdgeBuilder;
import org.eclipse.glsp.graph.builder.impl.GLabelBuilder;
import org.eclipse.glsp.graph.builder.impl.GLayoutOptions;
import org.eclipse.glsp.graph.builder.impl.GNodeBuilder;
import org.eclipse.glsp.graph.util.GConstants;
import org.eclipse.glsp.graph.util.GraphUtil;
import org.eclipse.glsp.server.emf.model.notation.Diagram;
import org.eclipse.glsp.server.emf.notation.EMFNotationGModelFactory;

import de.featjar.gui.handler.utils.IAttributesUtils;
import de.featjar.gui.handler.utils.ICardinialityUtils;
import de.featjar.gui.model.FeatureTreeLayouter.FeatureSubtreeResult;
import de.featjar.gui.model.FeatureTreeLayouter.LayoutContext;
import de.featjar.gui.model.FeatureTreeLayouter.TreeNode;
import de.featjar.gui.types.CardinalityType;
import de.featjar.gui.types.EdgeType;
import de.featjar.gui.types.FeatureModelTypes;
import de.featjar.gui.types.FeatureType;
import de.featjar.gui.types.NodeType;
import featJAR.Cardinality;
import featJAR.Constraint;
import featJAR.Feature;
import featJAR.FeatureModel;
import featJAR.GroupNode;
import featJAR.Identifiable;

public class FeatureModelGModelFactory extends EMFNotationGModelFactory implements IAttributesUtils, ICardinialityUtils{

   // All graphical elements (GModel Elements)
   public static Map<String, String> featureIdMap = new HashMap<>();
   public static List<Feature> emfFeatures = new ArrayList<>();
   public static List<GroupNode> emfGroupNodes = new ArrayList<>();
   public static Map<String, String> constraintIdMap = new HashMap<>();
   public static List<Constraint> emfConstraints = new ArrayList<>();
   List<GNode> gNodes = new ArrayList<>();
   List<GModelElement> gElements = new ArrayList<>();
   List<String> gExpressions = new ArrayList<>();

   // Layout information
   double horizontalGap = 150;
   double verticalGap = 100;
   int nodeWidth = 100;
   int nodeHeight = 30;
   GDimension portSize = GraphUtil.dimension(nodeWidth / 3, nodeHeight / 3);
   
   final GPoint currentPosition = GraphUtil.point(0, 0);

   protected void clearAllGraphicalElements() {
      FeatureTreeLayouter.clear();
      gElements.clear();
      gNodes.clear();
      gExpressions.clear();
      featureIdMap.clear();
      emfFeatures.clear();
      emfGroupNodes.clear();
      emfConstraints.clear();
      constraintIdMap.clear();
   }

   @Override
   protected void fillRootElement(final EObject semanticModel, final Diagram notationModel, final GModelRoot newRoot) {
      FeatureModel emfFeatureModel = FeatureModel.class.cast(semanticModel);
      GGraph graph = GGraph.class.cast(newRoot);

      // If no root element is found, then render nothing
      if (emfFeatureModel.getRoots().size() == 0) {
         return;
      }

      clearAllGraphicalElements();

      // Render for now only the first root
      Feature emfRoot = emfFeatureModel.getRoots().get(0);

      // Create the graphical elements without applying layout first
      FeatureSubtreeResult gRoot = buildFeatureSubtree(emfRoot, true);

      // Autolayouting the feature tree
      layoutFeatureTree(gRoot);

      // Creating the constraint box and autolayouting it
      createConstraintLegend(
         emfFeatureModel.getConstraints(),
         layoutConstraintLegend(gRoot.gNode));

      graph.getChildren().addAll(gNodes);
      graph.getChildren().addAll(gElements);
   }
 
   private FeatureSubtreeResult buildFeatureSubtree(Feature feature, boolean isRoot) {
	   Cardinality featureCardinality = feature.getCardinality();
	   String primaryCss = isRoot ? FeatureModelTypes.ROOT_FEATURE : CardinalityType.of(featureCardinality).value();
	   GNode gNode = createNode(feature, currentPosition, primaryCss);
      
	    FeatureType secondary = getFeatureType(feature);
	    if (secondary != FeatureType.NONE) {
	        gNode.getCssClasses().add(secondary.value());
	    }
	   
	   TreeNode currentNode = new TreeNode(gNode.getId());
	   
      for (GroupNode child : feature.getGroupNodeList()) {
          FeatureSubtreeResult gChild = buildNodeSubtree(child);
          currentNode.addChild(gChild.treeNode);
          createEdge(feature, child, gNode, gChild.gNode);
       }
      registerFeature(gNode, feature);
      return new FeatureSubtreeResult(gNode, currentNode);
   }
   
   private void registerFeature(GNode gNode, Feature feature) {
       gNodes.add(gNode);
       featureIdMap.put(gNode.getId(), feature.getId());
       emfFeatures.add(feature);
   }
   
   private FeatureSubtreeResult buildNodeSubtree(GroupNode groupNode) {
       GNode gNode;
	   Cardinality groupNodeCardinality = groupNode.getCardinality();
	   gNode = createNode(groupNode, currentPosition, resolveGroupType(groupNodeCardinality).value());
	   TreeNode currentNode = new TreeNode(gNode.getId());
	   
	  for (Feature child : groupNode.getFeatureList()) {
		   // TODO wont work exactly
		   if(!isHidden(child)) {
//			   gNode.getCssClasses().add(CssClass.HIDDEN.getCssClass());
			   FeatureSubtreeResult gChild = buildFeatureSubtree(child, false);
			   currentNode.addChild(gChild.treeNode);
			   createEdge(groupNode, child, gNode, gChild.gNode);
		   }
		  }
		  registerGroupNode(groupNode, gNode);
	   
	   return new FeatureSubtreeResult(gNode, currentNode);
   }

   private void registerGroupNode(GroupNode groupNode, GNode gNode) {
	   gNodes.add(gNode);
	  featureIdMap.put(gNode.getId(), groupNode.getId());
	  emfGroupNodes.add(groupNode);
   }
   
   private NodeType resolveGroupType(Cardinality c) {
	    if (isOr(c))  return NodeType.OR_NODE;
	    if (isXor(c)) return NodeType.XOR_NODE;
	    if (isAnd(c)) return NodeType.AND_NODE;
	    return NodeType.CARDINALITY_NODE;
	}
   
   // Create the graphical representation of a feature
   protected GNode createNode(final Identifiable identifiable, final GPoint gPosition, final String cssType) {

      GNodeBuilder nodeBuilder = new GNodeBuilder(DefaultTypes.NODE)
         .id(idGenerator.getOrCreateId(identifiable))
         .addCssClass(cssType)
         .position(gPosition)
         .layout(GConstants.Layout.VBOX)
         .layoutOptions(new GLayoutOptions()
            .vAlign(GConstants.VAlign.CENTER).hAlign(GConstants.HAlign.CENTER).minWidth(nodeWidth)
            .minHeight(nodeHeight));
      
      	if(identifiable instanceof Feature) {
      		nodeBuilder.add(new GLabelBuilder(FeatureModelTypes.EDITABLE_LABEL)
      	            .text(identifiable.getName())
      	            .id(identifiable.getId() + "_label")
      	            .build());
      	}


      // Experimentell ports
      // GPort port = new GPortBuilder("event:port")
      // .position(GraphUtil.point(0, 0))
      // .size(portSize).build();
      //
      // featureNodeBuilder.add(port);

      applyShapeData(identifiable, nodeBuilder);
      GNode gNode = nodeBuilder.build();

      // featureNode.getChildren().add(port);
      // gElements.add(port);

      return gNode;
   }

   // Create the graphical representation of the feature relation
   protected void createEdge(final Identifiable source, final Identifiable target, final GNode gSource, final GNode gTarget) {
      EdgeType edgeType;
	   
	  if(target instanceof Feature) {
		  Feature feature = (Feature) target;
		  Cardinality featureCardinality = feature.getCardinality();
		  Cardinality featureGroupCardinality = feature.getParent().getCardinality();
		  
		  if(isOr(featureGroupCardinality) || isXor(featureGroupCardinality)) {
			  edgeType = EdgeType.BASIC_EDGE;			  
		  } else if(isOptional(featureCardinality)) {
			  edgeType = EdgeType.OPTIONAL_EDGE;
		  } else if (isMandatory(featureCardinality)) {
			  edgeType = EdgeType.MANDATORY_EDGE;
		  } else {
			  edgeType = EdgeType.BASIC_EDGE; 
		  }		  
	  } else {
		  edgeType = EdgeType.BASIC_EDGE;
	  }  
	  
      gElements.add(new GEdgeBuilder(edgeType.value())
    	         .id(source.getId() + "_to_" + target.getId())
    	         .source(gSource).target(gTarget).build());
   }
   
   // Run to automatically layout the feature nodes nicely
   protected void layoutFeatureTree(final FeatureSubtreeResult gRoot) {
      LayoutContext ctx = new FeatureTreeLayouter.LayoutContext();
      FeatureTreeLayouter.computePositions(FeatureTreeLayouter.mapGNodeToTreeNode(gRoot.gNode), 0, horizontalGap,
         verticalGap, nodeWidth, nodeHeight, ctx);

      // Map computed positions back to the GNodes
      for (TreeNode node : FeatureTreeLayouter.allTreeNodes) {
         GNode gNode = FeatureTreeLayouter.mapTreeNodeToGNode(node, gNodes);
         gNode.setPosition(GraphUtil.point(node.x, node.y));

         // Experimentell ports
         // GPoint portPosition = GraphUtil.point(node.x + nodeWidth / 2 - portSize.getWidth() / 2,
         // node.y + nodeHeight - portSize.getHeight() / 2);
         // GPort.class.cast(gNode.getChildren().get(1)).setPosition(portPosition);

      }
   }

   // Place constraints box under the rightmost (last) leaf, centered horizontally
   protected GPoint layoutConstraintLegend(final GNode gRoot) {
      TreeNode rootTree = FeatureTreeLayouter.mapGNodeToTreeNode(gRoot);
      double marginY = 160; // tweak spacing below the last leaf
      double rootCenterX = rootTree.x;
      double legendTopY = FeatureTreeLayouter.computeYBelowDeepestRightmostLeaf(rootTree, nodeHeight, marginY);

      return GraphUtil.point(rootCenterX, legendTopY);
   }

   // Create a box with all existing constraints as strings
   // coords is treated as: (centerX, topY) anchor under the rightmost leaf
   protected GNode createConstraintLegend(final List<Constraint> constraints, final GPoint coords) {

      // Calculate size based on content
      int lines = 1 /* title */ + 1 /* first sep */ + (constraints.size() * 2);
      int lineHeight = 20;
      int padding = 40;
      int legendHeight = Math.max(200, lines * lineHeight + padding);
      int legendWidth = 290; // fixed width; adjust as needed

      int id = 0;

      // Center horizontally on coords.getX(), place top at coords.getY()
      double topLeftX = coords.getX();
      double topLeftY = coords.getY();

      GNodeBuilder legendBuilder = new GNodeBuilder("node:rectangle")
         .id("cross-tree-contraints")
         .size(GraphUtil.dimension(legendWidth, legendHeight))
         .position(GraphUtil.point(topLeftX, topLeftY))
         .layout(GConstants.Layout.VBOX)
         .addCssClass(FeatureModelTypes.CONSTRAINT_BOX);

      // Add title
      legendBuilder.add(createConstraintsTitle("Constraints"));
      legendBuilder.add(createLineLabel(id++, "."));

      // Add constraint strings
      for (Constraint constraint : constraints) {
         legendBuilder.add(createConstraintLabel(constraint, id++));
         legendBuilder.add(createLineLabel(id++, "-"));
      }

      GNode legend = legendBuilder.build();

      legend.setPosition(
         GraphUtil.point(legend.getPosition().getX() + (nodeWidth / 2) - (legendWidth / 2),
            legend.getPosition().getY()));

      gElements.add(legend);

      return legend;
   }

   // Create a label to draw a separating line using a specific symbol
   public GLabel createLineLabel(final int id, final String separator) {
      return new GLabelBuilder(DefaultTypes.LABEL)
         .id("constraints-label-line_" + id)
         .text(separator.repeat(30))
         .addCssClass(FeatureModelTypes.CONSTRAINT_LABEL) 
         .addArgument("wrap", true)
         .build();
   }

   // Create the label for the constraints
   public GLabel createConstraintsTitle(final String label) {
      return new GLabelBuilder(DefaultTypes.LABEL)
         .id("constraints_title")
         .text(label)
         .addCssClass(FeatureModelTypes.CONSTRAINT_TITLE) 
         .addArgument("wrap", true)
         .build();
   }

   // Create a label as GElement with a specific id and string value
   public GLabel createConstraintLabel(final Constraint constraint, final int id) {

      String gId = "constraints-label_" + id;

      GLabel gConstraint = new GLabelBuilder(FeatureModelTypes.EDITABLE_LABEL)
         .id(gId)
         .text(constraint.getName())
         .addCssClass(FeatureModelTypes.CONSTRAINT_LABEL)
         .addArgument("wrap", true)
         .build();

      emfConstraints.add(constraint);
      constraintIdMap.put(gId, constraint.getId());

      return gConstraint;
   }

}
