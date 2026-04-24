package de.featjar.gui.types;

import featJAR.Cardinality;

public enum CardinalityType {
	OPTIONAL_FEATURE("feature-optional"),	
	MANDATORY_FEATURE("feature-manatory"),
	MULTIPLE_FEATURE("feature-multiple");

	private final String value;

    CardinalityType(String value) {
        this.value = value;
    }
    
    public String value() {
        return value;
    }
    
    public static CardinalityType of(Cardinality c) {
        if (c.getLowerBound() == 0 && c.getUpperBound() == 1) return OPTIONAL_FEATURE;
        if (c.getLowerBound() == 1 && c.getUpperBound() == 1) return MANDATORY_FEATURE;
        return MULTIPLE_FEATURE;
    }
    
    public static CardinalityType fromGlspId(String glspId) {
        for (CardinalityType t : values()) {
            if (t.value().equals(glspId)) return t;
        }
        throw new IllegalArgumentException("Unknown FeatureCardinalityType: " + glspId);
    }
}
