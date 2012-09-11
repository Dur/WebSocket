/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.messages;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author Dur
 */
public class Message implements Serializable
{
	static final long serialVersionUID = 42L;
	private final String request;
	private final HashMap<String,Object> params;

	public Message( String request, HashMap<String, Object> params )
	{
		this.params = params;
		this.request = request;
	}

	public HashMap<String, Object> getParams()
	{
		if( this.params != null )
		{
			return params;
		}
		else
		{
			return null;
		}
	}

	public String getRequest()
	{
		if( request != null )
		{
			return request;
		}
		else
		{
			return "";
		}
	}
}
