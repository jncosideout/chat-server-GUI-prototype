// place this file the path such ends with: ChatServer/client/ServerThread.java

package client;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JTextArea;
import javax.swing.SwingWorker;

public class ServerThread extends SwingWorker<Void, String> {
    private Socket socket;
    protected JTextArea ta; 
    
    public ServerThread(Socket socket, JTextArea ta){
        this.socket = socket;
        this.ta = ta;
    }

	@Override
	protected Void doInBackground() throws Exception {

	        try{
	            InputStream serverInStream = socket.getInputStream();
	            Scanner serverIn = new Scanner(serverInStream);

	            while(!socket.isClosed()){
	                if(serverInStream.available() > 0){
	                    if(serverIn.hasNextLine()){
	                    	publish(serverIn.nextLine());
	                    }
	                }
	             
	            }
	            serverIn.close();
	        }
	        catch(IOException ex){
	            ex.printStackTrace();
	        }

		return null;
	}
	
	@Override
	protected void process(List<String> chunks) {
        for (String response : chunks) {
            ta.append(response + "\u23CE\n");
        }
	}
}