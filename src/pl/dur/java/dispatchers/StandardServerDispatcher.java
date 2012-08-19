/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.dispatchers;

import pl.dur.java.actions.TestAction;

/**
 *
 * @author Dur
 */
public class StandardServerDispatcher extends Dispatcher
{

	@Override
	public TestAction dispatch( String toDispatch )
	{
		return new TestAction("webSocket");
	}
	
}
