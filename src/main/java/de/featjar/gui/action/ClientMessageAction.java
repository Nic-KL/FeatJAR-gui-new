package de.featjar.gui.action;

import org.eclipse.glsp.server.actions.Action;

public class ClientMessageAction extends Action {
	
	public static final String KIND = "clientMessage";
	
	private String message;	
	
	public ClientMessageAction() {
		super(KIND);
	}
	
    public ClientMessageAction(final String message) {
        super(KIND);
        this.message = message;
    }
	
	public String getClientMessage() { return message; }
	
	public void setClientMessage(String message) {
		this.message = message;
	}
}
