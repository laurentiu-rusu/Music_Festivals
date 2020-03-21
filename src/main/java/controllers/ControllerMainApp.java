package controllers;

import domains.Concert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import services.ServiceConcert;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ControllerMainApp {
    ObservableList<Concert> modelGrade = FXCollections.observableArrayList();
    ObservableList<Concert> artists = FXCollections.observableArrayList();
    ServiceConcert serviceConcert;

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

    public void setService(ServiceConcert serviceConcert) {
        this.serviceConcert = serviceConcert;
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

}
