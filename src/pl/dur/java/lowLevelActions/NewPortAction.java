/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.lowLevelActions;

import java.util.HashMap;
import pl.dur.java.actions.Action;
import pl.dur.java.client.ClientSocketAdmin;

/**
 *
 * @author Dur
 */
public class NewPortAction extends Action
{
	public void execute( HashMap<String, Object> param )
	{
		System.out.println(param.toString());
		ClientSocketAdmin admin = null; 
		System.out.println( "New Port action" );
		String host = "";
		Integer port = -1;
		try
		{
			admin = (ClientSocketAdmin) param.get( "SOCKET_ADMIN");
			port = (Integer) param.get( "PORT" );
			host = (String) param.get( "HOST" );

		}
		catch( NullPointerException ex )
		{
			ex.printStackTrace();
			return;
		}
		int addresIndex = host.lastIndexOf( "/" );
		host = host.substring( addresIndex + 1 );
		int index = host.lastIndexOf( ":" );
		host = host.substring( 0, index );
		admin.changeSocket( port, host );
	}
	
}
