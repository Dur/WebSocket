package pl.dur.java.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.ClosedByInterruptException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
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
	Thread outputWritter;
	Thread inputListener;

	private class InputListener implements Runnable, SocketAdmin, Serializable
	{
		static final long serialVersionUID = 42L;
		private ArrayBlockingQueue<Message> actionQueue;
		private ObjectInputStream in;
		Socket inputSocket;

		public InputListener( ArrayBlockingQueue<Message> queue, Socket newSocket )
		{
			this.actionQueue = queue;
			this.inputSocket = socket;
		}

		@Override
		public void run()
		{
			try
			{
				in = new ObjectInputStream( inputSocket.getInputStream() );
			}
			catch( IOException ex )
			{
				if( Thread.interrupted() )
				{
					System.out.println( "Input listener interrupted" );
					return;
				}
				ex.printStackTrace();
				return;

			}
			System.out.println( "inputListener is running" );
			Message message = new Message( "", new Object() );
			while( !Thread.interrupted() )
			{
				try
				{
					System.out.println( "Waiting for input" );
					message = ((Message) in.readObject());
					System.out.println( "Got request from server: " + message.getRequest() );
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
					System.out.println( "IO Exception in Input listener" );
					return;
				}
				catch(Exception ex)
				{
					System.out.println("Unknown excepcion, killing thread input listener");
					return;
				}
				try
				{
					System.out.println( "setting message for dispacher" );
					actionQueue.put( message );
					System.out.println( "Message set" );

				}
				catch( InterruptedException ex )
				{
					System.out.println( "input listener interrupted" );
					return;
				}
			}
		}
	}

	private class OutputSender implements Runnable, Serializable
	{
		static final long serialVersionUID = 42L;
		private ArrayBlockingQueue<Message> outputQueue;
		private ObjectOutputStream out;
		private Socket outputSocket;

		public OutputSender( ArrayBlockingQueue<Message> queue, Socket newSocket )
		{
			this.outputQueue = queue;
			outputSocket = newSocket;
		}

		@Override
		public void run()
		{
			try
			{
				this.out = new ObjectOutputStream( outputSocket.getOutputStream() );
				System.out.println( "output listener got stream" );
			}
			catch( IOException ex )
			{
				if( Thread.interrupted() )
				{
					return;
				}
				ex.printStackTrace();

			}
			System.out.println( "Output sender is runing" );
			Message message = new Message( "", new Object() );
			while( !Thread.interrupted() )
			{
				try
				{
					message = outputQueue.take();
				}
				catch( InterruptedException ex )
				{
					System.out.println( "Output sender interrupted" );
					return;
				}
				try
				{
					out.writeObject( message );
				}
				catch( IOException ex )
				{
					System.out.println( "exception thrown in output sender" );
					ex.printStackTrace();
					return;
				}
			}
		}
	}

	public ClientSocketAdmin( int portNum, String host, EventMapper eventMapper, int maxRequest )
	{
		this.host = host;
		this.port = portNum;
		this.eventMapper = eventMapper;
		actionsBlockingQueue = new ArrayBlockingQueue<Message>( maxRequest );
		outputBlockingQueue = new ArrayBlockingQueue<Message>( maxRequest );
	}

	private synchronized void connect()
	{
		try
		{
			System.out.println( "connecting to " + host + ":" + port );
			socket = new Socket( host, port );
			inputListener = new Thread( new InputListener( this.actionsBlockingQueue, this.socket ) );
			inputListener.start();
			outputWritter = new Thread( new OutputSender( outputBlockingQueue, socket ) );
			outputWritter.start();
		}
		catch( Exception ex )
		{
			System.out.println( "exception thrown in connect line " + ex.getLocalizedMessage() );
			ex.printStackTrace();
		}
	}

	@Override
	public synchronized void run()
	{
		dispatcher = new Dispatcher( this, this.actionsBlockingQueue, eventMappers );
		Thread dispatcherThread = new Thread( dispatcher );
		dispatcherThread.start();
		connect();
		while( !Thread.interrupted() )
		{
		}
	}

	public void sendToServer( Message request )
	{
		try
		{
			this.outputBlockingQueue.put( request );
		}
		catch( InterruptedException ex )
		{
			System.out.println( "exception thrown in connect line " + ex.getLocalizedMessage() );
			ex.printStackTrace();
		}
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

	public void changeSocket( int port, String host )
	{
		Socket newSocket;
		System.out.println( "Connecting to " + host + ":" + port );
		try
		{
			newSocket = new Socket( host, port );
			System.out.println( "Connected to new socket" );
		}
		catch( ClosedByInterruptException ex )
		{
			System.out.println( "input stream interrupted" );
			return;
		}
		catch( UnknownHostException ex )
		{
			System.out.println( "exception thrown in change socket line " + ex.getLocalizedMessage() );
			ex.printStackTrace();
			return;
		}
		catch( IOException ex )
		{
			System.out.println( "exception thrown in change socket line " + ex.getLocalizedMessage() );
			ex.printStackTrace();
			return;
		}
		if( newSocket.isConnected() )
		{
			System.out.println( "killing threads" );
			inputListener.interrupt();
			System.out.println( "InputListener killed" );
			outputWritter.interrupt();
			System.out.println( "OutputListener killed" );
			this.socket = newSocket;
			System.out.println( "Getting new listeners" );
			try
			{
				outputWritter = new Thread( new OutputSender( outputBlockingQueue, newSocket ) );
				outputWritter.start();
				System.out.println( "has new output listener" );
				inputListener = new Thread( new InputListener( actionsBlockingQueue, newSocket ) );
				inputListener.start();
				System.out.println( "has new Input listener" );
			}
			catch( Exception ex )
			{
				System.out.println( "exception thrown in change socket line " + ex.getLocalizedMessage() );
				ex.printStackTrace();
			}
			System.out.println( "connected" );
		}
	}
}
