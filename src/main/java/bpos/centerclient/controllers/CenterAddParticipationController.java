package bpos.centerclient.controllers;

import bpos.centerclient.RestComunication.services.ClientService;
import bpos.common.model.Center;
import bpos.common.model.Event;
import bpos.common.model.LogInfo;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CenterAddParticipationController {

    @FXML
    private ListView<Event> listEvents;

    @FXML
    private ListView<Center> listCenters;

    @FXML
    private ListView<LogInfo> listUsers;

    public void setCenterParticipation(Stage stage, ClientService clientService) {
    }

    @FXML
    private void handleUploadDocument() {
        LogInfo selectedUser = listUsers.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            String username = selectedUser.getUsername(); // Assuming LogInfo has a getUsername() method

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Document to Upload");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All Files", "*.*"),
                    new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                    new FileChooser.ExtensionFilter("Text Files", "*.txt")
            );

            Stage stage = (Stage) listEvents.getScene().getWindow(); // Or another component in your scene
            File selectedFile = fileChooser.showOpenDialog(stage);

            if (selectedFile != null) {
                try {
                    // Create the main directory if it doesn't exist
                    File mainDir = new File("user_medicalinfo");
                    if (!mainDir.exists()) {
                        mainDir.mkdir();
                    }

                    // Create the user-specific directory if it doesn't exist
                    File userDir = new File(mainDir, username);
                    if (!userDir.exists()) {
                        userDir.mkdir();
                    }

                    // Copy the file to the user-specific directory
                    Files.copy(selectedFile.toPath(), Paths.get(userDir.getPath(), selectedFile.getName()));
                    System.out.println("File uploaded successfully!");
                } catch (IOException e) {
                    System.out.println("Failed to upload file: " + e.getMessage());
                }
            }
        } else {
            System.out.println("No user selected.");
        }
    }
}
