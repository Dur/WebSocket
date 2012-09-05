/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.events.mappers;

import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import pl.dur.java.actions.Action;
import pl.dur.java.messages.Message;
import pl.dur.java.socketAdmins.SocketAdmin;

/**
 *
 * @author Dur
 */
public abstract class EventMapper
{
	protected HashMap<String, Action> actionMapper = new HashMap<String, Action>();

	public EventMapper()
	{
	}

	public final void executeAction( Object param, String request, SocketAdmin administrator )
	{
		try
		{
			Action ax = actionMapper.get( request );
			if( ax != null )
			{
				ax.execute( param, administrator );
			}
			else
			{
				throw new NullPointerException( "Action not found in EventMapper");
			}
		}
		catch( NullPointerException ex )
		{
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}

	protected abstract void fulfillMap( List<EventMapper> eventMappersList );

	protected final HashMap<String, Action> getActionMapper()
	{
		return actionMapper;
	}
}
