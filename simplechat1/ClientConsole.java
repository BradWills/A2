// This file contains material ;supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;

import client.ChatClient;
import common.*;
import ocsf.client.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version July 2000
 */
public class ClientConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String host, int port) 
  {
    try 
    {
      client= new ChatClient(host, port, this);
    } 
    catch(IOException exception) 
    {
    	// *** Changed for E49 MC	
    	try {
    		client= new ChatClient(host, DEFAULT_PORT, this);
    		System.out.println("changed port number to default port");
    	} catch(IOException exception2) {
    		System.out.println("Error: Can't setup connection!"
                    + " Terminating client.");
    		System.exit(1);
    	}

    }
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
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
        	client.handleMessageFromClientUI(message);
        }

      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }
  // *** Changed for E49 MC
  public void handleCommand(String message) {
	  String[] message_list = message.split(" ", 2);
	  switch(message_list[0]) {
	  	case "#quit":
	  		if(client.isConnected()) {
	  			System.out.println("You must terminate connection to server before quitting");
	  		} else {
	  			client.quit();
	  		}
	  		break;
	  	case "#logoff":
	  		try {
	  			client.closeConnection();
	  		} catch(IOException e) {}
	  		break;
	  	case "#sethost":
	  		if(!client.isConnected()) {
	  			client.setHost(message_list[1]);
	  		} else {
	  			System.out.println("User must be logged off");
	  		}
	  		break;
	  	case "#setport":
	  		if(!client.isConnected()) {
	  			int portNum;
	  			try {
	  				portNum = Integer.parseInt(message_list[1]);
	  				client.setPort(portNum);
	  			} catch (NumberFormatException e) {
		  			System.out.println("invalid port number format");
		  		} 
	  			
	  		} else {
	  			System.out.println("User must be logged off");
	  		}
	  		break;
	  	case "#login":
	  		if(!client.isConnected()) {
	  			try{
	  				client.openConnection();
	  			} catch (IOException e) {
	  				System.out.println("there was problem connecting to the server");
	  			}
	  		} else {
	  			System.out.println("Client cannot be connected for the task to be accomplished");
	  		}
	  		break;
	  	case "#gethost":
	  		System.out.println(client.getHost());
	  		break;
	  	case "#getport":
	  		System.out.println(client.getPort());
	  		break;

	  }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println("> " + message);
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {
    String host = "";
    // *** Changed for E49 MC	
    String string_port = "";
    int port;  
    
    try
    {
      host = args[0];
   // *** Changed for E49 MC
      string_port = args[1];
      port = Integer.parseInt(string_port);

    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      host = "localhost";
      // *** Changed for E49 MC
      port = DEFAULT_PORT;
    }
 // *** Changed for E49 MC
    catch(NumberFormatException e) {
    	port = DEFAULT_PORT;
    }
    ClientConsole chat= new ClientConsole(host, port);
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
