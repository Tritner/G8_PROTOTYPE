// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package Server;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import gui.ServerController;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */

public class EchoServer extends AbstractServer {
	private static Connection con;
	private static ServerController control;
	// Class variables *************************************************

	/**
	 * The default port to listen on.
	 */
	// final public static int DEFAULT_PORT = 5555;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port The port number to connect on.
	 * 
	 */

	public EchoServer(int port, Connection con, ServerController control) {
		super(port);
		EchoServer.con = con;
		EchoServer.control = control;
	}
	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg    The message received from the client.
	 * @param client The connection from which the message originated.
	 * @param
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		try {
			@SuppressWarnings("unchecked")
			ArrayList<String> command = (ArrayList<String>) msg;
			
			Statement stmt = con.createStatement();
			if (command.contains("select *")) {
				ResultSet rs = stmt.executeQuery("SELECT * FROM project.order");
				ArrayList<ArrayList<String>> mega = new ArrayList<ArrayList<String>>();
				while (rs.next()) {
				//if (rs.next()) {
					ArrayList<String> res = new ArrayList<String>();
					//res.add("row");
					res.add(rs.getString(1));
					res.add(rs.getString(2));
					res.add(rs.getString(3));
					res.add(rs.getString(4));
					res.add(rs.getString(5));
					res.add(rs.getString(6));
					mega.add(res);
				}
					try {
						client.sendToClient(mega);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				rs.close();
			} else if(command.contains("disconn")){
				sendToGui("Client disconneted: "+ client.toString() +" "+ this.getPort());
//				try {
//					client.sendToClient("bye bye");
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
			}
			else {
				
				
				PreparedStatement ps = con.prepareStatement("UPDATE project.orders SET "+command.get(2)+" = ? WHERE (orderNumber = ?)");
				System.out.println(command.get(0) + command.get(1));
				ps.setString(1, command.get(1));
				ps.setString(2, command.get(0));
				int lineChanged = ps.executeUpdate();
				try {
					if(lineChanged>0)
						client.sendToClient("updated "+ command.get(2)+" of "+command.get(0)+" to "+command.get(1));
					else
						client.sendToClient("error: could not update " + command.get(2));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ps.close();
			}
			sendToGui("Message received: " + command + " from " + client);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		sendToGui("GoNature Server listening clients at port number: " + this.getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		sendToGui("Server has stopped listening for connections.");
	}
	@Override
	protected void clientConnected(ConnectionToClient client) {
		sendToGui("New Client conneted: "+ client.toString() +" "+ this.getPort());
	}
	
	public static void sendToGui(String str) {
		control.logIt(str);
	}
	
	public void setCon(Connection con) {
		EchoServer.con = con;
	}
	
	public static String handleMessageFromLogin(String data) throws SQLException {
	    // Handle the received login data
	    // For now, just print it to the console
	    System.out.println("Received login data: " + data);

	    // Here you can perform the database lookup and return the appropriate response
	    // For now, let's assume the response is hardcoded
	    String foundOrNot = "user_not_found";
	    if (data.contains("makeorder")) {
	        // Split the sentence into an array of words
	        String[] userData = data.split(" ");
	        // Assuming the sentence contains exactly 3 words
	        String username = userData[1];
	        String password = userData[2];
	        PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE user = ? AND password = ?");
	        ps.setString(1, username);
	        ps.setString(2, password);
	        ResultSet resultSet = ps.executeQuery();
	        if (resultSet.next()) {
	            // User found
	            foundOrNot = "userFound";
	        }
	        // Close resources
	        resultSet.close();
	        ps.close();
	    } else if (data.contains("handleorder")) {
	        // Simulate successful login for handleorder
	        return "success";
	    }

	    // Return an appropriate response based on the database lookup
	    return foundOrNot;
	}

	
}