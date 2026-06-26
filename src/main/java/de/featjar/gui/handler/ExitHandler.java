package de.featjar.gui.handler;

import java.util.List;

import org.eclipse.glsp.server.actions.AbstractActionHandler;
import org.eclipse.glsp.server.actions.Action;

import de.featjar.base.FeatJAR;
import de.featjar.gui.action.ClientMessageAction;

public class ExitHandler extends AbstractActionHandler<ClientMessageAction> {

    @Override
    protected List<Action> executeAction(final ClientMessageAction action) {
    	
        return none();  
    }
}

