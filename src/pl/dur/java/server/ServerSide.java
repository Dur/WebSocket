/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.server;

/**
 *
 * @author Dur
 */
public class ServerSide
{
	ConnectionReceiver connectionReceiver;

	public ServerSide()
	{
		connectionReceiver = new ConnectionReceiver();
		Thread connectionReceiverThread = new Thread( connectionReceiver );
		connectionReceiverThread.start();
	}

}
