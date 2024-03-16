package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import client.*;
import common.Order;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ClientController implements Initializable {
	// private ChatClient client;
	public static int DEFAULT_PORT;

	@FXML
	private Button updateBtn;

	@FXML
	private Button showBtn;

	@FXML
	private TextField changeparkName;

	@FXML
	private TextField changetelephoneNumber;

	@FXML
	private TextField orderNumber1;

	@FXML
	private TableView<Order> OrdersTable;

	@FXML
	private TableColumn<Order, String> parkName;

	@FXML
	private TableColumn<Order, String> orderNumber;

	@FXML
	private TableColumn<Order, String> timeOfVisit;

	@FXML
	private TableColumn<Order, String> numOfVisitors;

	@FXML
	private TableColumn<Order, String> Email;

	@FXML
	private TableColumn<Order, String> phoneNum;

	@FXML
	void show(ActionEvent event) {
		OrdersTable.getItems().clear();
		ClientUI.sendShowAll();
		addData(ChatClient.getData());
	}

	@FXML
    void Update(ActionEvent event) {
    	if(orderNumber.getText().equals("")) {
    		popUp("Error","missing order number","order number is missing please try again.");
    	}
    	
    	//can be resolved with a function
    	else {
    		if(!changeparkName.getText().equals("")) 
    			ClientUI.update(orderNumber1.getText(), changeparkName.getText(),"parkName");	
    		if (!changetelephoneNumber.getText().equals("")) {
    			ClientUI.update(orderNumber1.getText(), changetelephoneNumber.getText(),"telephoneNumber");
    		}
    		
    	}
		//// change to the new function update
		
		if (ChatClient.getFeedback().equals("positive"))
			ClientUI.sendShowAll();
		else {
			System.out.println("could not update data");
			popUp("Error", "data was Not Updated", "please check if the order number entered was correct ");
		}
	}

	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/Client.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("GoNature Client");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				ArrayList<String> byeBye = new ArrayList<String>();
				byeBye.add("disconn");
				try {
					ClientUI.getClient().sendToServer(byeBye);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Platform.exit();
				System.exit(0);
				System.out.println("client disconnected");
			}
		});
	}

	public void addData(ArrayList<ArrayList<String>> al) {
		for (ArrayList<String> arrayList : al) {
			addRow(arrayList);
		}
	}

	public void addRow(ArrayList<String> al) {
		OrdersTable.getItems().add(new Order(al));
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	    parkName.setCellValueFactory(new PropertyValueFactory<>("parkName"));
	    orderNumber.setCellValueFactory(new PropertyValueFactory<>("orderNumber"));
	    timeOfVisit.setCellValueFactory(new PropertyValueFactory<>("timeOfVisit"));
	    numOfVisitors.setCellValueFactory(new PropertyValueFactory<>("numOfVisitors"));
	    phoneNum.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
	    Email.setCellValueFactory(new PropertyValueFactory<>("Email"));
	}

	public void popUp(String title, String header, String content) { // popup for errors
		Alert alert = new Alert(Alert.AlertType.ERROR); // create alert
		alert.getDialogPane().setMaxSize(350, 150);
		alert.setTitle(title);
		alert.setHeaderText("");
		alert.setContentText(header + "\n" + content);
		alert.show(); // show alert
	}

}