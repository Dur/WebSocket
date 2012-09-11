package pl.dur.java.events.mappers;

import java.util.HashMap;
import pl.dur.java.actions.Action;
import pl.dur.java.messages.Message;

/**
 *
 * @author Dur
 */
public abstract class EventMapper
{
	protected HashMap<String, Action> actionMapper;

	public EventMapper()
	{
		this.actionMapper = new HashMap<String, Action>();
	}

	public EventMapper( HashMap<String, Action> mapper )
	{
		if( mapper != null )
		{
			this.actionMapper = mapper;
		}
		else
		{
			actionMapper = new HashMap<String, Action>();
		}
	}

	public abstract int executeAction( Message message );

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
