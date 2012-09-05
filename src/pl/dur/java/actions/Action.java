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
public interface Action
{

	public abstract void execute(Object param, SocketAdmin administrator);
}
