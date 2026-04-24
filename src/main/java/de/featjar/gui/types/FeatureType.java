package de.featjar.gui.types;

public enum FeatureType {	
	NONE("feature-none"),
	ABSTRACT("feature-abstract"),
	CONCRETE("feature-concrete");
	
	private final String value;
	
	private FeatureType(String value) {
		this.value = value;
	}
	
    public String value() {
        return value;
    }
}
