package pl.dur.java.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import pl.dur.java.actions.EchoAction;
import pl.dur.java.components.register.ClientComponentsRegister;
import pl.dur.java.dispatchers.Dispatcher;
import pl.dur.java.events.mappers.EventMapper;
import pl.dur.java.actions.NewPortAction;
import pl.dur.java.messages.Message;

/**
 *
 * @author Dur
 */
class ClientSideView extends JFrame implements ActionListener
{
	private JLabel text, clicked;
	private JButton button;
	private JButton webSocketPortOpener;
	private JTextField portNumberTextField;
	private JPanel panel;
	private int newDataListenerPort;
	private JTextField textField;
	private ClientSocketAdmin requestSender = null;
	private NewDataListener serverState = null;
	private static int MAX_PORT_NUM = 65000;
	private EventMapper eventMapper;
	private ArrayBlockingQueue<Message> actions = new ArrayBlockingQueue<Message>( 10 );
	Dispatcher dispatcher = null;

	ClientSideView( int portNum, String host )
	{
		eventMapper = new EventMapper( new ClientActionConfigurator().getConfigurator());
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
					webSocketPort = Integer.parseInt( portNumberTextField.getText() );
				}
				catch( NumberFormatException ex )
				{
					ex.printStackTrace();
				}
				if( webSocketPort != 0 )
				{
					HashMap<String, Object> params = new HashMap<String, Object>();
					params.put("WEBSOCKET_PORT", webSocketPort);
					requestSender.sendToServer( new Message( "WS", params ) );
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

//		serverState = new NewDataListener( newDataListenerPort );
//		Thread serverStateThread = new Thread( serverState );
//		serverStateThread.start();

		requestSender = new ClientSocketAdmin( actions, portNum, host, 10 );
		dispatcher = new Dispatcher( actions, eventMapper );
		Thread dispatcherThread = new Thread(dispatcher);
		dispatcherThread.start();
		Thread requestSenderThread = new Thread( requestSender );
		requestSenderThread.start();
		ClientComponentsRegister.addComponent( "SOCKET_ADMIN", requestSender);

//		NewDataListener newDataListener = new NewDataListener( 10045 );
//		Thread newDataListenerThread = new Thread( newDataListener );
//		newDataListenerThread.start();
//		System.out.println( "after initialisation" );
	}

	public void actionPerformed( ActionEvent event )
	{
		Object source = event.getSource();

		if( source == button )
		{
			String text = textField.getText();
			textField.setText( new String( "" ) );
			requestSender.sendToServer( new Message( text, new HashMap<String, Object>() ) );
		}
	}

	public static void main( String[] args )
	{
		ClientSideView frame = new ClientSideView( 80, "localhost" );
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
