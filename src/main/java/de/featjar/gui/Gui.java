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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import de.featjar.base.FeatJAR;
import de.featjar.base.data.Problem;
import de.featjar.base.data.Problem.Severity;
import de.featjar.base.data.Result;
import de.featjar.base.data.identifier.Identifiers;
import de.featjar.base.io.IO;
import de.featjar.base.tree.Trees;
import de.featjar.base.tree.visitor.TreePrinter;
import de.featjar.feature.model.FeatureModel;
import de.featjar.feature.model.IConstraint;
import de.featjar.feature.model.IFeature;
import de.featjar.feature.model.IFeatureModel;
import de.featjar.feature.model.IFeatureTree;
import de.featjar.feature.model.io.tikz.TikzFeatureModelSerializer;
import de.featjar.feature.model.io.uvl.UVLFeatureModelFormat;
import de.featjar.formula.structure.Expressions;

public class Gui {
	
	private static final int MAX_LOG_FILE_SIZE = 1024 * 50; // 50 KiB
	private static final String LOG_FILE_NAME = "server_logs.log";
	private static final Path LOG_FILE_PATH = Path.of("logs" + File.separator + LOG_FILE_NAME);
	private static final Path CLIENT_HTML_PATH = Path.of(
			".." + File.separator + "FeatJAR-gui-client" + File.separator + "app", "diagram.html"
			);
	
	public static void main(String[] agrs) {
    	FeatJAR.initialize();
    	
//        IFeatureModel ABCnAnBnCUVL = IO.load(
//                Paths.get("../formula/src/testFixtures/resources/formats/uvl/ABC-nAnBnC_01.uvl"),
//                new UVLFeatureModelFormat())
//        .orElseThrow();
//        
//        StringBuilder sb = new StringBuilder();
//       
//        TikzFeatureModelSerializer fms = new TikzFeatureModelSerializer();
//        
//        String a = fms.serialize(ABCnAnBnCUVL);
        
//        System.out.println(a);
    	
    	IFeatureModel fm = new FeatureModel(Identifiers.newCounterIdentifier());

        fm.mutate().setName("AllGroupsAndCardinalities");
        fm.mutate().setDescription("Example model with and/or/xor/cardinality, abstract/concrete/hidden, mandatory/optional/multiple");

        // Features anlegen
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

        // Baum aufbauen
        IFeatureTree rootTree = fm.mutate().addFeatureTreeRoot(root);

        IFeatureTree coreTree = rootTree.mutate().addFeatureBelow(core);
        IFeatureTree loggingTree = rootTree.mutate().addFeatureBelow(logging);
        IFeatureTree internalConfigTree = rootTree.mutate().addFeatureBelow(internalConfig);

        IFeatureTree uiTree = rootTree.mutate().addFeatureBelow(ui);
        uiTree.mutate().addFeatureBelow(cli);
        uiTree.mutate().addFeatureBelow(gui);

        IFeatureTree storageTree = rootTree.mutate().addFeatureBelow(storage);
        storageTree.mutate().addFeatureBelow(fileSystem);
        storageTree.mutate().addFeatureBelow(database);
        storageTree.mutate().addFeatureBelow(cloud);

        IFeatureTree pluginsTree = rootTree.mutate().addFeatureBelow(plugins);
        pluginsTree.mutate().addFeatureBelow(authPlugin);
        pluginsTree.mutate().addFeatureBelow(exportPlugin);
        pluginsTree.mutate().addFeatureBelow(monitoringPlugin);
        pluginsTree.mutate().addFeatureBelow(backupPlugin);

        IFeatureTree workerTree = rootTree.mutate().addFeatureBelow(worker);

        // Root als AND-Gruppe
        rootTree.mutate().addAndGroup();

        // Mandatory / Optional am AND-Knoten
        coreTree.mutate().makeMandatory();
        loggingTree.mutate().makeOptional();
        internalConfigTree.mutate().makeOptional();

        // hidden / abstract / concrete
        root.mutate().setAbstract(true);
        internalConfig.mutate().setHidden(true);
        core.mutate().setConcrete();
        logging.mutate().setConcrete();

        // XOR-Gruppe für UI
        uiTree.mutate().addAlternativeGroup();
        // Kinder in alt/xor sind typischerweise unter der Gruppe selbst geregelt

        // OR-Gruppe für Storage
        storageTree.mutate().addOrGroup();

        // Cardinality-Gruppe für Plugins: [1..3]
        pluginsTree.mutate().addCardinalityGroup(3, 4);

        IFeature w1 = fm.mutate().addFeature("W1");
        IFeature w2 = fm.mutate().addFeature("W2");
        IFeature w3 = fm.mutate().addFeature("W3");
        IFeature w4 = fm.mutate().addFeature("W4");
        IFeature w5 = fm.mutate().addFeature("W5");

        workerTree.mutate().addFeatureBelow(w1);
        workerTree.mutate().addFeatureBelow(w2);
        workerTree.mutate().addFeatureBelow(w3);
        workerTree.mutate().addFeatureBelow(w4);
        workerTree.mutate().addFeatureBelow(w5);

        workerTree.mutate().addCardinalityGroup(2, 5);

        // Beispiel-Constraints
        IConstraint c1 = fm.mutate().addConstraint(
            Expressions.implies(
                Expressions.literal("GUI"),
                Expressions.literal("Logging")
            )
        );

        IConstraint c2 = fm.mutate().addConstraint(
            Expressions.implies(
                Expressions.literal("Database"),
                Expressions.literal("Core")
            )
        );

        IConstraint c3 = fm.mutate().addConstraint(Expressions.True);
        
        FeatJAR.log()
        .message("\n"
                + Trees.traverse(fm.getRoots().get(0), new TreePrinter())
                        .get());
        
      TikzFeatureModelSerializer fms = new TikzFeatureModelSerializer();
      
      String a = fms.serialize(fm);
      
      System.out.println(a);
    	
	}

    public static void mainoff(String[] args) {
    	// TODO remove when completely connected to FeatJAR and FeatJAR Shell !
    	FeatJAR.initialize();
    	
    	try (Scanner scanner = new Scanner(System.in)) {
			try {
				Process backroundServer = launchBackroundServer(args);
				FeatJAR.log().message("Server is runnig");
				
			    while (true) {
			        System.out.print("$ ");
			        if(scanner.hasNextLine()) {
			        	String input = scanner.nextLine().trim().toLowerCase();
			        	if(Objects.equals(input, "display")) {
			        		FeatJAR.log().message(CLIENT_HTML_PATH.toAbsolutePath().toUri());
			        	}
			        	
			        	if(Objects.equals(input, "exit")) {
			        		FeatJAR.log().message("Server is shutting down.....");
			        		backroundServer.destroy();
			        		System.exit(0);
			        	}
			        }
			    }
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
   }
    
   private static Result<File> createLogFile() throws IOException {
	   
	   if(Files.exists(LOG_FILE_PATH)) {
		   if(Files.size(LOG_FILE_PATH) > MAX_LOG_FILE_SIZE) {
			   Files.delete(LOG_FILE_PATH);			   
			   return Result.of(Files.createFile(LOG_FILE_PATH).toFile());
		   }
	   } else {
		   return Result.of(Files.createFile(LOG_FILE_PATH).toFile());
	   }
	return Result.of(LOG_FILE_PATH.toFile());
   }
   
   private static Process launchBackroundServer(String[] args) throws IOException {
       String javaHome = System.getProperty("java.home");
       String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
       String classpath = System.getProperty("java.class.path");
       String className = "de.featjar.gui.launch.FeatureModelServerLauncher";
       List<String> command = new ArrayList<>();
     
       command.add(javaBin);
       command.add("-cp");
       command.add(classpath);
       command.add(className);
       
       for (String arg :args) {
    	   command.add(arg);
       }
       
       ProcessBuilder pb = new ProcessBuilder(command);

       File log = createLogFile().orElse(
    		   Files.exists(Path.of("fallback_logger.log")) ? 
    				   Path.of("fallback_logger.log").toFile() : 
    					   Files.createFile(Path.of("fallback_logger.log")).toFile()
       );

       pb.redirectOutput(ProcessBuilder.Redirect.appendTo(log));
       pb.redirectError(ProcessBuilder.Redirect.INHERIT);
//       pb.redirectError(ProcessBuilder.Redirect.appendTo(log));

       return pb.start();
   }
   
   private Problem addProblem(Severity severity, String message, Object... arguments) {
       return new Problem(String.format(message, arguments), severity);
   }
    
    
    @SuppressWarnings("unused")
	private void off() {
    	   FeatJAR.initialize();

           IFeatureModel featureModel = new FeatureModel(Identifiers.newCounterIdentifier());
           featureModel.mutate().setName("My Model");
           featureModel.mutate().setDescription("awesome description");
           IFeature rootFeature = featureModel.mutate().addFeature("root");
           IFeature childFeature = featureModel.mutate().addFeature("child1");
           IFeature childFeature2 = featureModel.mutate().addFeature("child2");
           IFeature childFeature3 = featureModel.mutate().addFeature("child3");
           IFeature childFeature4 = featureModel.mutate().addFeature("child4");   
           IFeature childFeature5 = featureModel.mutate().addFeature("child5");
           IFeature childFeature6 = featureModel.mutate().addFeature("child6");

           IFeatureTree rootTree = featureModel.mutate().addFeatureTreeRoot(rootFeature);
           IFeatureTree childTree = rootTree.mutate().addFeatureBelow(childFeature);
           
           rootTree.mutate().addFeatureBelow(childFeature2);
           rootTree.mutate().addFeatureBelow(childFeature3);
           rootTree.mutate().addFeatureBelow(childFeature4);
           childTree.mutate().addFeatureBelow(childFeature5);
           childTree.mutate().addFeatureBelow(childFeature6);

           IConstraint constraint1 = featureModel.mutate().addConstraint(Expressions.True);
           IConstraint constraint2 = featureModel.mutate().addConstraint(Expressions.True);
           IConstraint constraint3 = featureModel.mutate().addConstraint(Expressions.False);
           
           FeatJAR.log().message("\nSelf Build::" + Trees.traverse(rootTree, new TreePrinter()).get());
           
//           IO.load(Paths.get(""), new FeatureModelFormats()).orElseThrow();
           
           IFeatureModel exampleUVL = IO.load(
                   	Paths.get("./src/main/java/de/featjar/gui/EMFxmls/example2.uvl"),
                   	new UVLFeatureModelFormat())
           		.orElseThrow();
           
           IFeatureModel ABCnAnBnCUVL = IO.load(
                           Paths.get("../formula/src/testFixtures/resources/formats/uvl/ABC-nAnBnC_01.uvl"),
                           new UVLFeatureModelFormat())
                   .orElseThrow();
           
   	    IFeatureModel nAUVL = IO.load(
   	    		  		Paths.get("../formula/src/testFixtures/resources/formats/uvl/nA_01.uvl"),
   	    		  		new UVLFeatureModelFormat())
   	    		.orElseThrow();
   	    
           FeatJAR.log()
                   .message("\nExample:"
                           + Trees.traverse(exampleUVL.getRoots().get(0), new TreePrinter())
                                   .get());
          
//           TranslatorToEMF.writeToEMF(featureModel);
//           TranslatorToEMF.writeToEMF(exampleUVL);
           TranslatorToEMF.writeToEMF(ABCnAnBnCUVL);
//           TranslatorToEMF.writeToEMF(nAUVL);
   	    
           IFeatureModel myChild = TranslatorFromEMF.parseFromEMF("./src/main/java/de/featjar/gui/EMFxmls/My Model.featuremodel");
           
           if (myChild == null) {
           	System.err.println("Failed to create FeatJAT-Model from EMF");
           	return;
           }
           
           IFeatureTree newRoot = myChild.getRoots().get(0);
           FeatJAR.log().message("\nEMF-Loaded:" + Trees.traverse(newRoot, new TreePrinter()).get());
     
    }
}
