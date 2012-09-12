/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import pl.dur.java.dispatchers.Dispatcher;
import pl.dur.java.events.mappers.EventMapper;
import pl.dur.java.events.mappers.StandardServerMapper;
import pl.dur.java.messages.Message;

/**
 *
 * @author Dur
 */
public class ServerSide
{
	private ConnectionReceiver connectionReceiver;
	private BlockingQueue<Message> actions;
	private List<EventMapper> mappers;
	private int listenPort;
	private int queueSize;

	public ServerSide()
	{
		queueSize = 100;
		actions = new ArrayBlockingQueue<Message>(queueSize);
		mappers = new ArrayList<EventMapper>();
		mappers.add( new StandardServerMapper() );
		Dispatcher dispatcher = new Dispatcher( actions, mappers );
		connectionReceiver = new ConnectionReceiver( actions, listenPort, queueSize );
		Thread dispatcherThred = new Thread(dispatcher);
		Thread connectionReceiverThread = new Thread( connectionReceiver );
		connectionReceiverThread.start();
		dispatcherThred.start();
	}

}
