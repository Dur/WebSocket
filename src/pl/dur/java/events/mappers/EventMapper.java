package pl.dur.java.events.mappers;

import java.util.HashMap;
import pl.dur.java.actions.Action;
import pl.dur.java.messages.Message;

/**
 *
 * @author Dur
 */
public class EventMapper
{
	protected HashMap<String, Action> actionMapper;

	public EventMapper()
	{
		this.actionMapper = new HashMap<String, Action>();
	}

	public EventMapper( HashMap<String, Action> actions )
	{
		if( actions != null )
		{
			this.actionMapper.putAll( actions );
		}
	}

	public int executeAction( Message message )
	{
		HashMap<String, Object> params = message.getParams();
		int result = -1;
		Action action = this.actionMapper.get( message.getRequest() );
		if( action != null )
		{
			action.execute( params );
			result = 0;
		}
		return result;
	}

	public void setOrReplaceAction( String name, Action action )
	{
		actionMapper.put( name, action );
	}

	public int setAction( String name, Action action )
	{
		Action previousAction = this.actionMapper.put( name, action );
		if( previousAction != null )
		{
			actionMapper.put( name, previousAction );
			return -1;
		}
		return 0;
	}
	
	@Override
	public String toString()
	{
		return actionMapper.toString();
	}
}
