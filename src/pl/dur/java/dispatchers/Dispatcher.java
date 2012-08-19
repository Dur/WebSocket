/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.dispatchers;

import pl.dur.java.actions.Action;
import pl.dur.java.actions.TestAction;

/**
 *
 * @author Dur
 */
public abstract class Dispatcher
{
	public abstract Action dispatch(String toDispatch);
}
