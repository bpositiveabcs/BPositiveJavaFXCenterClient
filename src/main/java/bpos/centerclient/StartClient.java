package bpos.centerclient;


import bpos.centerclient.RestComunication.services.ClientService;
import bpos.centerclient.RestComunication.utils.ClientWebSocket;
import bpos.centerclient.RestComunication.utils.WebSocketManager;
import bpos.centerclient.controllers.LogInController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class StartClient extends Application {
    private Stage primaryStage;
    private ClientWebSocket clientWebSocket;
    private static int defaultChatPort = 55556;
    private static String defaultServer = "localhost";

    @Override
    public void start(Stage primaryStage) throws Exception {
        WebSocketManager.getInstance().connect();
        Properties properties = new Properties();
        try (InputStream input = getClass().getResourceAsStream("/application.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find application.properties");
                return;
            }
            // load a properties file from class path, inside static method
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }

        ClientService server = new ClientService();

        FXMLLoader loader = new FXMLLoader();
        URL url = getClass().getResource("/login-screen.fxml");
        System.out.println(url);
        loader.setLocation(getClass().getResource("/login-screen.fxml"));
        Parent root;
        try {
            System.out.println("Am intrat aici ");
            root = loader.load();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load login-screen.fxml", e);

        }

        LogInController controller = loader.getController();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");
        controller.setProperties(primaryStage,server);
        WebSocketManager.getInstance().addListener(message -> Platform.runLater(() -> System.out.println(message)));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
