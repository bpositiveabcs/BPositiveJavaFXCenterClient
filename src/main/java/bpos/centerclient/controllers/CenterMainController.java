package bpos.centerclient.controllers;

import bpos.centerclient.RestComunication.services.ClientService;
import bpos.centerclient.RestComunication.utils.WebSocketManager;
import bpos.centerclient.RestComunication.utils.WebSocketMessageListener;
import bpos.common.model.Center;
import bpos.common.model.Event;
import bpos.common.model.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class CenterMainController implements WebSocketMessageListener {

    private Stage stage;
    private ClientService clientService;
    private boolean filterVisible = false;
    private Optional<Center> loggedUser;

    private ObservableList<Event> eventList = FXCollections.observableArrayList();

    @FXML
    private TableView<Event> eventsTable;

    @FXML
    private TableColumn<Event, String> eventColumn;

    @FXML
    private TableColumn<Event, String> dateColumn;

    @FXML
    private TableColumn<Event, String> descriptionColumn;


    public void setCenter(Stage stage,Center user) {
        this.stage = stage;
        WebSocketManager.getInstance().addListener(this);
        this.loggedUser = Optional.of(user);
        //this.loggedUser = Optional.of(clientService.login("admin","admin"));


        // Refresh events list
        refreshListEvents();
    }

    public void setServer(ClientService server)  {
        this.clientService = server;
    }

    @FXML
    public void initialize() {
        System.out.println("Main Controller initialized");

        // Initialize TableView columns
        eventColumn.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("eventStartDate"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("eventDescription"));
        eventsTable.setItems(eventList);
    }

    public void refreshListEvents() {
        //System.out.println(loggedUser.get());
        if (loggedUser.isPresent()) {
            Center center = loggedUser.get();
            this.eventList.clear();

            Iterable<Event> events = clientService.allEventsForCenter(center.getId());
            for (Event event : events) {
                this.eventList.add(event);
            }
            eventsTable.setItems(eventList);
        } else {
            System.out.println("No logged-in user found.");
        }
    }

//    @FXML
//    public void initialize() {
//        System.out.println("Main Controller initialized");
//
//        // Initialize TableView columns
//        eventColumn.setCellValueFactory(new PropertyValueFactory<>("eventName"));
//        dateColumn.setCellValueFactory(new PropertyValueFactory<>("eventStartDate"));
//        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("eventDescription"));
//        eventsTable.setItems(eventList);
//    }
//
//    public void setCenter(Stage stage, Optional<Center> user) {
//        this.stage = stage;
//        this.loggedUser = user;
//        WebSocketManager.getInstance().addListener(this);
//        if (this.clientService != null) {
//            initData();
//        }
//    }
//
//    public void setServer(ClientService server) {
//        this.clientService = server;
//        if (this.loggedUser.isPresent()) {
//            initData();
//        }
//    }
//
//    private void initData() {
//        if (loggedUser.isPresent() && clientService != null) {
//            refreshListEvents();
//        }
//    }
//
//    public void refreshListEvents() {
//        if (loggedUser.isPresent()) {
//            Center center = loggedUser.get();
//            this.eventList.clear();
//
//            List<Event> events = clientService.allEventsForCenter(center.getId());
//            if (events != null) {
//                this.eventList.addAll(events);
//            }
//            eventsTable.setItems(eventList);
//        } else {
//            System.out.println("No logged-in user found.");
//        }
//    }

    @FXML
    public void handlelogout(ActionEvent event) {
        stage.close();
        clientService.logout();
        stage.close();

    }

    @FXML
    void handleAddEvent(ActionEvent event) {
        if (stage == null) {
            System.out.println("Stage is not set");
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/centre-screen-add-event.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        CenterAddEventController controller = fxmlLoader.getController();
        controller.setCenterEvent(stage, clientService, loggedUser.get());
        stage.setTitle("Add Event");
        stage.setScene(new Scene(root));
        stage.show();
    }


    @FXML
    void handleAddDonation(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/centre-screen-add-participation.fxml"));
        Parent root= null;
        try {
            root = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        CenterAddParticipationController controller = fxmlLoader.getController();
        controller.setCenterParticipation(stage,clientService,loggedUser.get());
        stage.setTitle("Add Donation");
        stage.setScene(new Scene(root ));
        stage.show();

    }
    @FXML
    private AnchorPane cartBtn;

    @Override
    public void onMessageReceived(String message) {
        showAlert(message);
    }
    public  void showAlert( String message) {
        javafx.application.Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }


}