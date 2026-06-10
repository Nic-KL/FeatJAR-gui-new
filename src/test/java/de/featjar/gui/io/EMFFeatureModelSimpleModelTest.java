package de.featjar.gui.io;

import static org.junit.jupiter.api.Assertions.*;

import de.featjar.base.FeatJAR;
import de.featjar.base.io.IO;
import de.featjar.feature.model.IFeatureModel;
import de.featjar.feature.model.IFeatureTree;
import de.featjar.feature.model.io.uvl.UVLFeatureModelFormat;
import java.nio.file.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class EMFFeatureModelSimpleModelTest {

    @TempDir
    Path tempDir;

    @BeforeAll
    static void setUp() {
        FeatJAR.initialize();
    }

    private IFeatureModel loadUvl(String name) {
        return IO.load(Path.of("src/test/resources/uvl", name), new UVLFeatureModelFormat())
                .orElseThrow();
    }

    @ParameterizedTest
    @ValueSource(strings = {"nAB_01.uvl", "ABC-nAnBnC_01.uvl", "nA_03.uvl"})
    void parsesAbstractCorrectly(String filename) throws Exception {
        IFeatureModel original = loadUvl(filename);

        Path emf = tempDir.resolve("model.featuremodel");
        IO.save(original, emf, new EMFFeatureModelFormat());
        IFeatureModel parsed = IO.load(emf, new EMFFeatureModelFormat()).orElseThrow();

        IFeatureTree root = parsed.getRoots().get(0);
        assertTrue(root.getFeature().isAbstract(), "Root shall be abstract");

        root.getChildren()
                .forEach(child -> assertFalse(
                        child.getFeature().isAbstract(),
                        child.getFeature().getName().orElse("?") + " must be concrete"));
    }

    @ParameterizedTest
    @CsvSource({"nAB_01.uvl, 2", "ABC-nAnBnC_01.uvl, 3", "nA_03.uvl, 1"})
    void preservesTreeStructure(String filename, int expected) throws Exception {
        IFeatureModel original = loadUvl(filename);

        Path emf = tempDir.resolve("model.featuremodel");
        IO.save(original, emf, new EMFFeatureModelFormat());
        IFeatureModel parsed = IO.load(emf, new EMFFeatureModelFormat()).orElseThrow();

        IFeatureTree root = parsed.getRoots().get(0);
        assertEquals("Root", root.getFeature().getName().orElseThrow());
        assertEquals(expected, root.getChildren().size());

        var childNames = root.getChildren().stream()
                .map(c -> c.getFeature().getName().orElse("?"))
                .toList();
        assertTrue(childNames.contains("A"));
        if (expected > 1) assertTrue(childNames.contains("B"));
        if (expected > 2) assertTrue(childNames.contains("C"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"nAB_01.uvl", "ABC-nAnBnC_01.uvl", "nA_03.uvl"})
    void roundTripPreservesFeatureCount(String filename) throws Exception {
        IFeatureModel original = loadUvl(filename);

        Path emf = tempDir.resolve("model.featuremodel");
        Path uvlOut = tempDir.resolve("out.uvl");

        IO.save(original, emf, new EMFFeatureModelFormat());
        IFeatureModel parsed = IO.load(emf, new EMFFeatureModelFormat()).orElseThrow();
        IO.save(parsed, uvlOut, new UVLFeatureModelFormat());

        assertEquals(
                countFeatures(original.getRoots().get(0)),
                countFeatures(parsed.getRoots().get(0)),
                "Different quantities of features detected");
    }

    private long countFeatures(IFeatureTree tree) {
        return tree.preOrderStream().count();
    }

    @ParameterizedTest
    @ValueSource(strings = {"nAB_01.uvl", "ABC-nAnBnC_01.uvl", "nA_03.uvl"})
    void parsesConstraint(String filename) throws Exception {
        IFeatureModel original = loadUvl(filename);

        Path emf = tempDir.resolve("model.featuremodel");
        IO.save(original, emf, new EMFFeatureModelFormat());
        IFeatureModel parsed = IO.load(emf, new EMFFeatureModelFormat()).orElseThrow();

        assertEquals(1, parsed.getConstraints().size(), "Expected exactly one constraint");
    }
}
