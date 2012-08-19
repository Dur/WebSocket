/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.actions;

/**
 *
 * @author Dur
 */
public abstract class Action
{
	private Object params;

	public Action( Object params )
	{
		this.params = params;
	}

	public abstract void execute();
}
