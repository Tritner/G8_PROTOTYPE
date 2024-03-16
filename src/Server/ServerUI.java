package Server;

import javafx.application.Application;
import javafx.stage.Stage;
import java.sql.Statement;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import gui.ServerController;

public class ServerUI extends Application {
	final public static int DEFAULT_PORT = 5555;
	private static Connection con2 = null;
	private static boolean DBup = false;
	private static boolean serverUP = false;
	private static ServerController control;
	private static String msg;
	private static EchoServer sv;

	public static void main(String args[]) throws Exception {
		launch(args);
	} 

	@Override
	public void start(Stage primaryStage) throws Exception {
		control = new ServerController();
		control.start(primaryStage);
	}

	public static void runServer(EchoServer sv)
	{
		if (DBup) {
			ServerUI.sv = sv;
			sv.setCon(con2);
	        try {
	          sv.listen();
	        } catch (Exception ex) {
	        	setMsg("ERROR - could not listen for clients!");
	        }
	        serverUP=true;
		}
		else {
			setMsg("Please Start DB first");
		}
	}
	
	public static void stopServer() {
		try {
			sv.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setMsg("Stoped Server succsefuly");
		serverUP=false;
	}


    public static void connectToDB(ArrayList<String> str) {
        String dburl = "jdbc:mysql://" + str.get(0) + ":" + str.get(1) + "/?serverTimezone=UTC";
        String username = str.get(3);
        String password = str.get(4);
        
        try {
            con2 = DriverManager.getConnection(dburl, username, password);
            
            // Create the database if it doesn't exist
            createDatabaseIfNotExists(str.get(2),dburl, username, password);
            
            // Update connection URL to include the schema
            dburl = "jdbc:mysql://" + str.get(0) + ":" + str.get(1) + "/" + str.get(2) + "?serverTimezone=UTC";
            con2 = DriverManager.getConnection(dburl, username, password);
            
            setMsg("DB " + str.get(2) + " connected successfully on port " + str.get(1));
            DBup = true;
        } catch (SQLException e) {
            setMsg("Could not connect to DB");
            e.printStackTrace();
        }
    }
    //create initial DB that will contain the workers of GoNature
    private static void createDatabaseIfNotExists(String schemaName, String dburl, String username, String password) {
        try (Connection con = DriverManager.getConnection(dburl, username, password)) {
            // Create database if not exists
            try (Statement statement = con.createStatement()) {
                String sql = "CREATE DATABASE IF NOT EXISTS " + schemaName;
                statement.executeUpdate(sql);
            } catch (SQLException e) {
                e.printStackTrace();
                return; // Return if database creation fails to avoid further execution
            }

            // Establish connection to the newly created database
            String dbUrlWithSchema = dburl + "/" + schemaName;

            // Create tables and insert data for 2 Workers [name, id, password, role]
            // The park manager is Vladi and the department manager is Lior
            try (Connection con2 = DriverManager.getConnection(dbUrlWithSchema, username, password);
                 Statement statement = con2.createStatement()) {
                String sql = "CREATE TABLE IF NOT EXISTS workers (name VARCHAR(255), id INT, password VARCHAR(255), role VARCHAR(255), PRIMARY KEY (id))";
                statement.executeUpdate(sql);

                // Insert data
                sql = "INSERT INTO workers (name, id, password, role) VALUES ('Vladi', 1234, '1234', 'Park Manager')";
                statement.executeUpdate(sql);
                sql = "INSERT INTO workers (name, id, password, role) VALUES ('Lior', 4321, '4321', 'Department Manager')";
                statement.executeUpdate(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




	public static void disconnectFromDB() {
		if(serverUP) {
			setMsg("Please Stop server first");
		}
		else {
			try {
				con2.close();			
				if(con2.isClosed())
					System.out.println("closed");
			} catch (SQLException q) {
				q.printStackTrace();
			} catch (Exception e) {}
			setMsg("Disconected from DB succsefuly");
		}
	}

	public static String getMsg() {
		String ret = msg;
		setMsg("");
		return ret;
	}

	public static void setMsg(String msg) {
		ServerUI.msg = msg;
	}
	
	public static boolean isDBup() {
		return DBup;
	}

	public static boolean isServerUP() {
		return serverUP;
	}


}
