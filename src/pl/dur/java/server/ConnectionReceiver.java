/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import pl.dur.java.messages.Message;
import pl.dur.java.model.ConnectionHolder;

/**
 *
 * @author Dur
 */
public class ConnectionReceiver implements Runnable
{
	private static int MAX_PORT_NUM = 65000;
	private HashSet<Integer> usedPorts = new HashSet<Integer>();
	private ServerSocket server = null;
	private Socket client = null;
	private Integer portNum = 0;
	private ConnectionHolder connectionHolder = new ConnectionHolder();
	private ServerStateChangeListener serverStateListener;
	private Message response = null;
	private ObjectOutputStream output = null;
	private int localPort;
	private BlockingQueue<Message> actions;
	private int queueSize;

	ConnectionReceiver( BlockingQueue<Message> actionsQueue, int port, int newQueueSize )
	{
		this.localPort = port;
		this.actions = actionsQueue;
		this.queueSize = newQueueSize;
	}

	public void listenSocket()
	{
		System.out.println( "before binding port" );
		try
		{
			server = new ServerSocket( 80 );
		}
		catch( IOException e )
		{
			System.out.println( "Could not listen on port 80" );
			e.printStackTrace();
			System.exit( -1 );
		}
		while( true )
		{
			try
			{
				client = server.accept();
			}
			catch( IOException e )
			{
				System.out.println( "Accept failed: 80" );
				System.exit( -1 );
			}
			try
			{
				portNum = (int) ( Math.random() * MAX_PORT_NUM );
				while( portNum.intValue() < 80 )
				{
					portNum = (int) (Math.random() * MAX_PORT_NUM);
				}
				usedPorts.add( portNum );
				output = new ObjectOutputStream( client.getOutputStream() );
				RequestListener clientRequestListener = new RequestListener( actions, portNum.intValue(), server.getInetAddress().toString(), this.queueSize );
				Thread thread = new Thread( clientRequestListener );
				thread.start();
				System.out.println( "Selected port for client " + portNum );
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put( "HOST", this.server.getLocalSocketAddress().toString() );
				params.put( "PORT", portNum );
				response = new Message( "NP", params );
				System.out.println( "before sending message" );
				output.writeObject( response );
				System.out.println( "after sending message" );
			}
			catch( IOException e )
			{
				System.out.println( "Accept failed: 80" );
				System.exit( -1 );
			}
		}
	}

	@Override
	public void run()
	{
		System.out.println( "starting ConnectionReceiver" );
		listenSocket();
	}

	public ConnectionHolder getConnectionHolder()
	{
		return connectionHolder;
	}

	public void setConnectionHolder( ConnectionHolder connectionHolder )
	{
		this.connectionHolder = connectionHolder;
	}
}
