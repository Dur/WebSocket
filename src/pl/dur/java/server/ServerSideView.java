/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JPanel;

/**
 *
 * @author Dur
 */
public class ServerSideView extends JFrame implements ActionListener
{
	private final JButton button;
	private final JPanel panel;
	private JTextArea textArea = new JTextArea();
	private JTextField textField;
	private final ServerSide serverSide;

	public ServerSideView()
	{
		button = new JButton( "Click Me" );
		button.addActionListener( this );
		panel = new JPanel();
		panel.setLayout( new BorderLayout() );
		panel.setBackground( Color.white );
		getContentPane().add( panel );
		panel.add( "Center", textArea );
		panel.add( "South", button );
		serverSide = new ServerSide();
	}

	@Override
	public void actionPerformed( ActionEvent event )
	{
		Object source = event.getSource();

		if( source == button )
		{
			try
			{
				System.out.println( "state changed" );
			}
			catch( Exception ex )
			{
				ex.printStackTrace();
			}
		}
	}

	protected void finalize()
	{
	}

	public static void main( String args[] )
	{
		ServerSideView serverSideView = new ServerSideView();
		serverSideView.setTitle( "Server Program" );
		WindowListener l = new WindowAdapter()
		{
			public void windowClosing( WindowEvent e )
			{
				System.exit( 0 );
			}
		};
		serverSideView.addWindowListener( l );
		serverSideView.pack();
		serverSideView.setVisible( true );
	}
}
