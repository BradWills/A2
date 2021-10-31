import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import client.ChatClient;
import common.ChatIF;

// *** Changed for E49 MC


public class ServerConsole implements ChatIF {
	EchoServer server;
	
	  public ServerConsole(EchoServer server) 
	  {
	      this.server= server;
	  }
	
	@Override
	public void display(String message) {
		// TODO Auto-generated method stub
		System.out.println("SERVER MSG> " + message);
	}
	
	// waits for and Reads inputs from server console
	  public void accept() 
	  {
	    try
	    {
	      BufferedReader fromConsole = 
	        new BufferedReader(new InputStreamReader(System.in));
	      String message;
	
	      while (true) 
	      {
	        message = fromConsole.readLine();
	        // *** Changed for E49 MC
	        if(message.charAt(0)=='#') {
	        	handleCommand(message);
	        } else {
	        	display(message);
	        }
	
	      }
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println
	        ("Unexpected error while reading from console!");
	    }
	  }
	  
	  public void handleCommand(String message) {
		  String[] message_list = message.split(" ", 2);
		  switch(message_list[0]) {
		  	case "#quit":
		  		try{
		  			server.close();
		  		} catch (IOException e) {
		  			System.out.println("there was a problem closing server");
		  		}
		  		System.exit(0);
		  		break;
		  	case "#stop":
		  		server.stopListening();
		  		break;
		  	case "#close":
		  		try{
		  			server.close();
		  		} catch (IOException e) {
		  			System.out.println("there was a problem closing server");
		  		}
		  		break;
		  	case "#setport":
		  		if(!server.isListening()) {
		  			int portNum;
		  			try {
		  				portNum = Integer.parseInt(message_list[1]);
		  				server.setPort(portNum);
		  			} catch (NumberFormatException e) {
			  			System.out.println("invalid port number format");
			  		} 
		  			
		  		} else {
		  			System.out.println("Server must be closed");
		  		}
		  		break;
		  	case "#start":
		  		if(!server.isListening()) {
		  			try{
		  				server.listen();
		  			} catch (IOException e) {
		  				System.out.println("there was problem while attempting to listen to clients");
		  			}
		  		} else {
		  			System.out.println("Server must be closed");
		  		}
		  		break;

		  	case "#getport":
		  		System.out.println(server.getPort());
		  		break;

		  }
	  }

}
