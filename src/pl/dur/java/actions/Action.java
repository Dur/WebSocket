/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.actions;

import java.util.HashMap;

/**
 *
 * @author Dur
 */
public abstract class Action
{
	public void execute(HashMap<String, Object> params)
	{
		throw new UnsupportedOperationException( "Must be implementet by extensive class");
	}
}
