/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.lowLevelActions;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import pl.dur.java.actions.Action;
import pl.dur.java.socketAdmins.SocketAdmin;

/**
 *
 * @author Dur
 */
public class NewPortAction implements Action
{
	@Override
	public void execute( Object param, SocketAdmin administrator)
	{
		Integer port = (Integer) param;
		String host = toResponse.getRemoteSocketAddress().toString();
		int addresIndex = host.lastIndexOf( "/" );
		host = host.substring( addresIndex + 1 );
		int index = host.lastIndexOf( ":" );
		host = host.substring( 0, index );
	}
}
