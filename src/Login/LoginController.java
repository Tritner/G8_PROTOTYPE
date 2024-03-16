package Login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Server.EchoServer;

import java.sql.SQLException;
import java.util.Arrays;

public class LoginController {

    @FXML
    private TextField txtfield1;

    @FXML
    private TextField txtfield2;

    @FXML
    private TextField txtfield3;

    @FXML
    private TextField txtfield4;

    @FXML
    private Button loginButton;

    @FXML
    private Button closeButton;

    @FXML
    private void handleCloseButton(ActionEvent event) {
        // Get the stage from the Close button
        Stage stage = (Stage) closeButton.getScene().getWindow();
        // Close the stage
        stage.close();
    }

    @FXML
    private void handleLoginButton(ActionEvent event) throws SQLException {
        // Get the text from the text fields
        String makeOrderID = txtfield1.getText();
        String makeOrderPassword = txtfield2.getText();
        String handleOrderID = txtfield3.getText();
        String handleOrderPassword = txtfield4.getText();

        // Create arrays for the filled fields
        String[] makeOrder = {makeOrderID, makeOrderPassword};
        String[] handleOrder = {handleOrderID, handleOrderPassword};

        // Check which array is filled and construct the appropriate string
        String dataToSend;
        if (Arrays.stream(makeOrder).allMatch(s -> s != null && !s.isEmpty())) {
            dataToSend = "makeorder " + Arrays.toString(makeOrder);
        } else if (Arrays.stream(handleOrder).allMatch(s -> s != null && !s.isEmpty())) {
            dataToSend = "handleorder " + Arrays.toString(handleOrder);
        } else {
            // No data was filled
            System.out.println("No data filled.");
            return;
        }

        // Send the data to the EchoServer
        String response = EchoServer.handleMessageFromLogin(dataToSend);

        // Check the response from the server
        if (response.equals("user_not_found")) {
            // Show alert message if user not found
            showAlert("User Not Found", "The specified user was not found in the database. please try again");
        } else {
// אחרי בדיקה בדאטה בייס מצאנו משתמש ומפה להמשיך 
        }

        System.out.println("Login data sent to server.");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
