/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.dispatchers;

import java.net.Socket;
import java.util.LinkedList;
import java.util.StringTokenizer;
import pl.dur.java.actions.Action;
import pl.dur.java.events.mappers.EventMapper;
import pl.dur.java.messages.Message;
import pl.dur.java.model.ConnectionHolder;

/**
 *
 * @author Dur
 */
public class Dispatcher
{
	EventMapper eventMapper = null;
	StringTokenizer tokenizer = null;

	public Dispatcher( EventMapper eventMapper )
	{
		this.eventMapper = eventMapper;
	}

	public void dispatch( Message message, Socket client )
	{
		String command;
		tokenizer = new StringTokenizer( message.getRequest(), " " );
		LinkedList<String> params = new LinkedList<String>();
		if( message.getParams() == null )
		{
			if( tokenizer.countTokens() > 0 )
			{
				command = tokenizer.nextToken();
				eventMapper.executeAction( params, command, client );
			}
		}
		else
		{
			command = message.getRequest();
			eventMapper.executeAction( message.getParams(), command, client);
		}
	}
}
