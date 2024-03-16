// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import gui.ClientController;

import java.io.*;
import java.util.ArrayList;

/**
 * This class overrides some of the methods defined in the abstract superclass
 * in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient {
	// Instance variables **********************************************

	/**
	 * The interface type variable. It allows the implementation of the display
	 * method in the client.
	 */
	// ChatIF clientUI;
	// private static ClientController control;
	private boolean awaitResponse;
	private static ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
	private static String feedback;

	// public static Student s1 = new Student(null, null, null, new Faculty(null,
	// null));

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param host     The server to connect to.
	 * @param port     The port number to connect on.
	 * @param clientUI The interface type variable.
	 */

	public ChatClient(String host, int port, ClientController control2) throws IOException {
		super(host, port); // Call the superclass constructor
		// control = control2;
		System.out.println("the Client is connected to server " + host + " over port " + port);
		connect();
		// openConnection();
	}

	// Instance methods ************************************************

	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg The message from the server.
	 */
	public void handleMessageFromServer(Object msg) {
		feedback = "";
		awaitResponse = false;
		data.clear();
		if (msg instanceof ArrayList<?>) {
			@SuppressWarnings("unchecked")
			ArrayList<ArrayList<String>> got = (ArrayList<ArrayList<String>>) msg;
			// data.addAll(got);
			setData(got);
			System.out.println("got data");
			feedback = "positive";
		} else {
			String answer = (String) msg;
			System.out.println("--> handleMessageFromServer");
			System.out.println(answer);
			if (!answer.contains("error"))
				feedback = "positive";
		}
		/// add send to GUI

	}

	/**
	 * This method handles all data coming from the UI
	 *
	 * @param message The message from the UI.
	 */

	public void handleMessageFromClientUI(String message) {
		feedback = "";
		ArrayList<String> command = new ArrayList<String>();
		if (message.contains("update")) {
			String str[] = message.split(" ");
			command.add(str[1]);
			command.add(str[2]);
			command.add(str[3]);
			System.out.println(command);
		} else if (message.equals("show")) {
			command.add("select *");
		}
		try {
			sendToServer(command);
		} catch (IOException e) {
			System.out.println("Could not send message to server.  Terminating client.");
			quit();
		}
		awaitResponse = true;
		while (awaitResponse) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (!feedback.equals("positive")) {
			System.out.println("something went wrong");
		}

	}

	public void connect() {
		try {
			openConnection();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // in order to send more than one message
	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
			System.out.println("did not closed connection");
		}
		System.exit(0);
	}

	public static ArrayList<ArrayList<String>> getData() {
		return data;
	}

	public static void setData(ArrayList<ArrayList<String>> data) {
		ChatClient.data = data;
	}

	public static String getFeedback() {
		return feedback;
	}

	public static void setFeedback(String feedback) {
		ChatClient.feedback = feedback;
	}
}
//End of ChatClient class
