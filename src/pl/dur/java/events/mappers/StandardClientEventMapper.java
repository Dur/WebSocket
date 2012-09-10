/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.events.mappers;

import java.util.HashMap;
import java.util.List;
import pl.dur.java.actions.Action;
import pl.dur.java.lowLevelActions.NewPortAction;
import pl.dur.java.lowLevelActions.TestAction;
import pl.dur.java.messages.Message;
import pl.dur.java.socketAdmins.SocketAdmin;

/**
 *
 * @author Dur
 */
public class StandardClientEventMapper extends EventMapper
{
	HashMap<String, Object> params = null;
	
	public StandardClientEventMapper( HashMap<String, Action> mapper, SocketAdmin socketAdmin )
	{
		super( mapper );
		fulfillMap( null );
		this.params = new HashMap<String, Object>();
		params.put("SOCKET_ADMIN", socketAdmin);
	}
	
	protected void fulfillMap( List<EventMapper> eventMappersList )
	{
		super.setOrReplaceAction( "NP", new NewPortAction());
		super.setOrReplaceAction( "", new TestAction());
	}

	@Override
	public int executeAction( Message message )
	{
		int result = -1;
		Action action = this.actionMapper.get( message.getRequest() );
		if( action != null )
		{
			params.put("REQUEST_PARAMS", message.getParams());
			action.execute( params );
			result = 0;
		}
		return result;
	}
	
}
