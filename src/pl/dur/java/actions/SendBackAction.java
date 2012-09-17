/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.actions;

import java.util.Map;
import pl.dur.java.client.ClientSocketAdmin;
import pl.dur.java.components.register.ClientComponentsRegister;

/**
 *
 * @author Dur
 */
public class SendBackAction extends Action
{

	@Override
	public void execute( Map<String, Object> params )
	{
		ClientSocketAdmin admin = (ClientSocketAdmin) ClientComponentsRegister.getComponent( "SOCKET_ADMIN");
		admin.sendToServer( null );
	}
	
}
