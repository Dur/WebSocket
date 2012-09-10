/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.dispatchers;

import java.util.List;
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
	List<EventMapper> executionList = null;

	public Dispatcher( SocketAdmin newSocketAdministrator, BlockingQueue<Message> newRequestQueue, List<EventMapper> executionList )
	{
		this.socketAdministrator = newSocketAdministrator;
		this.requestQueue = newRequestQueue;
		this.executionList = executionList;
	}


	@Override
	public void run()
	{
		Message message = new Message("", new Object());
		while( !Thread.interrupted() )
		{
			try
			{
				System.out.println("Dispatcher wait for message in queue");
				message = requestQueue.take();
				System.out.println("Dispatcher getting request to dispatch");
			}
			catch( InterruptedException ex )
			{
				ex.printStackTrace();
			}
			for(EventMapper mapper : executionList)
			{
				if( mapper.executeAction( message ) != -1)
				{
					break;
				}
			}
		}
	}
}
