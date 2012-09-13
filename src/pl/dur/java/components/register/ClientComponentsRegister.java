/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.components.register;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Dur
 */
public class ClientComponentsRegister
{
	static private HashMap<String, Object> components = new HashMap<String, Object>();

	public static Object getComponent( String name )
	{
		return components.get( name );
	}

	public static void addComponent( String name, Object component )
	{
		components.put( name, component );
	}

	public static void addCollectionOfComponents( Map<String, Object> collection )
	{
		components.putAll( collection );
	}
}
