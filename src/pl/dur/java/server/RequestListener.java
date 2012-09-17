/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import pl.dur.java.components.register.ServerComponentsRegister;
import pl.dur.java.dispatchers.Dispatcher;
import pl.dur.java.messages.Message;

/**
 *
 * @author Dur
 */
public class RequestListener implements Runnable
{
	private ServerSocket server = null;
	private Socket client = null;
	private int port = 0;
	//send to server
	private BlockingQueue<Message> outputBlockingQueue = null;
	//all external actions will be send to dispatcher
	private BlockingQueue<Message> actionsBlockingQueue = null;
	private Thread outputWritter;
	private Thread inputListener;

	public RequestListener( BlockingQueue<Message> actions, int portNum, String host, int maxRequest )
	{
		this.port = portNum;
		actionsBlockingQueue = actions;
		outputBlockingQueue = new ArrayBlockingQueue<Message>( maxRequest );
	}

	@Override
	public void run()
	{
		System.out.println( "RequestListener on port " + port );

		try
		{
			server = new ServerSocket( port );
		}
		catch( IOException e )
		{
			System.out.println( "Could not listen on port " + port );
			System.exit( -1 );
		}

		try
		{
			client = server.accept();
			inputListener = new Thread( new InputListener( this.actionsBlockingQueue, this.client ) );
			inputListener.start();
			outputWritter = new Thread( new OutputSender( outputBlockingQueue, this.client ) );
			outputWritter.start();
			System.out.println( "connection accepted from " + client.getRemoteSocketAddress() );
		}
		catch( IOException e )
		{
			System.out.println( "Accept failed:" + port );
			finalize();
		}

	}

	public void sendToClient( Message request )
	{
		try
		{
			this.outputBlockingQueue.put( request );
		}
		catch( InterruptedException ex )
		{
			System.out.println( "cant put to output queue" );
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

	public void finalize()
	{
		System.out.println( "killing threads" );
		inputListener.interrupt();
		System.out.println( "InputListener killed" );
		outputWritter.interrupt();
		System.out.println( "OutputListener killed" );
		try
		{
			client.close();
			server.close();
		}
		catch( IOException ex )
		{
			ex.printStackTrace();
			System.exit( -1 );
		}
	}

	private class InputListener implements Runnable
	{
		private BlockingQueue<Message> actionQueue;
		private ObjectInputStream in;
		private final Socket inputSocket;

		public InputListener( BlockingQueue<Message> queue, Socket newSocket )
		{
			this.actionQueue = queue;
			this.inputSocket = newSocket;
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
			Message message = new Message( "", null );
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
				catch( Exception ex )
				{
					System.out.println( "Unknown excepcion, killing thread input listener" );
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
		private BlockingQueue<Message> outputQueue;
		private ObjectOutputStream out;
		private Socket outputSocket;

		public OutputSender( BlockingQueue<Message> queue, Socket newSocket )
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
			Message message = new Message( "", null );
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
}