package de.featjar.gui.types;

import de.featjar.gui.handler.utils.ICardinialityUtils;
import featJAR.Cardinality;

public enum NodeType implements ICardinialityUtils{
	OR_NODE ("node-or"),
   	XOR_NODE ("node-xor"),
   	AND_NODE ("node-and"),
   	CARDINALITY_NODE ("node-cardinality");
   
	private final String value;
	
	private NodeType(String value) {
		this.value = value;
	}
	
    public String value() {
        return value;
    }
    
    public static NodeType of(Cardinality c) {
        int lower = c.getLowerBound();
        int upper = c.getUpperBound();
        if (lower == 1 && upper == -1) return OR_NODE;
        if (lower == 1 && upper == 1)  return XOR_NODE;
        if (lower == 0 && upper == -1) return AND_NODE;
        return CARDINALITY_NODE;
    }
    
    public static NodeType fromGlspId(String glspId) {
        for (NodeType t : values()) {
            if (t.value().equals(glspId)) return t;
        }
        throw new IllegalArgumentException("Unknown NodeType: " + glspId);
    }
}
