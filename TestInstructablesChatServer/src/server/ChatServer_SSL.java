// place this file the path such ends with: ChatServer/server/ChatServer.java

package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.*;

public class ChatServer_SSL {

    private static final int portNumber = 4444;

    private int serverPort;
    private List<ClientThread_SSL> clients; // or "protected static List<ClientThread_SSL> clients;"

    public static void main(String[] args){
        ChatServer_SSL server = new ChatServer_SSL(portNumber);
        server.startServer();
    }

    public ChatServer_SSL(int portNumber){
        this.serverPort = portNumber;
    }

    public List<ClientThread_SSL> getClients(){
        return clients;
    }

    private void startServer(){
        clients = new ArrayList<ClientThread_SSL>();
        SSLServerSocketFactory sslSrvFact =
        		(SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        SSLServerSocket serverSocket = null;
        
        try {
            serverSocket = (SSLServerSocket) sslSrvFact.createServerSocket(serverPort);
            serverSocket.setEnabledCipherSuites(serverSocket.getSupportedCipherSuites());
            acceptClients(serverSocket);
        } catch (IOException e){
            System.err.println("Could not listen on port: "+serverPort);
            System.exit(1);
        }
    }

    private void acceptClients(ServerSocket serverSocket){

        System.out.println("server starts port = " + serverSocket.getLocalSocketAddress());
        while(true){
            try{
                SSLSocket socket = (SSLSocket) serverSocket.accept();
                System.out.println("accepts : " + socket.getRemoteSocketAddress());
                socket.startHandshake();
                ClientThread_SSL client = new ClientThread_SSL(this, socket);
                Thread thread = new Thread(client);
                thread.start();
                clients.add(client);
            } catch (IOException ex){
                System.out.println("Accept failed on : "+serverPort);
            }
        }
    }
}