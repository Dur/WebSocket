package pl.dur.java.client;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.*;
import java.io.PrintWriter;
import javax.swing.*;

/**
 *
 * @author Dur
 */
class ClientSide extends JFrame
		implements ActionListener
{
	JLabel text, clicked;
	JButton button;
	JButton webSocketPortOpener;
	JTextField portNumberTextField;
	JPanel panel;
	int newDataListenerPort;
	JTextField textField;
	RequestSender requestSender = null;
	NewDataListener serverState = null;
	static int MAX_PORT_NUM = 65000;

	ClientSide( int portNum, String host )
	{
		text = new JLabel( "Text to send over socket:" );
		textField = new JTextField( 20 );
		button = new JButton( "Click Me" );
		webSocketPortOpener = new JButton( "Open web-socket port" );
		webSocketPortOpener.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent event )
			{
				Integer webSocketPort = 0;
				try
				{
					webSocketPort = Integer.parseInt( portNumberTextField.
							getText() );
				}
				catch( NumberFormatException ex )
				{
					ex.printStackTrace();
				}
				if( webSocketPort != 0 )
				{
					requestSender.sendRequest( "WEBSOCKET" + webSocketPort.
							intValue() );
				}
			}
		} );
		portNumberTextField = new JTextField( 10 );

		button.addActionListener( this );
		panel = new JPanel();

		panel.setLayout( new BorderLayout() );
		panel.setBackground( Color.white );

		getContentPane().add( panel );
		panel.add( "South", text );
		panel.add( "West", textField );
		panel.add( "North", button );
		panel.add( "East", webSocketPortOpener );
		panel.add( "Center", portNumberTextField );
		
		newDataListenerPort = (int) (Math.random() * MAX_PORT_NUM);
		while( newDataListenerPort < 80 )
		{
			newDataListenerPort = (int) (Math.random() * MAX_PORT_NUM);
		}
		
		serverState = new NewDataListener( newDataListenerPort );
		Thread serverStateThread = new Thread( serverState );
		serverStateThread.start();
		
		requestSender = new RequestSender( portNum, host );
		Thread requestSenderThread = new Thread( requestSender );
		requestSenderThread.start();
	}

	public void actionPerformed( ActionEvent event )
	{
		Object source = event.getSource();

		if( source == button )
		{
			String text = textField.getText();
			textField.setText( new String( "" ) );
			requestSender.sendRequest( text );
		}
	}

	public static void main( String[] args )
	{
		ClientSide frame = new ClientSide( 80, "localhost" );
		frame.setTitle( "Client Program" );
		WindowListener l = new WindowAdapter()
		{
			public void windowClosing( WindowEvent e )
			{
				System.exit( 0 );
			}
		};

		frame.addWindowListener( l );
		frame.pack();
		frame.setVisible( true );
	}
}
