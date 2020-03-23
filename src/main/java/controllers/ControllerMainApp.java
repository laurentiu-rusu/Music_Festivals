package controllers;

import domains.Concert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import services.ServiceConcert;
import services.ServiceTicket;
import services.ServiceUser;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ControllerMainApp {
    private ObservableList<Concert> modelGrade = FXCollections.observableArrayList();
    private ObservableList<Concert> artists = FXCollections.observableArrayList();
    private ServiceConcert serviceConcert;
    private ServiceTicket serviceTicket;
    private ServiceUser serviceUser;
    private Stage mainStage;

    @FXML
    TableView showsTable;
    @FXML
    TableView artistTable;

    @FXML
    TableColumn aArtistColumn;
    @FXML
    TableColumn aLocationColumn;
    @FXML
    TableColumn aEmptyColumn;
    @FXML
    TableColumn aHourColumn;

    @FXML
    TableColumn sArtistColumn;
    @FXML
    TableColumn sLocationColumn;
    @FXML
    TableColumn sDateColumn;
    @FXML
    TableColumn sTakenColumn;
    @FXML
    TableColumn sEmptyColumn;
    @FXML
    TableColumn sIdColumn;

    @FXML
    TextField wantedSeatsField;
    @FXML
    TextField buyerNameField;

    @FXML
    DatePicker calendar;

    public void setService(Stage mainStage, ServiceConcert serviceConcert, ServiceTicket serviceTicket, ServiceUser serviceUser) {
        this.mainStage = mainStage;
        this.serviceConcert = serviceConcert;
        this.serviceTicket = serviceTicket;
        this.serviceUser = serviceUser;
        calendar.setValue(LocalDate.now());
        modelGrade.setAll(serviceConcert.findAll());
    }

    public void initialize() {
        sIdColumn.setCellValueFactory(new PropertyValueFactory<Concert,Integer>("id"));
        sArtistColumn.setCellValueFactory(new PropertyValueFactory<Concert, String>("name"));
        sLocationColumn.setCellValueFactory(new PropertyValueFactory<Concert, String>("place"));
        sDateColumn.setCellValueFactory(new PropertyValueFactory<Concert, LocalDate>("date"));
        sTakenColumn.setCellValueFactory(new PropertyValueFactory<Concert, Integer>("takenSeats"));
        sEmptyColumn.setCellValueFactory(new PropertyValueFactory<Concert, Integer>("emptySeats"));
        showsTable.setItems(modelGrade);
        showsTable.setRowFactory(tv -> new TableRow<Concert>() {
            @Override
            protected void updateItem(Concert item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null)
                    setStyle("");
                else if (item.getEmptySeats()== 0)
                    setStyle("-fx-background-color: red;");
                else
                    setStyle("");
            }
        });
    }

    public void searchAction(){
        List<Concert> concerts = serviceConcert.findAll();
        LocalDate data = calendar.getValue();
        List<Concert> rez=new ArrayList<>();
        for (Concert concert : concerts){
            if (concert.getDate().isEqual(data))
            {
                rez.add(concert);
            }
        }
        artists.setAll(rez);
        populateArtistTable();
    }

    private void populateArtistTable(){
        aArtistColumn.setCellValueFactory(new PropertyValueFactory<Concert, String>("name"));
        aLocationColumn.setCellValueFactory(new PropertyValueFactory<Concert, String>("place"));
        aEmptyColumn.setCellValueFactory(new PropertyValueFactory<Concert, Integer>("emptySeats"));
        aHourColumn.setCellValueFactory(new PropertyValueFactory<Concert,String>("time"));
        artistTable.setItems(artists);
    }

    public void sellAction(){
        if (showsTable.getSelectionModel().isEmpty()) {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Warning",
                    "You have to choose a concert!");
        } else {
            Concert selectedConcert = (Concert) showsTable.getSelectionModel().getSelectedItem();

            try {
                int wantedSeats = Integer.parseInt(wantedSeatsField.getText());
                if (wantedSeats > selectedConcert.getEmptySeats())
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Warning",
                            "There are not so many empty seats!");
                else {
                    Concert concert = serviceConcert.findOne(selectedConcert.getId());
                    serviceConcert.update(concert.getId(), concert.getName(), concert.getDate(), concert.getTime(),
                            concert.getPlace(), concert.getTakenSeats(),
                            concert.getEmptySeats() - wantedSeats);
                    serviceTicket.save(buyerNameField.getText(), Integer.parseInt(wantedSeatsField.getText()),
                            concert.getId());
                    modelGrade.setAll(serviceConcert.findAll());
                    wantedSeatsField.clear();
                    buyerNameField.clear();
                }
            } catch (NumberFormatException e) {
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Warning",
                        "Couldn't sell the tickets!");
            }
        }
        showsTable.getSelectionModel().clearSelection();
    }

    public void logout() throws IOException {
        mainStage.close();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/loginView.fxml"));
        AnchorPane root = loader.load();
        ControllerLogin ctrl = loader.getController();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        ctrl.setServices(stage, serviceUser, serviceConcert, serviceTicket);
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();
    }

}
