/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.events.mappers;

import java.util.HashMap;
import java.util.List;
import pl.dur.java.actions.Action;
import pl.dur.java.client.ClientSocketAdmin;
import pl.dur.java.lowLevelActions.NewPortAction;
import pl.dur.java.lowLevelActions.TestAction;
import pl.dur.java.messages.Message;

/**
 *
 * @author Dur
 */
public class StandardClientEventMapper extends EventMapper
{
	HashMap<String, Object> params = null;

	public StandardClientEventMapper( HashMap<String, Action> mapper, ClientSocketAdmin socketAdmin )
	{
		super(mapper);
		fulfillMap( null );
		this.params = new HashMap<String, Object>();
		params.put( "SOCKET_ADMIN", socketAdmin );
		System.out.println(super.toString());
	}

	protected void fulfillMap( List<EventMapper> eventMappersList )
	{
		super.setOrReplaceAction( "NP", new NewPortAction() );
		super.setOrReplaceAction( "", new TestAction() );
	}

	@Override
	public int executeAction( Message message )
	{
		int result = -1;
		Action action = this.actionMapper.get( message.getRequest() );
		if( action != null )
		{
			for(String key : message.getParams().keySet() )
			{
				params.put( key, message.getParams().get( key ) );
			}
			action.execute( params );
			result = 0;
		}
		return result;
	}
}
