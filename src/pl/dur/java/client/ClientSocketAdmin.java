package pl.dur.java.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import pl.dur.java.dispatchers.Dispatcher;
import pl.dur.java.events.mappers.EventMapper;
import pl.dur.java.messages.Message;
import pl.dur.java.socketAdmins.SocketAdmin;

/**
 *
 * @author Dur
 */
public class ClientSocketAdmin implements Runnable, SocketAdmin
{
	private Socket socket = null;
	private String host = "";
	private int port = 0;
	//send to server
	private ArrayBlockingQueue<Message> outputBlockingQueue = null;
	//all external actions will be send to dispatcher
	private ArrayBlockingQueue<Message> actionsBlockingQueue = null;
	//state changeing 
	private ArrayBlockingQueue<SocketAdmin> internalStateChangeActions = null;
	private Dispatcher dispatcher = null;
	private EventMapper eventMapper = null;

	private class InputListener implements Runnable, SocketAdmin
	{
		private ArrayBlockingQueue<Message> actionQueue;
		private ObjectInputStream in;

		public InputListener( ArrayBlockingQueue<Message> queue, ObjectInputStream input )
		{
			this.in = input;
			this.actionQueue = queue;
		}

		@Override
		public void run()
		{
			Message message = new Message( "", new Object() );
			while( !Thread.interrupted() )
			{
				try
				{
					message = ((Message) in.readObject());
				}
				catch( ClassNotFoundException ex )
				{
					ex.printStackTrace();
				}
				catch( OptionalDataException ex )
				{
					ex.printStackTrace();
				}
				catch( IOException ex )
				{
					ex.printStackTrace();
				}
				try
				{
					actionQueue.put( message );

				}
				catch( InterruptedException ex )
				{
					ex.printStackTrace();
				}
			}
		}
	}

	private class OutputSender implements Runnable
	{
		private ArrayBlockingQueue<Message> outputQueue;
		private ObjectOutputStream out;

		public OutputSender( ArrayBlockingQueue<Message> queue, ObjectOutputStream output )
		{
			this.out = output;
			this.outputQueue = queue;
		}

		@Override
		public void run()
		{
			Message message = new Message( "", new Object() );
			while( !Thread.interrupted() )
			{
				try
				{
					message = outputQueue.take();
				}
				catch( InterruptedException ex )
				{
					ex.printStackTrace();
				}
				try
				{
					out.writeObject( message );
				}
				catch( IOException ex )
				{
					ex.printStackTrace();
				}
			}
		}
	}

	public ClientSocketAdmin( int portNum, String host, EventMapper eventMapper )
	{
		this.host = host;
		this.port = portNum;
		this.eventMapper = eventMapper;
	}

	private synchronized void connect()
	{
		try
		{
			System.out.println( "connecting to " + host + port );
			socket = new Socket( host, port );
			Thread inputListener = new Thread( new InputListener( actionsBlockingQueue, new ObjectInputStream( socket.getInputStream() ) ) );
			inputListener.start();
			Thread outputWritter = new Thread( new OutputSender( outputBlockingQueue, new ObjectOutputStream( socket.getOutputStream() ) ) );
			outputWritter.start();
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}

	@Override
	public synchronized void run()
	{
		dispatcher = new Dispatcher( this, actionsBlockingQueue, eventMapper  );
		connect();
	}

	public void setActionToExecute( Message message )
	{
		try
		{
			actionsBlockingQueue.put( message );
		}
		catch( InterruptedException ex )
		{
			ex.printStackTrace();
		}
	}

	public void putInternalStateChaneActions( SocketAdmin socketClone )
	{
		try
		{
			internalStateChangeActions.put( socketClone );
		}
		catch( InterruptedException ex )
		{
			ex.printStackTrace();
		}
	}

	public void sendToServer( Message request )
	{
		try
		{
			outputBlockingQueue.put( request );
		}
		catch( InterruptedException ex )
		{
			ex.printStackTrace();
		}
	}
}
