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
	String request = "";

	public RequestSender( int portNum, String host )
	{
		this.host = host;
		this.port = portNum;
	}

	public void connect()
	{
		try
		{
			socket = new Socket( host, port );
			out = new PrintWriter( socket.getOutputStream(), true );
			in = new BufferedReader( new InputStreamReader( socket.
					getInputStream() ) );
			String line = in.readLine();
			System.out.println( "Text received :" + line );
			if( line.contains( "NP" ) )
			{
				line = line.substring( 3 );
				Integer newPort = Integer.parseInt( line );
				socket = new Socket( host, newPort.intValue() );
				this.port = newPort;
			}
			out = new PrintWriter( socket.getOutputStream(), true );
			in = new BufferedReader( new InputStreamReader( socket.
					getInputStream() ) );
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
	}

	public synchronized void sendRequest( String request )
	{
		this.request = request;
		this.notify();
	}

	public synchronized void sendRequestToServer()
	{
		try
		{
			this.wait();
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
		out.print( request );
		try
		{
			System.out.println( in.readLine() );
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}

	@Override
	public synchronized void run()
	{
		connect();
		while( true )
		{
			sendRequestToServer();
		}
	}

	public void setRequest( String request )
	{
		this.request = request;
	}
}
