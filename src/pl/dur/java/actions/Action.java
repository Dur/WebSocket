/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.actions;

import java.net.Socket;

/**
 *
 * @author Dur
 */
public abstract class Action
{

	public Action(  )
	{
	}

	public abstract void execute(Object param, Socket toResponse);
}
