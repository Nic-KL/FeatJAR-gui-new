package de.featjar.gui.io;

import static org.junit.jupiter.api.Assertions.*;

import de.featjar.base.FeatJAR;
import de.featjar.base.data.identifier.Identifiers;
import de.featjar.base.io.IO;
import de.featjar.feature.model.FeatureModel;
import de.featjar.feature.model.IFeature;
import de.featjar.feature.model.IFeatureModel;
import de.featjar.feature.model.IFeatureTree;
import de.featjar.formula.structure.Expressions;
import java.nio.file.Path;
import java.util.Optional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Round-trip tests for a large model that exercises every group type
 * (AND, OR, XOR, cardinality) and the feature implementation (abstract, concrete),
 * the feature property hidden,
 * the feature type mandatory, optional
 * TODO Missing: Multiple features, and none implementation type (this type is only present in the EMF model )
 */
class EMFFeatureModelLargeModelTest {

    @TempDir
    Path tempDir;

    private IFeatureModel original;
    private IFeatureModel roundTrip;

    @BeforeAll
    static void initFeatJAR() {
        FeatJAR.initialize();
    }

    @BeforeEach
    void buildAndRoundTrip() throws Exception {
        original = buildLargeModel();

        Path emf = tempDir.resolve("large-model.featuremodel");
        //        String xml = new EMFFeatureModelWriter().serialize(original).orElseThrow();
        //        System.out.println(xml);
        IO.save(original, emf, new EMFFeatureModelFormat());
        roundTrip = IO.load(emf, new EMFFeatureModelFormat()).orElseThrow();
    }

    private static IFeatureModel buildLargeModel() {
        IFeatureModel fm = new FeatureModel(Identifiers.newCounterIdentifier());
        fm.mutate().setName("AllGroupsAndCardinalities");
        fm.mutate()
                .setDescription(
                        "Example model with and/or/xor/cardinality, abstract/concrete/hidden, mandatory/optional/multiple");

        IFeature root = fm.mutate().addFeature("Root");
        IFeature core = fm.mutate().addFeature("Core");
        IFeature logging = fm.mutate().addFeature("Logging");
        IFeature internalConfig = fm.mutate().addFeature("InternalConfig");
        IFeature ui = fm.mutate().addFeature("UI");
        IFeature cli = fm.mutate().addFeature("CLI");
        IFeature gui = fm.mutate().addFeature("GUI");
        IFeature storage = fm.mutate().addFeature("Storage");
        IFeature fileSystem = fm.mutate().addFeature("FileSystem");
        IFeature database = fm.mutate().addFeature("Database");
        IFeature cloud = fm.mutate().addFeature("Cloud");
        IFeature plugins = fm.mutate().addFeature("Plugins");
        IFeature authPlugin = fm.mutate().addFeature("AuthPlugin");
        IFeature exportPlugin = fm.mutate().addFeature("ExportPlugin");
        IFeature monitoringPlugin = fm.mutate().addFeature("MonitoringPlugin");
        IFeature backupPlugin = fm.mutate().addFeature("BackupPlugin");
        IFeature worker = fm.mutate().addFeature("Worker");

        IFeatureTree rootTree = fm.mutate().addFeatureTreeRoot(root);

        IFeatureTree coreTree = rootTree.mutate().addFeatureBelow(core);
        IFeatureTree loggingTree = rootTree.mutate().addFeatureBelow(logging);
        IFeatureTree internalConfigTree = rootTree.mutate().addFeatureBelow(internalConfig);
        IFeatureTree uiTree = rootTree.mutate().addFeatureBelow(ui);
        IFeatureTree storageTree = rootTree.mutate().addFeatureBelow(storage);
        IFeatureTree pluginsTree = rootTree.mutate().addFeatureBelow(plugins);
        IFeatureTree workerTree = rootTree.mutate().addFeatureBelow(worker);

        int rootGroup = rootTree.mutate().addAndGroup();
        coreTree.mutate().setParentGroupID(rootGroup);
        loggingTree.mutate().setParentGroupID(rootGroup);
        internalConfigTree.mutate().setParentGroupID(rootGroup);
        uiTree.mutate().setParentGroupID(rootGroup);
        storageTree.mutate().setParentGroupID(rootGroup);
        pluginsTree.mutate().setParentGroupID(rootGroup);
        workerTree.mutate().setParentGroupID(rootGroup);

        coreTree.mutate().makeMandatory();
        loggingTree.mutate().makeOptional();
        internalConfigTree.mutate().makeOptional();

        root.mutate().setAbstract(true);
        internalConfig.mutate().setHidden(true);
        //        internalConfig.mutate().setHidden(true);
        core.mutate().setConcrete();
        logging.mutate().setConcrete();

        IFeatureTree cliTree = uiTree.mutate().addFeatureBelow(cli);
        IFeatureTree guiTree = uiTree.mutate().addFeatureBelow(gui);

        int uiGroup = uiTree.mutate().addAlternativeGroup();
        cliTree.mutate().setParentGroupID(uiGroup);
        guiTree.mutate().setParentGroupID(uiGroup);

        IFeatureTree fileSystemTree = storageTree.mutate().addFeatureBelow(fileSystem);
        IFeatureTree databaseTree = storageTree.mutate().addFeatureBelow(database);
        IFeatureTree cloudTree = storageTree.mutate().addFeatureBelow(cloud);

        int storageGroup = storageTree.mutate().addOrGroup();
        fileSystemTree.mutate().setParentGroupID(storageGroup);
        databaseTree.mutate().setParentGroupID(storageGroup);
        cloudTree.mutate().setParentGroupID(storageGroup);

        IFeatureTree authPluginTree = pluginsTree.mutate().addFeatureBelow(authPlugin);
        IFeatureTree exportPluginTree = pluginsTree.mutate().addFeatureBelow(exportPlugin);
        IFeatureTree monitoringPluginTree = pluginsTree.mutate().addFeatureBelow(monitoringPlugin);
        IFeatureTree backupPluginTree = pluginsTree.mutate().addFeatureBelow(backupPlugin);

        int pluginsGroup = pluginsTree.mutate().addCardinalityGroup(3, 4);
        authPluginTree.mutate().setParentGroupID(pluginsGroup);
        exportPluginTree.mutate().setParentGroupID(pluginsGroup);
        monitoringPluginTree.mutate().setParentGroupID(pluginsGroup);
        backupPluginTree.mutate().setParentGroupID(pluginsGroup);

        IFeature w1 = fm.mutate().addFeature("W1");
        IFeature w2 = fm.mutate().addFeature("W2");
        IFeature w3 = fm.mutate().addFeature("W3");
        IFeature w4 = fm.mutate().addFeature("W4");
        IFeature w5 = fm.mutate().addFeature("W5");

        IFeatureTree w1Tree = workerTree.mutate().addFeatureBelow(w1);
        IFeatureTree w2Tree = workerTree.mutate().addFeatureBelow(w2);
        IFeatureTree w3Tree = workerTree.mutate().addFeatureBelow(w3);
        IFeatureTree w4Tree = workerTree.mutate().addFeatureBelow(w4);
        IFeatureTree w5Tree = workerTree.mutate().addFeatureBelow(w5);

        int workerGroup = workerTree.mutate().addCardinalityGroup(2, 5);
        w1Tree.mutate().setParentGroupID(workerGroup);
        w2Tree.mutate().setParentGroupID(workerGroup);
        w3Tree.mutate().setParentGroupID(workerGroup);
        w4Tree.mutate().setParentGroupID(workerGroup);
        w5Tree.mutate().setParentGroupID(workerGroup);

        fm.mutate().addConstraint(Expressions.implies(Expressions.literal("GUI"), Expressions.literal("Logging")));
        fm.mutate().addConstraint(Expressions.implies(Expressions.literal("Database"), Expressions.literal("Core")));
        fm.mutate().addConstraint(Expressions.True);

        //        System.out.println("Plugins groups:");
        //        for (int i = 0; i < pluginsTree.getChildrenGroups().size(); i++) {
        //            var g = pluginsTree.getChildrenGroups().get(i);
        //            if (g != null) {
        //                System.out.println("  group[" + i + "] = " + g.getLowerBound() + ".." + g.getUpperBound());
        //            }
        //        }
        //        for (IFeatureTree child : pluginsTree.getChildren()) {
        //            System.out.println("  child " + child.getFeature().getName().orElse("?")
        //                + " parentGroupID=" + child.getParentGroupID());
        //        }

        return fm;
    }

    private long countFeatures(IFeatureModel model) {
        return model.getRoots().get(0).preOrderStream().count();
    }

    private Optional<IFeatureTree> findFeature(IFeatureModel model, String name) {
        return model.getRoots()
                .get(0)
                .preOrderStream()
                .map(node -> (IFeatureTree) node)
                .filter(node -> name.equals(node.getFeature().getName().orElse(null)))
                .findFirst();
    }

    @Test
    void roundTripPreservesFeatureCount() {
        assertEquals(
                countFeatures(original),
                countFeatures(roundTrip),
                "Feature count must stay the same after the round trip");
    }

    @Test
    void roundTripPreservesConstraintCount() {
        assertEquals(
                original.getConstraints().size(),
                roundTrip.getConstraints().size(),
                "Constraint count must stay the same after the round trip");
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "Root",
                "Core",
                "Logging",
                "InternalConfig",
                "UI",
                "CLI",
                "GUI",
                "Storage",
                "FileSystem",
                "Database",
                "Cloud",
                "Plugins",
                "AuthPlugin",
                "ExportPlugin",
                "MonitoringPlugin",
                "BackupPlugin",
                "Worker",
                "W1",
                "W2",
                "W3",
                "W4",
                "W5"
            })
    void allFeaturesPresentAfterRoundTrip(String featureName) {
        assertTrue(
                findFeature(roundTrip, featureName).isPresent(),
                "Feature '" + featureName + "' must exist after the round trip");
    }

    @ParameterizedTest
    @CsvSource({"Root, true", "Core, false", "Logging, false"})
    void abstractFlagPreserved(String featureName, boolean expectedAbstract) {
        IFeatureTree node = findFeature(roundTrip, featureName).orElseThrow();
        assertEquals(
                expectedAbstract,
                node.getFeature().isAbstract(),
                "Abstract flag of '" + featureName + "' must be preserved");
    }

    @Test
    void hiddenFlagPreserved() {
        IFeatureTree node = findFeature(roundTrip, "InternalConfig").orElseThrow();
        assertTrue(node.getFeature().isHidden(), "InternalConfig must stay hidden after the round trip");
    }

    @ParameterizedTest
    @CsvSource({"Core, true", "Logging, false", "InternalConfig, false"})
    void mandatoryFlagPreserved(String featureName, boolean expectedMandatory) {
        IFeatureTree node = findFeature(roundTrip, featureName).orElseThrow();
        assertEquals(
                expectedMandatory, node.isMandatory(), "Mandatory flag of '" + featureName + "' must be preserved");
    }

    @ParameterizedTest
    @CsvSource({"UI, 2", "Storage, 3", "Plugins, 4", "Worker, 5"})
    void groupChildCountPreserved(String parentName, int expectedChildren) {
        IFeatureTree node = findFeature(roundTrip, parentName).orElseThrow();
        assertEquals(
                expectedChildren, node.getChildren().size(), "Child count of '" + parentName + "' must be preserved");
    }

    @ParameterizedTest
    @CsvSource({"Plugins, 3, 4", "Worker, 2, 5"})
    void cardinalityGroupBoundsPreserved(String parentName, int lower, int upper) {
        IFeatureTree node = findFeature(roundTrip, parentName).orElseThrow();
        node.getChildren()
                .get(0)
                .getParentGroup()
                .ifPresentOrElse(
                        group -> {
                            assertEquals(
                                    lower,
                                    group.getLowerBound(),
                                    "Lower bound of group '" + parentName + "' must be preserved");
                            assertEquals(
                                    upper,
                                    group.getUpperBound(),
                                    "Upper bound of group '" + parentName + "' must be preserved");
                        },
                        () -> fail("Group of '" + parentName + "' was not found after the round trip"));
    }
}
