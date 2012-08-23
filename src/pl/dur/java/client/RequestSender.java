/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author Dur
 */
public class RequestSender implements Runnable
{
	private Socket socket = null;
	BufferedReader in;
	PrintWriter out;
	private final String host;
	private int port;
	String myRequest = "";

	public RequestSender( int portNum, String host )
	{
		this.host = host;
		this.port = portNum;
	}

	public void connect()
	{
		try
		{
			System.out.println( "connecting to " + host + port );
			socket = new Socket( host, port );
			out = new PrintWriter( socket.getOutputStream(), true );
			in = new BufferedReader( new InputStreamReader( socket.
					getInputStream() ) );
			System.out.println( "after connect to new socket" );
			String serverAnswer = in.readLine();
			if( serverAnswer.contains( "NP" ) )
			{
				port = Integer.parseInt( serverAnswer.substring( 3 ) );
				socket = new Socket( host, port );
				out = new PrintWriter( socket.getOutputStream(), true );
				in = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
				System.out.println( "connecting to " + host + port );
			}
		}
		catch( UnknownHostException e )
		{
			System.out.println( "Unknown host" );
			System.exit( 1 );
		}
		catch( IOException e )
		{
			System.out.println( "No I/O" );
			System.exit( 1 );
		}
		System.out.println( "after connect" );
	}

	public synchronized void sendRequest( String request )
	{
		this.myRequest = request;
		this.notify();
	}

	@Override
	public synchronized void run()
	{
		connect();
		while( true )
		{
			try
			{
				this.wait();
				out.println( myRequest );
				System.out.println( "sending " + myRequest );
				System.out.println( "received " + in.readLine() );
			}
			catch( Exception ex )
			{
				ex.printStackTrace();
			}
		}
	}

	public void setRequest( String request )
	{
		this.myRequest = request;
	}
}
