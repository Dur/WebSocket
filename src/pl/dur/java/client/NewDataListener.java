/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Dur
 */
public class NewDataListener implements Runnable
{
	private ServerSocket serverState = null;
	private final int portNum;
	private Socket serverConnection;
	BufferedReader in;
	
	public NewDataListener( int portNum )
	{
		this.portNum = portNum;
		try
		{
			serverState = new ServerSocket( portNum );
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
	
	@Override
	public void run()
	{
		while( true )
		{
			System.out.println("waiting for server connection");
			try
			{
				serverConnection = serverState.accept();
			}
			catch( Exception ex )
			{
				ex.printStackTrace();
			}
			try
			{
				in = new BufferedReader( new InputStreamReader( serverConnection.
						getInputStream() ) );
				System.out.println("got server input");
				System.out.println( in.readLine() );
			}
			catch( Exception ex )
			{
				ex.printStackTrace();
			}
			try
			{
				serverState.close();
			}
			catch( Exception ex )
			{
				ex.printStackTrace();
			}
		}
	}
}
