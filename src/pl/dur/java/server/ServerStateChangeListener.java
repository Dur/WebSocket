/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Dur
 */
public class ServerStateChangeListener implements Runnable
{
	Socket clientSocket = null;
	PrintWriter out = null;
	BufferedReader in = null;

	public ServerStateChangeListener( String host, int port )
	{
		try
		{
			clientSocket = new Socket( host, port );
			out = new PrintWriter( clientSocket.getOutputStream(), true );
			in = new BufferedReader( new InputStreamReader( clientSocket.
					getInputStream() ) );
			
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}

	@Override
	public synchronized void run()
	{
		while( true )
		{
			try
			{
				this.wait();
				out.println("Server State Changed");
			}
			catch( Exception ex )
			{
				ex.printStackTrace();
			}
			System.out.println( "SERVER STATE CHANGED" );
		}
	}

	public synchronized void serverStateChanged()
	{
		this.notify();
	}
}
