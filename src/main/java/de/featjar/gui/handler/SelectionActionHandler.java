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
package de.featjar.gui.handler;

import com.google.inject.Inject;
import de.featjar.gui.handler.utils.HandlerUtils;
import de.featjar.gui.model.FeatureModelGModelFactory;
import featJAR.Identifiable;
import java.util.List;
import java.util.NoSuchElementException;
import org.eclipse.glsp.server.actions.Action;
import org.eclipse.glsp.server.actions.ActionHandler;
import org.eclipse.glsp.server.actions.SelectAction;
import org.eclipse.glsp.server.emf.EMFIdGenerator;
import org.eclipse.glsp.server.emf.notation.EMFNotationModelState;

public class SelectionActionHandler implements ActionHandler {

    @Inject
    protected EMFNotationModelState modelState;

    @Inject
    protected EMFIdGenerator idGenerator;

    @Override
    public boolean handles(final Action action) {
        return action instanceof SelectAction;
    }

    @Override
    public List<Class<? extends Action>> getHandledActionTypes() {
        return List.of(SelectAction.class);
    }

    @Override
    public List<Action> execute(final Action action) {

        SelectAction selectAction = (SelectAction) action;

        // Get the selected element IDs from the action
        List<String> selectedIds = selectAction.getSelectedElementsIDs();

        if (selectedIds.size() > 0) {
            // TODO DOES NOT WORK !
            Identifiable element = HandlerUtils.findIdentifiableById(
                            FeatureModelGModelFactory.featureGroupIdMap.get(selectedIds.get(0)))
                    .orElseThrow(NoSuchElementException::new);
            modelState.setProperty("currentSelection", element);
        }

        return List.of();
    }
}
