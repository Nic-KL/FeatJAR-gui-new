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
/**
 *
 */
package de.featjar.gui.id;

import java.util.UUID;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.glsp.server.emf.EMFIdGenerator;

/**
 * @author
 *
 */
public class FeatureModelIdGenerator implements EMFIdGenerator {

    @Override
    public String getOrCreateId(final EObject element) {
        String id = EcoreUtil.getID(element);

        if (element.eClass().getEIDAttribute() == null) {
            return EcoreUtil.getURI(element).fragment();
        }

        if (id == null || id.isBlank()) {
            id = UUID.randomUUID().toString().replace("-", "");
            EcoreUtil.setID(element, id);
        }
        return id;
    }
}
