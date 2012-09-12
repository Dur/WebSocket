/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.actions;

import java.util.Map;
import pl.dur.java.client.ClientSocketAdmin;
import pl.dur.java.messages.Message;

/**
 *
 * @author Dur
 */
public class ResponseAction extends Action
{
	@Override
	public final void execute( final Map<String, Object> request )
	{
		ClientSocketAdmin toResponse = (ClientSocketAdmin) request.get( "TO_RESPONSE" );
		toResponse.sendToServer( new Message( "Hello", null ) );
	}
}
