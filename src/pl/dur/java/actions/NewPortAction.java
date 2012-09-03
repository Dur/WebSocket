/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.actions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Dur
 */
public class NewPortAction extends Action
{
	@Override
	public void execute( Object param, Socket toResponse )
	{
		Integer port = (Integer) param;
		try
		{
			toResponse = new Socket( toResponse.getRemoteSocketAddress().
					toString(), port );
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
		System.out.println( "connecting to " + toResponse.getRemoteSocketAddress().toString() + port );
	}
}
