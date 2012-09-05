/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.dispatchers;

import java.net.Socket;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.concurrent.BlockingQueue;
import pl.dur.java.events.mappers.EventMapper;
import pl.dur.java.messages.Message;
import pl.dur.java.socketAdmins.SocketAdmin;

/**
 *
 * @author Dur
 */
public class Dispatcher implements Runnable
{
	StringTokenizer tokenizer = null;
	SocketAdmin socketAdministrator;
	BlockingQueue<Message> requestQueue;
	EventMapper eventMapper;

	public Dispatcher( SocketAdmin newSocketAdministrator, BlockingQueue<Message> newRequestQueue, EventMapper mapper )
	{
		this.socketAdministrator = newSocketAdministrator;
		this.requestQueue = newRequestQueue;
		this.eventMapper = mapper;
	}


	@Override
	public void run()
	{
		Message message = new Message("", new Object());
		while( !Thread.interrupted() )
		{
			try
			{
				message = requestQueue.take();
			}
			catch( InterruptedException ex )
			{
				ex.printStackTrace();
			}
			eventMapper.executeAction( message.getParams(), message.getRequest(), socketAdministrator);
		}
	}
}
