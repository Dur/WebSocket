/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.client;

import java.io.ObjectInputStream;
import java.net.Socket;
import pl.dur.java.dispatchers.Dispatcher;
import pl.dur.java.events.mappers.StandardClientEventMapper;
import pl.dur.java.messages.Message;

/**
 *
 * @author Dur
 */
public class ResponseExecutor implements Runnable
{
	public Socket socketToListen;
	public ObjectInputStream in = null;
	Dispatcher dispatcher = new Dispatcher( new StandardClientEventMapper() );

	@Override
	public synchronized void run()
	{
		while( true )
		{
			try
			{
				System.out.println("Response executor waiting for action");
				this.wait();
				System.out.println("got action, executing...");
				in = new ObjectInputStream( socketToListen.getInputStream() );
				Message response = (Message) in.readObject();
				//dorobic sekcje krytyczna
				dispatcher.dispatch( response, socketToListen );
				System.out.println("after dispatch");
			}
			catch( Exception ex )
			{
				ex.printStackTrace();
			}
		}

	}

	public synchronized void waitForAnswer( Socket socket )
	{
		System.out.println("wait for answer method");
		this.socketToListen = socket;
		this.notify();
	}
}
