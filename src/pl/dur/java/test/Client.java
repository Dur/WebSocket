/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.*;

/**
 *
 * @author Dur
 */
class Client extends JFrame
        implements ActionListener
{
    JLabel text, clicked;
    JButton button;
    JPanel panel;
    JTextField textField;
    Socket socket = null;
    PrintWriter out = null;
    BufferedReader in = null;
    Integer port;

    Client()
    {
        text = new JLabel("Text to send over socket:");
        textField = new JTextField(20);
        button = new JButton("Click Me");
        button.addActionListener(this);

        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.white);
        getContentPane().add(panel);
        panel.add("North", text);
        panel.add("Center", textField);
        panel.add("South", button);
    }

    public void actionPerformed(ActionEvent event)
    {
        Object source = event.getSource();

        if (source == button)
        {
            String text = textField.getText();
            out.println(text);
            textField.setText(new String(""));
            try
            {
                String line = in.readLine();
                if(line.contains("NP"))
                {
                    line.substring(3);
                    port = Integer.parseInt(line);
                    socket = new Socket("localhost", port.intValue());
                }
                System.out.println("Text received :" + line);
            }
            catch (IOException e)
            {
                System.out.println("Read failed");
                System.exit(1);
            }
        }
    }

    public void listenSocket()
    {
        try
        {
            socket = new Socket("localhost", 80);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = in.readLine();
            System.out.println("Text received :" + line);
                if(line.contains("NP"))
                {
                    line = line.substring(3);
                    port = Integer.parseInt(line);
                    socket = new Socket("localhost", port.intValue());
                }
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (UnknownHostException e)
        {
            System.out.println("Unknown host");
            System.exit(1);
        }
        catch (IOException e)
        {
            System.out.println("No I/O");
            System.exit(1);
        }
    }

//    public static void main(String[] args)
//    {
//        Client frame = new Client();
//        frame.setTitle("Client Program");
//        WindowListener l = new WindowAdapter()
//        {
//            public void windowClosing(WindowEvent e)
//            {
//                System.exit(0);
//            }
//
//        };
//
//        frame.addWindowListener(l);
//        frame.pack();
//        frame.setVisible(true);
//        frame.listenSocket();
//    }

}