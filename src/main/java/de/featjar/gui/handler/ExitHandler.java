package de.featjar.gui.handler;

import java.util.List;

import org.eclipse.glsp.server.actions.AbstractActionHandler;
import org.eclipse.glsp.server.actions.Action;

import de.featjar.base.FeatJAR;
import de.featjar.gui.action.ClientMessageAction;
import de.featjar.gui.action.ExitAction;
import de.featjar.gui.launch.FeatureModelWebsocketLauncher;

public class ExitHandler extends AbstractActionHandler<ExitAction> {

    @Override
    protected List<Action> executeAction(final ExitAction action) {
    	FeatJAR.log().error("Server Exit Handler called ... shutting down ");
    	FeatureModelWebsocketLauncher.out.println(FeatureModelWebsocketLauncher.SIGNAL_STOP);
        return none();  
    }
}

