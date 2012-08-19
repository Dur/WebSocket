/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package p.dur.java.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import pl.dur.java.dispatchers.Dispatcher;
import pl.dur.java.dispatchers.StandardServerDispatcher;

/**
 *
 * @author Dur
 */
public class RequestListener implements Runnable
{
	private int socketNumber;
	private ServerSocket server = null;
	private Socket client = null;
	private BufferedReader in = null;
	private PrintWriter out = null;
	private String line;
	private Dispatcher serverDispatcher = new StandardServerDispatcher();

	public RequestListener( int socketNumber )
	{
		this.socketNumber = socketNumber;
	}

	@Override
	public void run()
	{
		System.out.println( "ServerThread " + socketNumber );
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
		}
		catch( IOException e )
		{
			System.out.println( "Accept failed:" + socketNumber );
			System.exit( -1 );
		}
		try
		{
			in = new BufferedReader( new InputStreamReader( client.
					getInputStream() ) );
			out = new PrintWriter( client.getOutputStream(), true );
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
		while( true )
		{
			try
			{
				line = in.readLine();
				out.println( line );
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
}
