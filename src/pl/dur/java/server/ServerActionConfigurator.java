/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.server;

import java.util.HashMap;
import pl.dur.java.actions.Action;
import pl.dur.java.actions.KillAction;

/**
 *
 * @author Dur
 */
public class ServerActionConfigurator
{
	private HashMap<String, Action> configurator;
	
	public ServerActionConfigurator()
	{
		configurator = new HashMap<String, Action>();
		configurator.put("KILL", new KillAction());
	}

	public final HashMap<String, Action> getConfigurator()
	{
		return configurator;
	}
}