package pl.dur.java.model;

import java.util.HashMap;
import pl.dur.java.server.RequestListener;

/**
 * Class which contains all connections from clients. After serwer state change,
 * the dispatcher object could use this class to let the clients know that
 * serwer has some new informations.
 *
 * @author Dur
 */
public class ConnectionHolder
{
	HashMap<Integer, RequestListener> connections = new HashMap<Integer, RequestListener>();

	public void stateChanged(String change)
	{
		for(Integer key : connections.keySet() )
		{
			connections.get( key ).serverStateChanged( change );
		}
	}

	public void addConnection( Integer socketNum, RequestListener thread )
	{
		connections.put( socketNum, thread );
	}
	
	public void removeConnection(Integer key)
	{
		connections.remove( key );
	}
}
