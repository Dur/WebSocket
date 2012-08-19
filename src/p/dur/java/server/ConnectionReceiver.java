/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package p.dur.java.server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import javax.swing.*;
import pl.dur.java.model.ConnectionHolder;

/**
 *
 * @author Dur
 */
public class ConnectionReceiver implements Runnable
{
	static int MAX_PORT_NUM = 65000;
	HashSet<Integer> usedPorts = new HashSet<Integer>();
	ServerSocket server = null;
	Socket client = null;
	BufferedReader in = null;
	PrintWriter out = null;
	String line;
	Integer socketNum = 0;
	ConnectionHolder connectionHolder = new ConnectionHolder();

	ConnectionReceiver()
	{ //Begin Constructor

	} //End Constructor

	public void listenSocket()
	{
		try
		{
			server = new ServerSocket( 80 );
		}
		catch( IOException e )
		{
			System.out.println( "Could not listen on port 80" );
			System.exit( -1 );
		}
		while( true )
		{
			try
			{
				client = server.accept();
			}
			catch( IOException e )
			{
				System.out.println( "Accept failed: 80" );
				System.exit( -1 );
			}
			try
			{
				socketNum = (int) (Math.random() * MAX_PORT_NUM);
				while( socketNum.intValue() < 80 )
				{
					socketNum = (int) (Math.random() * MAX_PORT_NUM);
				}
				usedPorts.add( socketNum );
				in = new BufferedReader( new InputStreamReader( client.
						getInputStream() ) );
				out = new PrintWriter( client.getOutputStream(), true );
				RequestListener clientRequestListener = new RequestListener( socketNum );
				Thread thread = new Thread( clientRequestListener );
				thread.start();
				System.out.println( "Po uruchomieniu wątku" );
				out.println( "NP "+socketNum);
			}
			catch( IOException e )
			{
				System.out.println( "Accept failed: 80" );
				System.exit( -1 );
			}
		}
	}

	@Override
	public void run()
	{
		listenSocket();
	}
}
