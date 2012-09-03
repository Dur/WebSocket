/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import pl.dur.java.dispatchers.Dispatcher;
import pl.dur.java.messages.Message;

/**
 *
 * @author Dur
 */
public class RequestListener implements Runnable
{
	private int socketNumber;
	private ServerSocket server = null;
	private Socket client = null;
	private ObjectInputStream input;
	private Dispatcher dispatcher = null;

	public RequestListener( int socketNumber, Dispatcher dipatcher )
	{
		this.socketNumber = socketNumber;
		this.dispatcher = dipatcher;
	}

	@Override
	public void run()
	{
		System.out.println( "RequestListener on port " + socketNumber );

		try
		{
			server = new ServerSocket( socketNumber );
		}
		catch( IOException e )
		{
			System.out.println( "Could not listen on port " + socketNumber );
			System.exit( -1 );
		}

		try
		{
			client = server.accept();
			System.out.println( "connection accepted from " + client.getRemoteSocketAddress() );
		}
		catch( IOException e )
		{
			System.out.println( "Accept failed:" + socketNumber );
			System.exit( -1 );
		}
		try
		{
			input = new ObjectInputStream( client.getInputStream() );
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
		Object inputObject;
		Message request;
		while( true )
		{
			try
			{
				System.out.println( "waiting for input" );
				inputObject = input.readObject();
				if( inputObject.getClass() == String.class )
				{
					request = new Message( (String) inputObject, null );
				}
				else
				{
					request = (Message) inputObject;
				}
				dispatcher.dispatch( request, client );
			}
			catch( Exception e )
			{
				System.out.println( "Read failed" );
				System.exit( -1 );
			}
		}
	}

	public Socket getClientSocket()
	{
		return client;
	}

	public void serverStateChanged( String change )
	{
	}
}
