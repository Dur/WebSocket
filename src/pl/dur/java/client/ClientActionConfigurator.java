/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.client;

import java.util.HashMap;
import pl.dur.java.actions.Action;
import pl.dur.java.actions.EchoAction;
import pl.dur.java.actions.NewPortAction;
import pl.dur.java.actions.SendBackAction;

/**
 *
 * @author Dur
 */
public final class ClientActionConfigurator
{
	private HashMap<String, Action> configurator = new HashMap<String, Action>();
	
	public ClientActionConfigurator()
	{
		configurator.put("SEND_BACK", new SendBackAction());
		configurator.put("NP", new NewPortAction());
		configurator.put("ECHO", new EchoAction());
	}

	public final HashMap<String, Action> getConfigurator()
	{
		return configurator;
	}

	
	
}
