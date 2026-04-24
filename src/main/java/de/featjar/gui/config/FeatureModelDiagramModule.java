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
package de.featjar.gui.config;

import org.eclipse.glsp.server.actions.ActionHandler;
import org.eclipse.glsp.server.di.MultiBinding;
import org.eclipse.glsp.server.diagram.DiagramConfiguration;
import org.eclipse.glsp.server.emf.EMFIdGenerator;
import org.eclipse.glsp.server.emf.EMFSourceModelStorage;
import org.eclipse.glsp.server.emf.notation.EMFNotationDiagramModule;
import org.eclipse.glsp.server.features.core.model.GModelFactory;
import org.eclipse.glsp.server.features.directediting.LabelEditValidator;
import org.eclipse.glsp.server.features.toolpalette.ToolPaletteItemProvider;
import org.eclipse.glsp.server.operations.OperationHandler;

import de.featjar.gui.handler.SelectionActionHandler;
import de.featjar.gui.handler.create.CreateConstraintOperationHandler;
import de.featjar.gui.handler.create.PasteOperationHandler;
import de.featjar.gui.handler.create.feature.CreateMandatoryFeatureNodeHandler;
import de.featjar.gui.handler.create.feature.CreateMultipleFeatureNodeHandler;
import de.featjar.gui.handler.create.feature.CreateOptionalFeatureNodeHandler;
import de.featjar.gui.handler.create.group.CreateAndGroupNodeHandler;
import de.featjar.gui.handler.create.group.CreateCardinalityGroupNodeHandler;
import de.featjar.gui.handler.create.group.CreateOrGroupNodeHandler;
import de.featjar.gui.handler.create.group.CreateXorGroupNodeHandler;
import de.featjar.gui.handler.modify.DeleteFeatureNodeHandler;
import de.featjar.gui.handler.modify.FeatureModelLabelEditValidator;
import de.featjar.gui.handler.modify.FeatureNodeLabelEditHandler;
import de.featjar.gui.handler.modify.FeatureNodeLabelEditOperationHandler;
import de.featjar.gui.id.FeatureModelIdGenerator;
import de.featjar.gui.model.FeatureModelGModelFactory;
import de.featjar.gui.model.FeatureModelSourceModelStorage;
import de.featjar.gui.palette.FeatureModelToolPaletteItemProvider;

public class FeatureModelDiagramModule extends EMFNotationDiagramModule {

   @Override
   protected Class<? extends DiagramConfiguration> bindDiagramConfiguration() {
      // define what operations are allowed with our elements
      return FeatureModelDiagramConfiguration.class;
   }

   // Experimentell
   // @Override
   // protected Class<? extends PopupModelFactory> bindPopupModelFactory() {
   // return FeaturesPopupModelFactory.class;
   // }

   @Override
   protected Class<? extends EMFSourceModelStorage> bindSourceModelStorage() {
      // ensure our custom package is registered when loading our models
      return FeatureModelSourceModelStorage.class;
   }

   @Override
   public Class<? extends GModelFactory> bindGModelFactory() {
      // custom factory to convert tasks into nodes
      return FeatureModelGModelFactory.class;
   }

   // WRONG we need a custom generator which sets IDs when there is no ID present !
   // @Override
   // protected Class<? extends EMFIdGenerator> bindEMFIdGenerator() {
   // // all our elements inherit from Identifiable and have an ID attribute set
   // return AttributeIdGenerator.class;
   // }

   @Override
   protected Class<? extends EMFIdGenerator> bindEMFIdGenerator() {
      // all our elements inherit from Identifiable and have an ID attribute set
      return FeatureModelIdGenerator.class;
   }

   @Override
   protected Class<? extends ToolPaletteItemProvider> bindToolPaletteItemProvider() {
      return FeatureModelToolPaletteItemProvider.class;
   }

   @Override
   protected Class<? extends LabelEditValidator> bindLabelEditValidator() {
      return FeatureModelLabelEditValidator.class;
   }

   @Override
   protected void configureActionHandlers(final MultiBinding<ActionHandler> bindings) {
      super.configureActionHandlers(bindings);
      bindings.add(SelectionActionHandler.class);
      bindings.add(FeatureNodeLabelEditHandler.class);

   }

   @Override
   protected void configureOperationHandlers(final MultiBinding<OperationHandler<?>> binding) {
      super.configureOperationHandlers(binding);
      binding.add(CreateMandatoryFeatureNodeHandler.class);
      binding.add(CreateOptionalFeatureNodeHandler.class);
      binding.add(CreateMultipleFeatureNodeHandler.class);

      binding.add(CreateOrGroupNodeHandler.class);
      binding.add(CreateXorGroupNodeHandler.class);
      binding.add(CreateAndGroupNodeHandler.class);
      binding.add(CreateCardinalityGroupNodeHandler.class);

      binding.add(CreateConstraintOperationHandler.class);
      binding.add(DeleteFeatureNodeHandler.class);
      binding.add(PasteOperationHandler.class);
      binding.add(FeatureNodeLabelEditOperationHandler.class);

   }

   @Override
   public String getDiagramType() { return "featuremodel-diagram"; }

}
