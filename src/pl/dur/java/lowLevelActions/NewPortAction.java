/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.lowLevelActions;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.dur.java.actions.Action;
import pl.dur.java.client.ClientSocketAdmin;
import pl.dur.java.messages.Message;
import pl.dur.java.socketAdmins.SocketAdmin;

/**
 *
 * @author Dur
 */
public class NewPortAction extends Action
{
	public void execute( Object param )
	{
		HashMap<String, Object> parametersMap = (HashMap<String, Object>) param;
		SocketAdmin admin = null; 
		System.out.println( "New Port action" );
		String host = "";
		Integer port = -1;
		try
		{
			admin = (SocketAdmin) parametersMap.get( "SOCKET_ADMIN");
			port = (Integer) parametersMap.get( "PORT" );
			host = (String) parameters.get( "HOST" );

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
		Socket socket;
		ClientSocketAdmin admin = (ClientSocketAdmin) administrator;
		admin.changeSocket( port, host );
	}
	
}
