package controllers;

import domains.User;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import observer.applicationObserver;
import persistance.PersistanceException;
import service.IService;

import java.io.IOException;

public class ControllerLogin {
    IService service;
    private String user;
    private Stage loginStage;

    @FXML
    TextField usernameField;

    @FXML
    public AnchorPane pane;

    @FXML
    PasswordField passwordField;

    private Alert eroare;

    private ControllerMainApp appController;
    Stage mainStage;

    public void setService(Stage loginStage, Stage mainStage, IService service, ControllerMainApp appController) {
        this.loginStage = loginStage;
        this.service = service;
        this.mainStage = mainStage;
        this.appController = appController;
        final BooleanProperty firstTime = new SimpleBooleanProperty(true);
        pane = new AnchorPane();
        usernameField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && firstTime.get()) {
                pane.requestFocus(); // Delegate the focus to container
                firstTime.setValue(false); // Variable value changed for future references
            }
        });
    }

    @FXML
    public void signInAction() throws IOException {
        eroare = new Alert(Alert.AlertType.ERROR);
        eroare.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        if (!usernameField.getText().isEmpty() && !passwordField.getText().isEmpty()){
            String username = usernameField.getText();
            String password = passwordField.getText();

            try {
                service.login(new User(0, username, password), appController);
                System.out.println("login succes");
                user = usernameField.getText();
                loginStage.close();
                appController.set( mainStage,service,this);
                appController.setTable();
                mainStage.setTitle("Meniu");
                mainStage.show();


            } catch (PersistanceException ex) {
                ex.printStackTrace();
                eroare.setContentText("Date gresite! Username sau parola incorecte!");
                eroare.showAndWait();
            }
        } else {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Wrong data!",
                    "Username and password are not supposed to be null!");
        }
    }

    public String getUser() {
        return user;

    }

    @FXML
    public void clear() {
        usernameField.clear();
        passwordField.clear();
        pane.requestFocus();
    }
}
