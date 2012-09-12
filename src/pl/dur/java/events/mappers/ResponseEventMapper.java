/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.events.mappers;

import java.util.HashMap;
import java.util.Map;
import pl.dur.java.actions.Action;
import pl.dur.java.actions.ResponseAction;
import pl.dur.java.client.ClientSocketAdmin;
import pl.dur.java.messages.Message;

/**
 *
 * @author Dur
 */
public class ResponseEventMapper extends EventMapper
{
	ClientSocketAdmin admin;

	public ResponseEventMapper( ClientSocketAdmin admin )
	{
		this.admin = admin;
	}

	@Override
	public int executeAction( Message message )
	{
		int toReturn = -1;
		HashMap<String, Object> params;
		if( message.getParams() != null )
		{
			params = message.getParams();
			params.put( "TO_RESPONSE", admin );
		}
		else
		{
			params = new HashMap<String, Object>();
			params.put( "TO_RESPONSE", admin );
		}
		final Action action = this.actionMapper.get( message.getRequest() );
		if( action != null )
		{
			toReturn = 0;
			action.execute( params );
		}
		return toReturn;
	}

	public void fulFillMap( Map<String, Action> actions )
	{
		if( actions != null )
		{
			this.actionMapper.putAll( actions );
		}
		this.actionMapper.put("RESPONSE", new ResponseAction());
	}
}
