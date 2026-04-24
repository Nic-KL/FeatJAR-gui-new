package de.featjar.gui.handler.utils;

import java.util.Optional;

import de.featjar.gui.types.AttributeTypes;
import de.featjar.gui.types.FeatureType;
import featJAR.Attributes;
import featJAR.FeatJARFactory;
import featJAR.Feature;
import featJAR.Identifiable;

public interface IAttributesUtils {
	
	private void setAttribute(Identifiable element, String key, String value) {
		    Optional<Attributes> existing = element.getAttributes().stream()
		        .filter(a -> key.equals(a.getKey()))
		        .findFirst();

		    if (existing.isPresent()) {
		        existing.get().setValue(value);
		    } else {
		        Attributes attr = FeatJARFactory.eINSTANCE.createAttributes();
		        attr.setKey(key);
		        attr.setValue(value);
		        element.getAttributes().add(attr);
		    }
		}
	   
	private Optional<Attributes> getAttribute(Identifiable element, String key) {
		    Optional<Attributes> attribute = element.getAttributes().stream()
		        .filter(a -> key.equals(a.getKey()))
		        .findFirst();
		    return attribute;
		}
	   
   default void setFeatureType(Feature feature, FeatureType featureType) {
		   setAttribute(feature, AttributeTypes.TYPE, featureType.name());
	   }
	   
   default FeatureType getFeatureType(Feature feature) {   
	   Optional<Attributes> a = getAttribute(feature, AttributeTypes.TYPE);
	   return a.isPresent() ? FeatureType.valueOf(a.get().getValue()) : FeatureType.NONE;
	   }
   
   default void setHidden(Feature feature) {
	   setAttribute(feature, AttributeTypes.HIDDEN, "");
   }
   
   default boolean isHidden(Feature feature) {
	   return !getAttribute(feature, AttributeTypes.HIDDEN).isEmpty();
   }
}
