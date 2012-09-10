/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.lowLevelActions;

import pl.dur.java.actions.Action;
import pl.dur.java.socketAdmins.SocketAdmin;

/**
 *
 * @author Dur
 */
public class TestAction extends Action
{

	@Override
	public void execute( Object param )
	{
		System.out.println("Executed low level  TEST action");
	}
	
}
