package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import services.ServiceUser;

import java.io.IOException;

public class ControllerLogin {
    ServiceUser serviceUser;

    @FXML
    TextField usernameField;

    @FXML
    PasswordField passwordField;

    public void setServices(ServiceUser serviceUser) {
        this.serviceUser = serviceUser;
    }

    @FXML
    public void signInAction() throws IOException {

        if (!usernameField.getText().isEmpty() && !passwordField.getText().isEmpty()){
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (serviceUser.checkLoginInfo(username, password)) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/mainView.fxml"));
                AnchorPane root=loader.load();

//                MainController ctrl=loader.getController();
//                ctrl.setService(ticketService,showService,buyerService,artistService);

                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            } else {
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Wrong data!",
                        "Username or password are incorrect!");
            }
        } else {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Wrong data!",
                    "Username and password are not supposed to be null!");
        }
    }
}
