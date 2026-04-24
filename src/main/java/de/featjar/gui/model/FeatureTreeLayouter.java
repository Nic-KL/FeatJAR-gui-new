package de.featjar.gui.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.glsp.graph.GNode;

public class FeatureTreeLayouter {

   // class datastructure which holds GNode and corresponding Layout Treenode
   protected static class FeatureSubtreeResult {
      public final GNode gNode;
      public final FeatureTreeLayouter.TreeNode treeNode;

      public FeatureSubtreeResult(final GNode gNode, final FeatureTreeLayouter.TreeNode treeNode) {
         this.gNode = gNode;
         this.treeNode = treeNode;
      }
   }

   // Static list to keep track of all TreeNode instances
   public static List<TreeNode> allTreeNodes = new ArrayList<>();

   public static class TreeNode {
      public String id;
      public double x;
      public double y;
      public List<TreeNode> children = new ArrayList<>();

      public TreeNode(final String id) {
         this.id = id;
         // Add this instance to the static list whenever a new node is created
         allTreeNodes.add(this);
      }

      public void addChild(final TreeNode child) {
         children.add(child);
      }

      @Override
      public String toString() {
         return String.format("%s(%.1f, %.1f)", id, x, y);
      }
   }

   /** Lightweight point for returning coordinates without extra deps. */
   public static class Point {
      public final double x;
      public final double y;

      public Point(final double x, final double y) {
         this.x = x;
         this.y = y;
      }

      @Override
      public String toString() {
         return "Point(" + x + ", " + y + ")";
      }
   }

   public static class LayoutContext {
      public double nextX = 0; // keeps track of horizontal position
   }

   public static void computePositions(final TreeNode node, final double startY,
      double horizontalSpacing, double verticalSpacing, final double nodeWidth, final double nodeHeight,
      final LayoutContext ctx) {
      if (node == null) {
         return;
      }

      node.y = startY;

      horizontalSpacing = Math.max(horizontalSpacing, nodeWidth * 1.2);
      verticalSpacing = Math.max(verticalSpacing, nodeHeight * 1.2);

      // Leaf node
      if (node.children.isEmpty()) {
         node.x = ctx.nextX;
         ctx.nextX += horizontalSpacing;
      }
      // Internal node
      else {
         for (TreeNode child : node.children) {
            computePositions(child, startY + verticalSpacing,
               horizontalSpacing, verticalSpacing, nodeWidth, nodeHeight, ctx);
         }

         double left = node.children.get(0).x;
         double right = node.children.get(node.children.size() - 1).x;
         node.x = (left + right) / 2.0;
      }
   }

   public static GNode mapTreeNodeToGNode(final TreeNode treeNode, final List<GNode> gNodeList) {
      for (GNode g : gNodeList) {
         if (g.getId().equals(treeNode.id)) { // Use equals() for String comparison
            return g;
         }
      }
      return null; // return null if no match is found
   }

   public static TreeNode mapGNodeToTreeNode(final GNode gnode) {
      for (TreeNode t : allTreeNodes) {
         if (t.id.equals(gnode.getId())) { // Use equals() for String comparison
            return t;
         }
      }
      return null; // return null if no match is found
   }

   public static void clear() {
      allTreeNodes.clear();
   }

   /**
    * Returns the "last child" in display order: repeatedly takes the last child
    * of each node until a leaf is reached. Returns null if root is null.
    */
   public static TreeNode findRightmostLeaf(final TreeNode root) {
      if (root == null) {
         return null;
      }
      TreeNode current = root;
      while (!current.children.isEmpty()) {
         current = current.children.get(current.children.size() - 1);
      }
      return current;
   }

   /**
    * Computes an anchor point centered horizontally under the rightmost leaf.
    *
    * @param root       The layout tree root (from mapGNodeToTreeNode(gRootNode)).
    * @param nodeWidth  The width used for feature nodes in layout.
    * @param nodeHeight The height used for feature nodes in layout.
    * @param marginY    Additional vertical gap below the leaf.
    * @return Point where x = center under the leaf, y = bottom + margin.
    */
   public static Point computeAnchorBelowRightmostLeaf(final TreeNode root,
      final double nodeWidth,
      final double nodeHeight,
      final double marginY) {
      TreeNode leaf = findRightmostLeaf(root);
      if (leaf == null) {
         return new Point(0, 0);
      }
      double centerX = leaf.x + (nodeWidth / 2.0);
      double bottomY = leaf.y + nodeHeight + marginY;
      return new Point(centerX, bottomY);
   }

   /**
    * Computes the TOP-LEFT position to place a rectangular legend of given size
    * so that it is horizontally centered under the rightmost leaf.
    *
    * @param root         The layout tree root.
    * @param legendWidth  The legend's width.
    * @param legendHeight The legend's height (not used for centering, but handy).
    * @param nodeWidth    The feature node width used in layout.
    * @param nodeHeight   The feature node height used in layout.
    * @param marginY      Vertical margin below the leaf.
    * @return Point = top-left for the legend rectangle.
    */
   public static Point computeLegendTopLeftUnderRightmostLeaf(final TreeNode root,
      final double legendWidth,
      final double legendHeight,
      final double nodeWidth,
      final double nodeHeight,
      final double marginY) {
      Point anchor = computeAnchorBelowRightmostLeaf(root, nodeWidth, nodeHeight, marginY);
      double topLeftX = anchor.x - (legendWidth / 2.0);
      double topLeftY = anchor.y; // place top edge at anchor; add extra offset if desired
      return new Point(topLeftX, topLeftY);
   }

   // Find the deepest (max y) leaf under `root`. If multiple share the same y,
   // pick the rightmost (max x).
   public static TreeNode findDeepestRightmostLeaf(final TreeNode root) {
      if (root == null) {
         return null;
      }

      TreeNode best = null;
      double bestY = Double.NEGATIVE_INFINITY;
      double bestX = Double.NEGATIVE_INFINITY;

      // Simple DFS
      final ArrayList<TreeNode> stack = new ArrayList<>();
      stack.add(root);
      while (!stack.isEmpty()) {
         TreeNode n = stack.remove(stack.size() - 1);
         if (n.children.isEmpty()) {
            if (n.y > bestY || (n.y == bestY && n.x > bestX)) {
               best = n;
               bestY = n.y;
               bestX = n.x;
            }
         } else {
            // push children
            for (TreeNode element : n.children) {
               stack.add(element);
            }
         }
      }
      return best;
   }

   /** Y just below the deepest-rightmost leaf (pixel coords, after layout). */
   public static double computeYBelowDeepestRightmostLeaf(final TreeNode root,
      final double nodeHeight,
      final double marginY) {
      TreeNode leaf = findDeepestRightmostLeaf(root);
      if (leaf == null) {
         return marginY;
      }
      return leaf.y + nodeHeight + marginY;
   }
}
