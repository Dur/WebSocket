package pl.dur.java.test;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;

import java.io.*;
import java.net.*;
import java.util.HashSet;
import pl.dur.java.model.ConnectionHolder;

class Server extends JFrame implements ActionListener
{
	static int MAX_PORT_NUM = 65000;
	JButton button;
	HashSet<Integer> usedPorts = new HashSet<Integer>();
	JLabel label = new JLabel( "Text received over socket:" );
	JPanel panel;
	JTextArea textArea = new JTextArea();
	ServerSocket server = null;
	Socket client = null;
	BufferedReader in = null;
	PrintWriter out = null;
	String line;
	JTextField textField;
	Integer socketNum = 0;
	ConnectionHolder connectionHolder = new ConnectionHolder();

	Server()
	{ //Begin Constructor
		button = new JButton( "Click Me" );
		button.addActionListener( this );
		panel = new JPanel();
		panel.setLayout( new BorderLayout() );
		panel.setBackground( Color.white );
		getContentPane().add( panel );
		panel.add( "North", label );
		panel.add( "Center", textArea );
		panel.add( "South", button );

	} //End Constructor

	public void actionPerformed( ActionEvent event )
	{
		Object source = event.getSource();

		if( source == button )
		{
			connectionHolder.stateChanged();
		}
	}

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
				System.out.println( "Po uruchomieniu wątku" );
			}
			catch( IOException e )
			{
				System.out.println( "Accept failed: 80" );
				System.exit( -1 );
			}

			try
			{
				//line = "NP " + socketNum;
				line = "404";
				out.println( line );
			}
			catch( Exception e )
			{
				System.out.println( "Read failed" );
				System.exit( -1 );
			}
		}
	}

	protected void finalize()
	{
		try
		{
			in.close();
			out.close();
			server.close();
		}
		catch( IOException e )
		{
			System.out.println( "Could not close." );
			System.exit( -1 );
		}
	}

//	public static void main( String[] args )
//	{
//		Server frame = new Server();
//		System.out.println( "***************************" + frame.getClass().
//				toString() );
//		frame.setTitle( "Server Program" );
//		WindowListener l = new WindowAdapter()
//		{
//			public void windowClosing( WindowEvent e )
//			{
//				System.exit( 0 );
//			}
//		};
//		frame.addWindowListener( l );
//		frame.pack();
//		frame.setVisible( true );
//		frame.listenSocket();
//	}
}
