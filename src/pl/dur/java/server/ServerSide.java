/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.server;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import pl.dur.java.components.register.ServerComponentsRegister;
import pl.dur.java.dispatchers.Dispatcher;
import pl.dur.java.events.mappers.EventMapper;
import pl.dur.java.messages.Message;

/**
 *
 * @author Dur
 */
public class ServerSide
{
	private ConnectionReceiver connectionReceiver;
	private BlockingQueue<Message> actions;
	private EventMapper mapper;
	private int listenPort;
	private int queueSize;

	public ServerSide()
	{
		queueSize = 100;
		listenPort = 80;
		ServerComponentsRegister.addComponent( "QUEUE_SIZE", queueSize );
		mapper = new EventMapper( new ServerActionConfigurator().getConfigurator() );
		ServerComponentsRegister.addComponent( "MAPPERS", mapper );
		actions = new ArrayBlockingQueue<Message>( queueSize );
		ServerComponentsRegister.addComponent( "ACTIONS", actions );
		Dispatcher dispatcher = new Dispatcher( actions, mapper );
		ServerComponentsRegister.addComponent( "DISPATCHER", dispatcher );
		connectionReceiver = new ConnectionReceiver( listenPort );
		ServerComponentsRegister.addComponent( "CONNECTION_RECEIVER", connectionReceiver );
		Thread dispatcherThred = new Thread( dispatcher );
		Thread connectionReceiverThread = new Thread( connectionReceiver );
		connectionReceiverThread.start();
		dispatcherThred.start();
	}
}
