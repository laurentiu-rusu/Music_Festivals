package controllers;

import domains.Concert;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import observer.applicationObserver;
import persistance.PersistanceException;
import service.IService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ControllerMainApp implements applicationObserver {
    private ObservableList<Concert> modelGrade = FXCollections.observableArrayList();
    private ObservableList<Concert> artists = FXCollections.observableArrayList();
    IService service;
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

    private ControllerLogin loginController;

    public void set(Stage mainStage, IService service, ControllerLogin loginController) throws PersistanceException {
        this.service = service;
        this.mainStage = mainStage;
        this.loginController = loginController;
        init();
    }

    public void init() throws PersistanceException {
        List<Concert> list = service.findAll();
        ObservableList<Concert> excursieObservableList = FXCollections.observableArrayList(list);

        showsTable.setItems(excursieObservableList);
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

//    public void setService(Stage mainStage, ServiceConcert serviceConcert, ServiceTicket serviceTicket, ServiceUser serviceUser) {
//        this.mainStage = mainStage;
//        this.serviceConcert = serviceConcert;
//        this.serviceTicket = serviceTicket;
//        this.serviceUser = serviceUser;
//        calendar.setValue(LocalDate.now());
//        modelGrade.setAll(serviceConcert.findAll());
//    }

    public void tableSelection() {
        showsTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Concert>() {
            @Override
            public void changed(ObservableValue<? extends Concert> observableValue, Concert excursie, Concert t1) {
                if (showsTable.getSelectionModel().getSelectedItem() != null && ((Concert) showsTable.getSelectionModel().getSelectedItem()).getEmptySeats() != 0) {
                    Concert excursie1 = ((Concert) showsTable.getSelectionModel().getSelectedItem());
//                    sIdColumn.setText(excursie1.getId().toString());
                } else {
                    sIdColumn.setText("");
                }
            }
        });
    }

    public void initialize() {
        sIdColumn.setCellValueFactory(new PropertyValueFactory<Concert,Integer>("id"));
        sArtistColumn.setCellValueFactory(new PropertyValueFactory<Concert, String>("name"));
        sLocationColumn.setCellValueFactory(new PropertyValueFactory<Concert, String>("place"));
        sDateColumn.setCellValueFactory(new PropertyValueFactory<Concert, LocalDate>("date"));
        sTakenColumn.setCellValueFactory(new PropertyValueFactory<Concert, Integer>("takenSeats"));
        sEmptyColumn.setCellValueFactory(new PropertyValueFactory<Concert, Integer>("emptySeats"));
        tableSelection();
    }

    public void searchAction(){
        try {
            List<Concert> concerts = service.findAll();
            LocalDate data = calendar.getValue();
            List<Concert> rez = new ArrayList<>();
            for (Concert concert : concerts) {
                if (concert.getDate().isEqual(data)) {
                    rez.add(concert);
                }
            }
            artists.setAll(rez);
            populateArtistTable();
        } catch (DateTimeParseException | PersistanceException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Wrong Data");
            alert.show();
        }
    }

    private void populateArtistTable(){
        aArtistColumn.setCellValueFactory(new PropertyValueFactory<Concert, String>("name"));
        aLocationColumn.setCellValueFactory(new PropertyValueFactory<Concert, String>("place"));
        aEmptyColumn.setCellValueFactory(new PropertyValueFactory<Concert, Integer>("emptySeats"));
        aHourColumn.setCellValueFactory(new PropertyValueFactory<Concert,String>("time"));
        artistTable.setItems(artists);
    }

    public void sellAction() {
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
                    Concert concert = service.findOne(selectedConcert.getId());
                    service.update(concert.getId(), concert.getName(), concert.getDate(), concert.getTime(),
                            concert.getPlace(), concert.getTakenSeats() + wantedSeats,
                            concert.getEmptySeats() - wantedSeats);
                    service.saveTicket(buyerNameField.getText(), Integer.parseInt(wantedSeatsField.getText()),
                            concert.getId());
                    modelGrade.setAll(service.findAll());
                    wantedSeatsField.clear();
                    buyerNameField.clear();
                }
            } catch (NumberFormatException | PersistanceException e) {
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Warning",
                        "Couldn't sell the tickets!");
            }
        }
        showsTable.getSelectionModel().clearSelection();
    }

    public void logout() throws IOException, PersistanceException {
        mainStage.close();
        service.logout(loginController.getUser());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/loginView.fxml"));
        AnchorPane root = loader.load();
        ControllerLogin ctrl = loader.getController();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        ctrl.setService(stage, mainStage,service,this);
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();
    }



    public void setTable() throws PersistanceException {
        List<Concert> list = service.findAll();
        ObservableList<Concert> excursies= FXCollections.observableArrayList(list);
        showsTable.setItems(excursies);
        showsTable.refresh();
        showsTable.getSelectionModel().select(null);

    }

    @Override
    public void updateTrips(List<Concert> concerts) throws PersistanceException {
        showsTable.getItems().clear();
        ObservableList<Concert> all = FXCollections.observableArrayList(concerts);
        showsTable.setItems(all);
        showsTable.refresh();
        showsTable.getSelectionModel().select(null);

    }
}
