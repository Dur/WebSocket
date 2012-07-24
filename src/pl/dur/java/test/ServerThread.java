/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Dur
 */
public class ServerThread implements Runnable
{
    int socketNumber;
    ServerSocket server = null;
    Socket client = null;
    BufferedReader in = null;
    PrintWriter out = null;

    public ServerThread(int socketNumber)
    {
        this.socketNumber = socketNumber;
    }

    @Override
    public void run()
    {
        try
        {
            server = new ServerSocket(socketNumber);
        }
        catch (IOException e)
        {
            System.out.println("Could not listen on port " + socketNumber);
            System.exit(-1);
        }

        try
        {
            client = server.accept();
        }
        catch (IOException e)
        {
            System.out.println("Accept failed:" + socketNumber);
            System.exit(-1);
        }
        try
        {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
            System.out.println("Message on socket " + socketNumber + " " + client.getInputStream().toString());
            out.print("Message received on port " + socketNumber);
        }
        catch (Exception ex)
        {
            System.out.println("Zjebał się odczyt na porcie " + socketNumber);
        }


    }

}
