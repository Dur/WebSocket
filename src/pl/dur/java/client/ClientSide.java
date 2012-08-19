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
	JPanel panel;
	int newDataListenerPort;
	JTextField textField;
	PrintWriter out = null;
	RequestSender requestSender = null;
	NewDataListener serverState = null;
	static int MAX_PORT_NUM = 65000;

	ClientSide( int portNum, String host )
	{
		text = new JLabel( "Text to send over socket:" );
		textField = new JTextField( 20 );
		button = new JButton( "Click Me" );
		button.addActionListener( this );
		panel = new JPanel();
		panel.setLayout( new BorderLayout() );
		panel.setBackground( Color.white );
		getContentPane().add( panel );
		panel.add( "North", text );
		panel.add( "Center", textField );
		panel.add( "South", button );
		
		
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
			out.println( text );
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