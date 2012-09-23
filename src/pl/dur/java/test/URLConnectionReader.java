package pl.dur.java.test;

import java.net.*;
import java.io.*;

public class URLConnectionReader
{
	public static void main( String[] args ) throws Exception
	{
		URL yahoo = new URL( "http://www.yahoo.com/" );
		URLConnection yc = yahoo.openConnection();
		BufferedReader in = new BufferedReader(
				new InputStreamReader(
				yc.getInputStream() ) );
		String inputLine;
		StringBuffer html = new StringBuffer( "");
		while( (inputLine = in.readLine()) != null )
		{
			html.append( inputLine );
		}
		System.out.print(html.toString());
		in.close();
	}
}
