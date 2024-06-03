package bpos.centerclient.controllers;

import bpos.centerclient.RestComunication.services.ClientService;
import bpos.centerclient.RestComunication.utils.WebSocketManager;
import bpos.centerclient.RestComunication.utils.WebSocketMessageListener;
import bpos.common.model.Center;
import bpos.common.model.Event;
import javafx.application.Platform;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

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

    private Optional<Center> loggedUser;



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

        // Definirea formatului corespunzător datei în interfața de utilizator
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

// Conversia datelor în formatul așteptat
        LocalDate announcementLocalDate = LocalDate.parse(announcementDate, formatter);
        LocalDate endingLocalDate = LocalDate.parse(endingDate, formatter);

        // Transformarea datelor locale în LocalDateTime
        LocalDateTime announcementDateTransform = announcementLocalDate.atStartOfDay();
        LocalDateTime endingDateTransform = endingLocalDate.atStartOfDay();

        LocalDateTime beginningDateTransform = LocalDateTime.of(0, 1, 1, 0, 0);

        // Crearea evenimentului cu datele transformate
        Event event = new Event(name, announcementDateTransform, beginningDateTransform, endingDateTransform, Integer.parseInt(numberOfParticipants), description, requirements, centerLogged);
        Platform.runLater(()->{
            Event eventAdded = clientService.addEvent(event);

            if (eventAdded != null) {
                System.out.println("Event added successfully");
            }
        });


    }


    public void handlereturn2(ActionEvent actionEvent) {

        loggedUser = Optional.of(centerLogged);
        // Închide fereastra curentă
        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        currentStage.close();

        // Deschide fereastra anterioară (CenterMainController)
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/centre-screen.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        CenterMainController controller = fxmlLoader.getController();
        controller.setServer(clientService);
        controller.setCenter(stage, loggedUser.get()); // Setează utilizatorul și alte informații necesare

        stage.setTitle("Main Screen");
        stage.setScene(new Scene(root));
        stage.show();
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
