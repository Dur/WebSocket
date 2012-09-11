/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.dispatchers;

import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.BlockingQueue;
import pl.dur.java.client.ClientSocketAdmin;
import pl.dur.java.events.mappers.EventMapper;
import pl.dur.java.messages.Message;

/**
 *
 * @author Dur
 */
public class Dispatcher implements Runnable
{
	StringTokenizer tokenizer = null;
	ClientSocketAdmin socketAdministrator;
	BlockingQueue<Message> requestQueue;
	List<EventMapper> executorsList = null;

	public Dispatcher( BlockingQueue<Message> newRequestQueue, List<EventMapper> executorsList )
	{
		this.requestQueue = newRequestQueue;
		this.executorsList = executorsList;
	}


	@Override
	public void run()
	{
		Message message = new Message("", null);
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
			for(EventMapper mapper : executorsList)
			{
				if( mapper.executeAction( message ) != -1)
				{
					break;
				}
			}
		}
	}
}
