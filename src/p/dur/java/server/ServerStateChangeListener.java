/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package p.dur.java.server;

/**
 *
 * @author Dur
 */
public class ServerStateChangeListener implements Runnable
{
	@Override
	public synchronized void run()
	{
		while( true )
		{
			try
			{
				this.wait();
			}
			catch( Exception ex )
			{
				ex.printStackTrace();
			}
			System.out.println( "SERVER STATE CHANGED" );
		}
	}
	
	public synchronized void serverStateChanged()
	{
		this.notify();
	}
}
