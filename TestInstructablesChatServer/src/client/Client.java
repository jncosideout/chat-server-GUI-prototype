// place this file the path such ends with: ChatServer/client/Client.java

package client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultCaret;


public class Client implements ActionListener, Runnable {

    private static final String host = "localhost";
    private static final int portNumber = 4444;

    private String userName = null;
    private String serverHost;
    private int serverPort;
    
    private final JFrame f = new JFrame();
    private final JTextField tf = new JTextField(25);
    protected final JTextArea ta = new JTextArea(15, 25);
    private final JButton send = new JButton("Send");
    private Thread thread;
    PrintWriter serverOut;
    
    public static void main(String[] args){
    	SwingUtilities.invokeLater(new Runnable() {
             //@Override
             public void run() {
                 new Client(host, portNumber).startClient();
             }
         });
    }
    
    public void readName () {
         display("Please input username:");
         SwingUtilities.invokeLater(new Runnable() {
        	   
        	   @Override
        	   public void run() {
              	 userName = tf.getText();
//	             while(userName == null || userName.trim().equals("")){
//	               // null, empty, whitespace(s) not allowed.
//	          	 userName = tf.getText();
//		               if(userName.trim().equals("")){
//		                   display("Invalid. Please enter again:");
//		               }
//		           }
        	   }
        	  });

    }

    private Client(String host, int portNumber){
        this.serverHost = host;
        this.serverPort = portNumber;
        
        f.setTitle("Echo Client");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getRootPane().setDefaultButton(send);
        f.add(tf, BorderLayout.NORTH);
        f.add(new JScrollPane(ta), BorderLayout.CENTER);
        f.add(send, BorderLayout.SOUTH);
        f.pack();
        send.addActionListener(this);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        DefaultCaret caret = (DefaultCaret) ta.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        display("Trying " + host + " on port " + portNumber);
        thread = new Thread(this);
    }
    
    public void startClient() {
        f.setVisible(true);
        thread.start();
    }

    public void run(){
        try{
        	readName();
            Socket socket = new Socket(serverHost, serverPort);
            Thread.sleep(1000); // waiting for network communicating.
            serverOut = new PrintWriter(socket.getOutputStream(), false);
            display("Welcome :" + userName);
            display("Local Port :" + socket.getLocalPort());
	        display("Server = " + socket.getRemoteSocketAddress() + ":" + socket.getPort());
            ServerThread serverThread = new ServerThread(socket, ta);
            serverThread.execute();
 
        }catch(IOException ex){
            System.err.println("Fatal Connection error!");
            ex.printStackTrace();
        }catch(InterruptedException ex){
            System.out.println("Interrupted");
        }
    
    }
    
    //@Override
    public void actionPerformed(ActionEvent ae) {
        String s = tf.getText();
        if (serverOut != null) {
            serverOut.println(userName + " > " + s);
            serverOut.flush();
        }
        tf.setText("");
    }
    
    private void display(final String s) {
    	SwingUtilities.invokeLater(new Runnable() {
            //@Override
            public void run() {
                ta.append(s + "\n");
            }
        });
    }
    
}//eoc
    
    