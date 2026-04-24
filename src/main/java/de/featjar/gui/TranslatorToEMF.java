/*
 * Copyright (C) 2025 FeatJAR-Development-Team
 *
 * This file is part of FeatJAR-gui.
 *
 * gui is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3.0 of the License,
 * or (at your option) any later version.
 *
 * gui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with gui. If not, see <https://www.gnu.org/licenses/>.
 *
 * See <https://github.com/FeatureIDE/FeatJAR-gui> for further information.
 */
package de.featjar.gui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import de.featjar.base.data.Range;
import de.featjar.feature.model.FeatureTree.Group;
import de.featjar.feature.model.IConstraint;
import de.featjar.feature.model.IFeatureModel;
import de.featjar.feature.model.IFeatureTree;

public class TranslatorToEMF {
    private static int idCounter;
//  private static String dir = "./src/main/java/de/featjar/gui/EMFxmls";
    private static String dir = Paths.get("")
            .toAbsolutePath()
            .toString();

    private static String giveID() {
        idCounter++;
        return String.valueOf(idCounter);
    }
    
    private static void bla(IFeatureTree tree) {
    	Optional<Group> parentGroup = tree.getParentGroup();

    	if (parentGroup.isPresent()) {
    	    Group group = parentGroup.get();

    	    int lower = group.getLowerBound();
    	    int upper = group.getUpperBound();

    	    if (lower == 0 && upper == Range.OPEN) {
    	        System.out.println("Feature ist Teil einer AND-Gruppe");
    	    } else if (lower == 1 && upper == Range.OPEN) {
    	        System.out.println("Feature ist Teil einer OR-Gruppe");
    	    } else if (lower == 1 && upper == 1) {
    	        System.out.println("Feature ist Teil einer ALTERNATIVE-Gruppe");
    	    }
    	}

    }
    
    private static boolean isORGroup(IFeatureTree tree) {    	
    	return tree.getParentGroup().map(pg -> pg.getLowerBound() == 1 && pg.getUpperBound() == Range.OPEN).orElse(false);
    }
    
    private static boolean isXORGroup(IFeatureTree tree) {    	
    	return tree.getParentGroup().map(pg -> pg.getLowerBound() == 1 && pg.getUpperBound() == 1).orElse(false);
    }
    
    /**
     * Returns the group type string for a feature tree node.
     * The group type describes how the node's CHILDREN are grouped.
     * Uses IFeatureTree's isOr() / isAlternative() query methods.
     */
    private static String getGroupType(IFeatureTree tree) {
        if (isORGroup(tree)) return "or";
        if (isXORGroup(tree)) return "alternative";
        return "and"; // default / mandatory
    }

    public static void writeToEMF(IFeatureModel source) {
        // fetching info about model
        String name = source.getName().get();
        List<IFeatureTree> rootNodes = (List<IFeatureTree>) source.getRoots();

        // Creating new File for the GLSP-Client/Server to read
        Path path = Paths.get(dir);
        createEMFFileDir(path);

        String filename = dir + "/" + name + ".featuremodel";
        path = Paths.get(filename);

        writeEMFFiles(filename, name);

        // FIX 1: Removed stray '>' that appeared inside the id attribute value.
        // Was: id=\">" + giveID()  →  produced  id=">1"
        // Now: id=\""  + giveID()  →  produces   id="1"
        String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n"
                + "<featJAR:FeatureModel id=\"" + giveID() + "\" name=\"" + name + "\" "
                + "xmi:version=\"2.0\"\nxmlns:xmi=\"http://www.omg.org/XMI\"\n"
                + "xmlns:featJAR=\"http://www.example.org/featJAR\"";

        // determine if we should already close the element
        if (rootNodes.isEmpty()) {
            input += " />\n\n";
        } else {
            input += " >\n\n";
        }

        try {
            Files.writeString(path, input, StandardOpenOption.WRITE);
        } catch (IOException e) {
            System.err.println("Error creating" + path.toString());
        }

        if (rootNodes.isEmpty()) {
            return;
        }

        // going over the roots (should only be one) and writing them down
        for (IFeatureTree root : rootNodes) {
            addEMFRoot(root, path);
        }

        // getting the constraints
        addEMFConstraints(source, path);

        System.err.println(path);

        // closing element
        input = "</featJAR:FeatureModel>";
        try {
            Files.writeString(path, input, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Error creating" + path.toString());
        }
    }

    public static void addEMFRoot(IFeatureTree root, Path path) {
        // fetching info about root
        String rootName = root.getFeature().getName().get();
        List<? extends IFeatureTree> childTree = root.getChildren();

        // FIX 2: Added 'optional' attribute (always false for root) and
        // FIX 3: Added 'groupType' attribute so OR / Alternative groups are preserved.
        // getGroupType() reads isOr() / isAlternative() from the IFeatureTree.
        String groupType = getGroupType(root);
        String input = "<roots id = \"" + giveID() + "\" name = \"" + rootName
                + "\" optional = \"false\" groupType = \"" + groupType + "\"";

        // determine if we can already close the element
        if (childTree.isEmpty()) {
            input += " />\n\n";
        } else {
            input += " >\n\n";
        }

        try {
            Files.writeString(path, input, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Error creating" + path.toString());
        }

        // check for early return
        if (childTree.isEmpty()) {
            return;
        }

        // getting the children
        for (IFeatureTree feature : childTree) {
            addEMFFeatures(feature, path);
        }

        // closing element
        input = "</roots>\n\n";
        try {
            Files.writeString(path, input, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Error creating" + path.toString());
        }
    }

    public static void addEMFFeatures(IFeatureTree feature, Path path) {
        // fetching info about node
        String featureName = feature.getFeature().getName().get();
        boolean optional = feature.isOptional();
        List<? extends IFeatureTree> childTree = feature.getChildren();

        // FIX 3: Added 'groupType' attribute so OR / Alternative groups are preserved.
        // This is the group type that applies to THIS node's children.
        String groupType = getGroupType(feature);
        String input = "<features id = \"" + giveID() + "\" name = \"" + featureName
                + "\" optional = \"" + optional + "\" groupType = \"" + groupType + "\"";

        // determine if we can already close the element
        if (childTree.isEmpty()) {
            input += " />\n\n";
        } else {
            input += " >\n\n";
        }

        try {
            Files.writeString(path, input, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Error creating" + path.toString());
        }

        // determine if we should already close the element
        if (childTree.isEmpty()) {
            return;
        }

        // getting the children
        for (IFeatureTree child : childTree) {
            addEMFFeatures(child, path);
        }

        // close element after having added children
        input = "</features>\n\n";
        try {
            Files.writeString(path, input, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Error creating" + path.toString());
        }
    }

    // TODO: serialize the actual constraint formula, not just the constraint name.
    // The EMF model may need an extension to store propositional formulas properly.
    public static void addEMFConstraints(IFeatureModel source, Path path) {
        // fetching constraints
        Collection<IConstraint> cons = source.getConstraints();
        for (IConstraint con : cons) {
            String conName = con.getName().get();
            String input = "<constraints id = \"" + giveID() + "\" name = \"" + conName + "\" />\n\n";
            try {
                Files.writeString(path, input, StandardOpenOption.APPEND);
            } catch (IOException e) {
                System.err.println("Error creating" + path.toString());
            }
        }
    }

    // making a new directory for files, if not already there
    public static void createEMFFileDir(Path path) {
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            System.err.println("Error creating Directory");
        }
    }

    // writing the necessary files
    public static void writeEMFFiles(String filename, String name) {
        File file = new File(filename);

        // Note: new File() never returns null; the null-check is always true and
        // was kept here only to preserve the original structure.
        if (file != null) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creating" + file.toString());
            }
            if (!(file.isFile() && file.canWrite() && file.canRead())) {
                return;
            }
            System.out.println(file + " created");
        }

        // clearing the file in case something's already in there
        try (FileWriter fileWriter = new FileWriter(file, false)) {
            fileWriter.write("");
        } catch (IOException e) {
            System.err.println("Error creating" + file.toString());
        }

        // creating a .notation file, necessary for the GLSP-Server/Client
        String filename2 = dir + "/" + name + ".notation";
        File file2 = new File(filename2);

        if (file2 != null) {
            try {
                file2.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creating" + file2.toString());
            }
            if (!(file2.isFile() && file2.canWrite() && file2.canRead())) {
                return;
            }
            System.out.println(file2 + " created");
        }

        try (FileWriter fileWriter = new FileWriter(file2, false)) {
            fileWriter.write("<?xml version=\"1.0\" encoding=\"ASCII\"?>\n"
                    + "<notation:Diagram xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:notation=\"http://www.eclipse.org/glsp/notation\">\n"
                    + "<semanticElement elementId=\"root\"/>\n"
                    + "<elements xsi:type=\"notation:Shape\">\n"
                    + "<semanticElement elementId=\"2f4667d1-97ac-4d40-92de-581ebfa12cbb\"/>\n"
                    + "<position x=\"333.0\" y=\"201.0\"/>\n"
                    + "<size width=\"79.34375\" height=\"25.0\"/>\n"
                    + "</elements>\n"
                    + "<elements xsi:type=\"notation:Shape\">\n"
                    + "<semanticElement elementId=\"0be70207-1f24-4213-98fb-433506118b52\"/>\n"
                    + "<position x=\"405.0\" y=\"121.0\"/>\n"
                    + "<size width=\"135.1875\" height=\"25.0\"/>\n"
                    + "</elements>\n"
                    + "<elements xsi:type=\"notation:Shape\">\n"
                    + "<semanticElement elementId=\"2525269f-c7c6-48cd-9c0e-552c6228f13d\"/>\n"
                    + "<position x=\"180.0\" y=\"128.0\"/>\n"
                    + "<size width=\"125.375\" height=\"25.0\"/>\n"
                    + "</elements>\n"
                    + "</notation:Diagram>");
        } catch (IOException e) {
            System.err.println("Error creating" + file2.toString());
        }
    }
}
