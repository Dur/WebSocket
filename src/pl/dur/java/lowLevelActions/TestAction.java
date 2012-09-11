/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.lowLevelActions;

import java.util.HashMap;
import pl.dur.java.actions.Action;

/**
 *
 * @author Dur
 */
public class TestAction extends Action
{

	@Override
	public void execute( HashMap<String, Object> params )
	{
		System.out.println("Executed low level  TEST action");
	}
	
}
