/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.actions;

import java.net.Socket;
import pl.dur.java.messages.Message;
import pl.dur.java.socketAdmins.SocketAdmin;

/**
 *
 * @author Dur
 */
public abstract class Action
{
	public void execute(Object param)
	{
		throw new UnsupportedOperationException( "Must be implementet by extensive class");
	}
}
