package bpos.centerclient.controllers;

import bpos.centerclient.RestComunication.services.ClientService;
import bpos.centerclient.RestComunication.utils.WebSocketManager;
import bpos.centerclient.RestComunication.utils.WebSocketMessageListener;
import bpos.common.model.Center;
import bpos.common.model.Person;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;

public class CenterMainController implements WebSocketMessageListener {

    private Stage stage;
    //private Credentials credentials;
    private ClientService clientService;
    private boolean filterVisible = false;
    private Optional<Center> loggedUser;

    public void setCenter(Stage stage,Optional<Center> user) {
        this.stage = stage;
        WebSocketManager.getInstance().addListener(this);
        this.loggedUser = user;
        //this.credentials = credentials;
        //this.txtNameOfUser.setText(credentials.getUsername());
        this.clientService = new ClientService();
        //loadTopBooksCategories();
        //filterScrollPane.setVisible(false);
        //setBasketItemsCount();
    }

    public void setServer(ClientService server)  {
        this.clientService = server;


    }

    public void setLoggedUser(Optional<Center> user) {
        this.loggedUser = user;

    }


    @FXML
    public void initialize() {
    }










    @FXML
    public void handlelogout(ActionEvent event) {
        stage.close();
        clientService.logout();
        stage.close();

    }


//    @FXML
//    void handleAddEvent(ActionEvent event) {
//        FXMLLoader fxmlLoader = new FXMLLoader();
//        fxmlLoader.setLocation(getClass().getResource("/centre-screen-add-event.fxml"));
//        Parent root= null;
//        try {
//            root = fxmlLoader.load();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        CenterAddEventController controller = fxmlLoader.<CenterAddEventController>getController();
//        controller.setCenterEvent(stage,clientService);
//        stage.setTitle("Add Event");
//        stage.setScene(new Scene(root ));
//        stage.show();
//
//    }

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
        controller.setCenterEvent(stage, clientService);
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
        controller.setCenterParticipation(stage,clientService);
        stage.setTitle("Add Donation");
        stage.setScene(new Scene(root ));
        stage.show();

    }



    @FXML
    private AnchorPane cartBtn;

    @Override
    public void onMessageReceived(String message) {

    }


}