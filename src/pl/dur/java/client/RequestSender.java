/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.client;

import java.io.ObjectOutputStream;
import java.net.Socket;
import pl.dur.java.messages.Message;

/**
 *
 * @author Dur
 */
public class RequestSender implements Runnable
{
	private Socket socket = null;
	private ObjectOutputStream out;
	private final String host;
	private int port;
	private String myRequest = "";
	private ResponseExecutor responseExecutor = null;
	private Object requestParams = null;
	private Thread readyToServe;

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
			responseExecutor = new ResponseExecutor();
			readyToServe = new Thread( responseExecutor );
			readyToServe.start();
			while( readyToServe.getState() != Thread.State.WAITING )
			{
				System.out.println( "Waiting for thread to wait " );
			}
			responseExecutor.waitForAnswer( socket );
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}

	public synchronized void sendRequest( String request, Object params )
	{
		this.myRequest = request;
		this.requestParams = params;
		this.notify();
	}

	@Override
	public synchronized void run()
	{
		connect();
		Message request;
		try
		{
			out = new ObjectOutputStream( socket.getOutputStream() );
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
		while( true )
		{
			try
			{
				System.out.println( "waiting for request" );
				this.wait();
				System.out.println( "got request" );
				request = new Message( myRequest, requestParams );
				System.out.println( "sending " + myRequest );
				out.writeObject( request );
				responseExecutor.waitForAnswer( socket );
			}
			catch( Exception ex )
			{
				ex.printStackTrace();
			}
		}
	}
}
