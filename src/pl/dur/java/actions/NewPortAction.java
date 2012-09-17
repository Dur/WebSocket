/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.actions;

import java.util.Map;
import pl.dur.java.actions.Action;
import pl.dur.java.client.ClientSocketAdmin;
import pl.dur.java.components.register.ClientComponentsRegister;

/**
 *
 * @author Dur
 */
public class NewPortAction extends Action
{
	public void execute( Map<String, Object> param )
	{
		System.out.println(param.toString());
		ClientSocketAdmin admin = null; 
		System.out.println( "New Port action" );
		String host = "";
		Integer port = -1;
		try
		{
			admin = (ClientSocketAdmin) ClientComponentsRegister.getComponent( "SOCKET_ADMIN");
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
