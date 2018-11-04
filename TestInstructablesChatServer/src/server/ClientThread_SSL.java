// place this file the path such ends with: ChatServer_SSL/server/ClientThread.java

package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import javax.net.ssl.*;

public class ClientThread_SSL implements Runnable {
    private SSLSocket socket;
    private PrintWriter clientOut;
    private ChatServer_SSL server;

    public ClientThread_SSL(ChatServer_SSL server, SSLSocket socket){
        this.server = server;
        this.socket = socket;
    }

    private PrintWriter getWriter(){
        return clientOut;
    }

    @Override
    public void run() {
        try{
            // setup
//        	socket.setEnabledCipherSuites(socket.getEnabledCipherSuites());
//        	socket.startHandshake();
            this.clientOut = new PrintWriter(socket.getOutputStream(), false);
            Scanner in = new Scanner(socket.getInputStream());

            // start communicating
            while(!socket.isClosed()){
                if(in.hasNextLine()){
                    String input = in.nextLine();
                    // NOTE: if you want to check server can read input, uncomment next line and check server file console.
                     System.out.println(input);
                    for(ClientThread_SSL thatClient : server.getClients()){
                        PrintWriter thatClientOut = thatClient.getWriter();
                        if(thatClientOut != null){
                            thatClientOut.write(input + "\r\n");
                            thatClientOut.flush();
                        }
                    }
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}