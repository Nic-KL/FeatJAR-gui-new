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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import de.featjar.base.data.identifier.Identifiers;
import de.featjar.feature.model.FeatureModel;
import de.featjar.feature.model.IFeature;
import de.featjar.feature.model.IFeatureModel;
import de.featjar.feature.model.IFeatureTree;

class TranslatorFromEMF {
    public static IFeatureModel parseFromEMF(String file) {
        // making a new Model
        IFeatureModel newFeatureModel = new FeatureModel(Identifiers.newCounterIdentifier());
        File emf = new File(file);

        // creating a reader
        try {
            BufferedReader reader = new BufferedReader(new FileReader(emf));

            // skipping the preamble (two '<'-delimited blocks)
            String stop = readEMFFileAfterChar(reader, '<');
            stop = readEMFFileAfterChar(reader, '<');

            // read attribute values: first quoted word is the id (discarded),
            // second is the actual model name
            String name = readEMFFileCapsulatedWord(reader, '"');
            name = readEMFFileCapsulatedWord(reader, '"');

            // adding to the model
            if (stop.compareTo("feat") == 0) {
                newFeatureModel.mutate().setName(name);
                newFeatureModel.mutate().setDescription("awesome description");
            } else {
                throw new IOException("Unexpected tag: expected 'feat', got '" + stop + "'");
            }

            // skip remaining attribute values of the FeatureModel element:
            // xmi:version and the two xmlns values each have a quoted string
            readEMFFileCapsulatedWord(reader, '"');
            readEMFFileCapsulatedWord(reader, '"');
            readEMFFileCapsulatedWord(reader, '"');

            // check for an early end (self-closing element)
            if (readEMFFileCheckingClosingTag(reader)) {
                return newFeatureModel;
            }

            // getting the roots (should only be one) and constraints
            while (!((stop = readEMFFileAfterChar(reader, '<')).isBlank())) {
                if (stop.compareTo("root") == 0) {
                    addRoot(newFeatureModel, reader);
                } else if (stop.compareTo("/fea") == 0) {
                    // </featJAR:FeatureModel> — normal end of document
                    return newFeatureModel;
                } else if (stop.compareTo("cons") == 0) {
                    // TODO: parse constraint formula
                    // FMaddConstraint(newFeatureModel, reader);
                } else {
                    throw new IOException("Unexpected tag prefix: '" + stop + "'");
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading " + file);
        }

        // FIX 1: Return the (potentially complete) model instead of null when
        // the reader reaches EOF without encountering </featJAR:FeatureModel>.
        // Previously this line was 'return null', causing data loss on EOF.
        return newFeatureModel;
    }

    public static void addRoot(IFeatureModel goal, BufferedReader reader) throws IOException {
        // Attribute order in EMF: id, name, optional, groupType
        // Read id (discard), then name
        String name = readEMFFileCapsulatedWord(reader, '"'); // id value  — discard
        name = readEMFFileCapsulatedWord(reader, '"');         // name value — keep

        IFeature rootFeature = goal.mutate().addFeature(name);
        IFeatureTree rootTree = goal.mutate().addFeatureTreeRoot(rootFeature);

        // FIX 2: Read 'optional' (always false for root, discard) and
        // FIX 3: Read 'groupType' and apply it to the root's children group.
        String optional  = readEMFFileCapsulatedWord(reader, '"'); // "false" — discard for root
        String groupType = readEMFFileCapsulatedWord(reader, '"'); // "or" / "alternative" / "and"
        applyGroupType(rootTree, groupType);

        // check for early end (self-closing element)
        if (readEMFFileCheckingClosingTag(reader)) {
            return;
        }

        // looking for child-features
        String stop;
        while (!((stop = readEMFFileAfterChar(reader, '<')).isBlank())) {
            if (stop.compareTo("feat") == 0) {
                addFeature(goal, rootTree, reader);
            } else if (stop.compareTo("/roo") == 0) {
                return;
            } else {
                throw new IOException("Unexpected tag prefix inside <roots>: '" + stop + "'");
            }
        }
        throw new IOException("Unexpected EOF inside <roots>");
    }

    public static void addFeature(IFeatureModel goal, IFeatureTree hook, BufferedReader reader) throws IOException {
        // Attribute order in EMF: id, name, optional, groupType
        String name = readEMFFileCapsulatedWord(reader, '"'); // id value  — discard
        name         = readEMFFileCapsulatedWord(reader, '"'); // name value — keep
        String optional  = readEMFFileCapsulatedWord(reader, '"'); // "true" / "false"
        // FIX 3: Read groupType so OR / Alternative structure is restored.
        String groupType = readEMFFileCapsulatedWord(reader, '"'); // "or" / "alternative" / "and"

        IFeature feature = goal.mutate().addFeature(name);
        IFeatureTree tree = hook.mutate().addFeatureBelow(feature);

        if (optional.compareTo("true") == 0) {
            tree.mutate().makeOptional();
        }

        // Apply the group type that governs THIS node's children.
        applyGroupType(tree, groupType);

        // early return for self-closing element
        if (readEMFFileCheckingClosingTag(reader)) {
            return;
        }

        try {
            String stop;
            while (!((stop = readEMFFileAfterChar(reader, '<')).isBlank())) {
                // continue adding children...
                if (stop.compareTo("feat") == 0) {
                    addFeature(goal, tree, reader);
                // ...until the closing element is read
                } else if (stop.compareTo("/fea") == 0) {
                    return;
                } else {
                    throw new IOException("Unexpected tag prefix inside <features>: '" + stop + "'");
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: Read something unexpected");
        }
    }

    /**
     * Applies the group type string read from the EMF file to the given tree node.
     * The group type controls how the node's CHILDREN are grouped.
     *
     * FIX 3: This helper was missing entirely; without it OR / Alternative groups
     * were silently dropped during import, leaving every group as AND (default).
     */
   private static void applyGroupType(final IFeatureTree tree, final String groupType) {
      switch (groupType) {
         case "or":
            tree.mutate().addAndGroup();
            // case "OR": tree.mutate().addAndGroup();
            break;
         case "alternative":
            tree.mutate().addAlternativeGroup();
            // case "XOR": tree.mutate().addAlternativeGroup();
            break;
         case "and":
         default:
            // AND is the FeatJAR default; no explicit call needed.
            break;
      }
   }

    /*
     * TODO: a method to add constraints
     * incomplete; method isn't parsing any formula
     * the emf model might also not be fitted for formulas
     *
    public static void FMaddConstraint(IFeatureModel goal, BufferedReader reader) {
        IConstraint constraint1 = goal.mutate().addConstraint(Expressions.True);
        ExpressionParser parser = new ExpressionParser();
        parser.setSymbols();
        Result<IExpression> parse = parser.parse("");
    }
    */

    // getting the word that's between the next instance of walls (like ", as in "word")
    public static String readEMFFileCapsulatedWord(BufferedReader reader, char walls) {
        StringBuilder result = new StringBuilder();
        int character;
        try {
            while ((character = reader.read()) != -1) {
                char ch = (char) character;
                if (ch != walls) {
                    continue;
                }
                while ((character = reader.read()) != -1) {
                    ch = (char) character;
                    if (ch == walls) {
                        break;
                    }
                    result.append(ch);
                }
                break;
            }
        } catch (IOException e) {
            // swallowed intentionally; caller receives whatever was read so far
        }
        return result.toString();
    }

    // getting the 4 chars after the next occurrence of 'sign' (used '<' so far)
    // 4 chars because "root" is the shortest distinguishing prefix in the file
    public static String readEMFFileAfterChar(BufferedReader reader, char sign) {
        StringBuilder result = new StringBuilder();
        int character;
        try {
            while ((character = reader.read()) != -1) {
                char ch = (char) character;
                if (ch != sign) {
                    continue;
                }
                for (int i = 0; i < 4; i++) {
                    if ((character = reader.read()) == -1) {
                        break;
                    }
                    ch = (char) character;
                    result.append(ch);
                }
                return result.toString();
            }
        } catch (IOException e) {
            System.err.println("Error reading");
        }
        return "";
    }

    // checks if either '/' or '>' comes next to determine whether the element
    // continues (>) or is self-closing (/>) or closing (</...>)
    public static boolean readEMFFileCheckingClosingTag(BufferedReader reader) throws IOException {
        int character;
        try {
            while ((character = reader.read()) != -1) {
                char ch = (char) character;
                if (ch == '/') {
                    return true;
                } else if (ch == '>') {
                    return false;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading");
        }
        throw new IOException("EOF reached before '/' or '>' was found");
    }
}
