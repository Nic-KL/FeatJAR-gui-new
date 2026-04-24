package de.featjar.gui.types;

import org.eclipse.glsp.graph.DefaultTypes;

public enum EdgeType {
	MANDATORY_EDGE("edge-mandatory"),
	OPTIONAL_EDGE("edge-optional"),
	BASIC_EDGE(DefaultTypes.EDGE);
	
	private final String value;
	
	private EdgeType(String value) {
		this.value = value;
	}
	
    public String value() {
        return value;
    }
}
