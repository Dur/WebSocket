/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package p.dur.java.server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import pl.dur.java.model.ConnectionHolder;

/**
 *
 * @author Dur
 */
public class ServerSide extends JFrame implements ActionListener
{
	ConnectionReceiver connectionReceiver = new ConnectionReceiver();
	private ServerStateChangeListener serverStateListener = new ServerStateChangeListener();
	JButton button;
	JPanel panel;
	JTextArea textArea = new JTextArea();
	JTextField textField;
	ConnectionHolder connectionHolder = new ConnectionHolder();

	public ServerSide()
	{
		button = new JButton( "Click Me" );
		button.addActionListener( this );
		panel = new JPanel();
		panel.setLayout( new BorderLayout() );
		panel.setBackground( Color.white );
		getContentPane().add( panel );
		panel.add( "Center", textArea );
		panel.add( "South", button );
	}

	@Override
	public void actionPerformed( ActionEvent event )
	{
		Object source = event.getSource();

		if( source == button )
		{
			try
			{
				serverStateListener.serverStateChanged();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

	protected void finalize()
	{
	}

	private void startServer()
	{
		Thread connectionReceiverThread = new Thread( connectionReceiver );
		connectionReceiverThread.start();
		Thread serverStateListenerThread = new Thread( serverStateListener );
		serverStateListenerThread.start();
	}

	public static void main( String args[] )
	{
		ServerSide serverSide = new ServerSide();
		serverSide.setTitle( "Server Program" );
		WindowListener l = new WindowAdapter()
		{
			public void windowClosing( WindowEvent e )
			{
				System.exit( 0 );
			}
		};
		serverSide.addWindowListener( l );
		serverSide.pack();
		serverSide.setVisible( true );
		serverSide.startServer();
	}
}
