package bpos.centerclient.controllers;

import bpos.centerclient.RestComunication.services.ClientService;
import bpos.centerclient.RestComunication.utils.WebSocketManager;
import bpos.centerclient.RestComunication.utils.WebSocketMessageListener;
import bpos.common.model.Center;
import bpos.common.model.Event;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CenterAddEventController
{
    @FXML
    private TextField nametextfield;

    @FXML
    private DatePicker adtae;

    @FXML
    private DatePicker edate;

    @FXML
    private TextField nrparticipants;

    @FXML
    private TextArea requirementsfield;

    @FXML
    private TextArea descriptionfield;
    private ClientService clientService;
    private Stage stage;
    private Center centerLogged;

    public void setCenterEvent(Stage stage, ClientService clientService, Center centerLogged) {
        this.clientService = clientService;
        this.stage = stage;
        this.centerLogged = centerLogged;


    }

    public void handleAddEvent(ActionEvent actionEvent) {
        String name = nametextfield.getText();
        String announcementDate = adtae.getValue() != null ? adtae.getValue().toString() : "";
        String endingDate = edate.getValue() != null ? edate.getValue().toString() : "";
        String numberOfParticipants = nrparticipants.getText();
        String requirements = requirementsfield.getText();
        String description = descriptionfield.getText();

        // Aici puteți adăuga logica pentru a procesa datele colectate, de exemplu, salvarea într-o bază de date
        System.out.println("Event Added: " + name);
        System.out.println("Announcement Date: " + announcementDate);
        System.out.println("Ending Date: " + endingDate);
        System.out.println("Number of Participants: " + numberOfParticipants);
        System.out.println("Requirements: " + requirements);
        System.out.println("Description: " + description);

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime announcementDateTransform = LocalDateTime.parse(announcementDate, formatter);
        LocalDateTime endingDateTransform = LocalDateTime.parse(endingDate, formatter);
        Event event = new Event(name,LocalDateTime.MIN, announcementDateTransform,endingDateTransform, Integer.parseInt(numberOfParticipants),description,requirements,centerLogged);
        Event eventAdded=clientService.addEvent(event);

        if(eventAdded!=null) {
            System.out.println("Event added successfully");
        }
    }

//    public void onMessageReceived(String message)
//    public  void showAlert( String message) {
//        javafx.application.Platform.runLater(() -> {
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setHeaderText(null);
//            alert.setContentText(message);
//            alert.showAndWait();
//        });
//    }

}
