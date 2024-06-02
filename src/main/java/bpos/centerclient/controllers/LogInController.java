package bpos.centerclient.controllers;

import bpos.centerclient.RestComunication.services.ClientService;

import bpos.common.model.Center;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class LogInController {
    private static final String BASE_URL ="http://localhost:55555/personActorService" ;


    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private AnchorPane backgroundPane;

    @FXML
    private ImageView rectangleImageView;
    @FXML
    private ImageView topImageView;

    @FXML
    private Button loginButton;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;

    private String URI_WebSocket = "ws://localhost:8080/websocket-endpoint";
    private String URI_REST = "http://localhost:55555/api/common/login";
    private Stage stage;
    private ClientService clientService;
    private Center centerLogged;


    public void initialize() {
        System.out.println("LogInController initialized");
        //setWindowSize();
        playBackgroundAnimation();

        Image image = new Image(getClass().getResource("/img_4.png").toExternalForm());
        rectangleImageView.setImage(image);
        pulsatingAnimation(rectangleImageView);
        Image topImage = new Image(getClass().getResource("/banner v2.png").toExternalForm());
        topImageView.setImage(topImage);


        pulsatingAnimation(rectangleImageView);
        pulsatingAnimationComingAtYou(topImageView);

        pulsatingAnimationgeneral(loginButton,3);
        pulsatingAnimationgeneral(usernameTextField,3);
        pulsatingAnimationgeneral(passwordTextField,3);
        pulsatingAnimationgeneral(usernameLabel,3);
        pulsatingAnimationgeneral(passwordLabel,3);


    }

    public void setProperties(Stage primaryStage, ClientService server) {
        this.stage = primaryStage;
        this.clientService = server;
    }



    public static void pulsatingAnimation(ImageView imageView) {
        // Definirea animației de scalare
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(5));
        scaleTransition.setNode(imageView); // Setarea imageView ca nod pentru animație
        scaleTransition.setToX(1.2); // Scalarea pe axa X la 1.2 (mărire cu 20%)
        scaleTransition.setToY(1.2); // Scalarea pe axa Y la 1.2 (mărire cu 20%)
        scaleTransition.setAutoReverse(true); // Revine la dimensiunea inițială după finalizarea animației
        scaleTransition.setCycleCount(Animation.INDEFINITE); // Animație repetată la infinit
        scaleTransition.play(); // Pornirea animației de scalare
    }

    public static void pulsatingAnimationComingAtYou(ImageView imageView) {
        // Definirea animației de scalare pentru imaginea de sus
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(5));
        scaleTransition.setNode(imageView); // Setarea imageView ca nod pentru animație
        scaleTransition.setToX(1.1); // Scalarea pe axa X la 1.5 (mărire cu 50%)
        scaleTransition.setToY(1.1); // Scalarea pe axa Y la 1.5 (mărire cu 50%)
        scaleTransition.setAutoReverse(true); // Revine la dimensiunea inițială după finalizarea animației
        scaleTransition.setCycleCount(Animation.INDEFINITE); // Animație repetată la infinit
        scaleTransition.play(); // Pornirea animației de scalare
    }

    public static void pulsatingAnimationgeneral(Node node, int cycles) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(5), node);
        scaleTransition.setToX(1.1); // Scalarea pe axa X la 1.2 (mărire cu 20%)
        scaleTransition.setToY(1.1); // Scalarea pe axa Y la 1.2 (mărire cu 20%)
        scaleTransition.setAutoReverse(true); // Revine la dimensiunea inițială după finalizarea animației
        scaleTransition.setCycleCount(cycles * 2); // Numărul de cicluri (într-un ciclu complet se include mărirea și micșorarea)
        scaleTransition.setOnFinished(event -> {
            node.setScaleX(1); // Resetare la dimensiunea inițială pe axa X
            node.setScaleY(1); // Resetare la dimensiunea inițială pe axa Y
        });
        scaleTransition.play(); // Pornirea animației de scalare
    }

    private void playBackgroundAnimation2() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(backgroundPane.styleProperty(), "-fx-background-color: #ADD8E6")),
                new KeyFrame(Duration.seconds(3), new KeyValue(backgroundPane.styleProperty(), "-fx-background-color: #90EE90")),
                new KeyFrame(Duration.seconds(6), new KeyValue(backgroundPane.styleProperty(), "-fx-background-color: #ADD8E6"))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.play();
    }
    private void playBackgroundAnimation() {
        Stop[] stops = new Stop[] {
                new Stop(0, Color.web("#F0E7E7")),
                new Stop(1, Color.web("#EBDCC6"))
        };
        LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(backgroundPane.styleProperty(), "-fx-background-color: " + toRGBCode(Color.web("#F0E7E7")))),
                new KeyFrame(Duration.seconds(6), new KeyValue(backgroundPane.styleProperty(), "-fx-background-color: " + toRGBCode(Color.web("#EBDCC6"))))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.play();
    }

    private String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }



    private void setWindowSize() {
        Platform.runLater(() -> {
            Stage stage = (Stage) backgroundPane.getScene().getWindow();
            stage.setWidth(600);
            stage.setHeight(400);
            //stage.setResizable(false);  // Opțional: fereastra să nu fie redimensionabilă
        });
    }


    @FXML
    void handleCancel(ActionEvent event) {
        stage.close();
        //close program as well

    }


public void handleLogin(ActionEvent event) throws URISyntaxException, JsonProcessingException {
    String username = usernameTextField.getText();
    String password = passwordTextField.getText();

    Map<String, String> loginParams = new HashMap<>();
    loginParams.put("username", username);
    loginParams.put("password", password);
    String requestBody = "";
    // Convertirea Map într-un JSON String
    ObjectMapper objectMapper = new ObjectMapper();
    try {
        requestBody = objectMapper.writeValueAsString(loginParams);
    } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
    }

    Center person = clientService.login(username, password);
    this.centerLogged=person;
    if (person != null) {
        System.out.println("Login successful");
        try {
            handleLoginSuccess(username, password, person);
        } catch (IOException e) {
            e.printStackTrace();
        }
    } else {
        System.out.println("Login failed");
        passwordTextField.clear();
        usernameTextField.clear();
    }
}


    public void handleLoginSuccess(String username, String password, Center person) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        URL url=getClass().getResource("/centre-screen.fxml");
        loader.setLocation(url);
        Parent userViewParent = loader.load();
        CenterMainController userController = loader.getController();
        if (!Objects.equals(person.getLogInfo().getPassword(), password)) {
            System.out.println("Password is not correct");
            passwordTextField.clear();
            return;
        }
        userController.setServer(clientService);
        userController.setCenter(stage, person);
        System.out.println(person);
        Scene userViewScene = new Scene(userViewParent);
        Stage stage = (Stage) usernameTextField.getScene().getWindow();
        stage.setScene(userViewScene);
        stage.show();


        passwordTextField.clear();
        usernameTextField.clear();

    }

}



