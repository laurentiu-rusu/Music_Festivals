package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import services.ServiceConcert;
import services.ServiceTicket;
import services.ServiceUser;

import java.io.IOException;

public class ControllerLogin {
    private ServiceUser serviceUser;
    private ServiceConcert serviceConcert;
    private ServiceTicket serviceTicket;
    private Stage loginStage;

    @FXML
    TextField usernameField;

    @FXML
    PasswordField passwordField;

    public void setServices(Stage loginStage, ServiceUser serviceUser, ServiceConcert serviceConcert, ServiceTicket serviceTicket) {
        this.loginStage = loginStage;
        this.serviceUser = serviceUser;
        this.serviceConcert = serviceConcert;
        this.serviceTicket = serviceTicket;
    }

    @FXML
    public void signInAction() throws IOException {

        if (!usernameField.getText().isEmpty() && !passwordField.getText().isEmpty()){
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (serviceUser.checkLoginInfo(username, password)) {
                loginStage.close();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/mainView.fxml"));
                AnchorPane root = loader.load();

                Scene scene = new Scene(root);
                Stage stage = new Stage();
                ControllerMainApp ctrl = loader.getController();
                ctrl.setService(stage, serviceConcert, serviceTicket, serviceUser);
                stage.setScene(scene);
                stage.setTitle("Main");
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
