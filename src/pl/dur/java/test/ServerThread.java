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
public class ServerThread implements Runnable {

    int socketNumber;
    ServerSocket server = null;
    Socket client = null;
    BufferedReader in = null;
    PrintWriter out = null;
    String line;

    public ServerThread(int socketNumber) {
        this.socketNumber = socketNumber;
    }

    @Override
    public void run() {
        System.out.println("ServerThread " + socketNumber);
        try {
            server = new ServerSocket(socketNumber);
        } catch (IOException e) {
            System.out.println("Could not listen on port " + socketNumber);
            System.exit(-1);
        }

        try {
            client = server.accept();
        } catch (IOException e) {
            System.out.println("Accept failed:" + socketNumber);
            System.exit(-1);
        }
        try
        {
             in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        while ( ! server.isClosed())
        {
            try
            {
                line = in.readLine();
                if(line.contains("401"))
                {
                    server.close();
                }
                out.println("Message OK");
            }
            catch (Exception e)
            {
                System.out.println("Read failed");
                System.exit(-1);
            }
        }
        System.out.println("Polaczenie zakonczone");
    }
}
