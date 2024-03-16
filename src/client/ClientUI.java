package client;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;
import gui.ClientController;

public class ClientUI extends Application {
	private static ChatClient client;

	public static void main(String args[]) throws Exception {
		launch(args);
	} // end main

	@Override
	public void start(Stage primaryStage) throws Exception {
		ClientController aFrame = new ClientController(); // create StudentFrame
		try {
			client = new ChatClient("localhost", 5555, aFrame);
		} catch (IOException exception) {
			System.out.println("Error: Can't setup connection! Terminating client.");
			System.exit(1);
		}
		aFrame.start(primaryStage);
	}
	
	public static void sendShowAll() {
    	getClient().handleMessageFromClientUI("show");
	}

	public static void update(String who, String what, String change) {
		getClient().handleMessageFromClientUI("update " + who + " " + what +" " + change);
	}

	public static ChatClient getClient() {
		return client;
	}
}
