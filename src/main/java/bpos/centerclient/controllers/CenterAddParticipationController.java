package bpos.centerclient.controllers;

import bpos.centerclient.RestComunication.services.ClientService;
import bpos.centerclient.RestComunication.utils.WebSocketManager;
import bpos.common.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class CenterAddParticipationController {

    private ClientService clientService;
    private Stage stage;
    private Center centerLogged;
    @FXML
    private Button addmedicalinfo;

    @FXML
    private TableView<Student> studentsTable;
    @FXML
    private TableColumn<Student, String> nameColumn;
    @FXML
    private TableColumn<Student, String> cnpColumn;
    @FXML
    private TableColumn<Student, String> departmentColumn;
    @FXML
    private TableColumn<Student, Integer> yearColumn;
    @FXML
    private TableColumn<Student, String> groupColumn;

    @FXML
    private TableView<Person> personsTable;
    @FXML
    private TableColumn<Person, String> firstNameColumn;
    @FXML
    private TableColumn<Person, String> lastNameColumn;
    @FXML
    private TableColumn<Person, String> cnpColumn2;

    private ObservableList<Student> studentList = FXCollections.observableArrayList();

    private ObservableList<Person> personList = FXCollections.observableArrayList();


    @FXML
    private ListView<Event> listEvents;

    @FXML
    private ListView<Center> listCenters;

    @FXML
    private ListView<LogInfo> listUsers;

    public void setCenterParticipation(Stage stage, ClientService clientService, Center centerLogged) {
        this.clientService = clientService;
        this.stage = stage;
        this.centerLogged = centerLogged;

        loadStudents();
        loadPersons();
    }

    public void initialize() {
        // Initialize TableView columns
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPersonalDate().getFirstName()));
        cnpColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPersonalDate().getCnp()));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));

        firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPersonalDate().getFirstName()));
        lastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPersonalDate().getLastName()));
        cnpColumn2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPersonalDate().getCnp()));

        studentsTable.setItems(studentList);
        personsTable.setItems(personList);



    }

    public void loadStudents() {
        List<Student> students = clientService.findAllStudents();
        if (students != null) {
            studentList.setAll(students);
            studentsTable.setItems(studentList);
        } else {
            System.out.println("Failed to load students.");
        }
    }

    public void loadPersons() {
        List<Person> persons = clientService.findAllPersons();
        if (persons != null && !persons.isEmpty()) {
            personList.setAll(persons);
            personsTable.setItems(personList);
        } else {
            System.out.println("No persons found or failed to load persons.");
            //personsTable.setItems(FXCollections.observableArrayList()); // Clear the table
        }
    }

    @FXML
    private void handleUploadDocument(ActionEvent event) {
        Object selectedObject = null;

        selectedObject = studentsTable.getSelectionModel().getSelectedItem();

        if (selectedObject == null) {
            selectedObject = personsTable.getSelectionModel().getSelectedItem();
        }
        if (selectedObject != null) {
            String username = null;

            if (selectedObject instanceof Student) {
                // Dacă obiectul selectat este un student
                Student selectedUser = (Student) selectedObject;
                username = selectedUser.getPersonLogInfo().getUsername();
            } else if (selectedObject instanceof Person) {
                // Dacă obiectul selectat este o persoană
                Person selectedPerson = (Person) selectedObject;
                // Presupunând că PersonalData are o metodă getUsername()
                username = selectedPerson.getPersonLogInfo().getUsername();
            }

            if (username != null) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Select Document to Upload");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("All Files", "*.*"),
                        new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                        new FileChooser.ExtensionFilter("Text Files", "*.txt")
                );

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Obține fereastra curentă
                File selectedFile = fileChooser.showOpenDialog(stage);

                if (selectedFile != null) {
                    try {
                        // Creează directorul principal dacă nu există
                        File mainDir = new File("user_medicalinfo");
                        if (!mainDir.exists()) {
                            mainDir.mkdir();
                        }

                        // Creează directorul specific utilizatorului dacă nu există
                        File userDir = new File(mainDir, username);
                        if (!userDir.exists()) {
                            userDir.mkdir();
                        }

                        // Copiază fișierul în directorul specific utilizatorului
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


//    @FXML
//    private void handleUploadDocument() {
//        LogInfo selectedUser = listUsers.getSelectionModel().getSelectedItem();
//        if (selectedUser != null) {
//            String username = selectedUser.getUsername(); // Assuming LogInfo has a getUsername() method
//
//            FileChooser fileChooser = new FileChooser();
//            fileChooser.setTitle("Select Document to Upload");
//            fileChooser.getExtensionFilters().addAll(
//                    new FileChooser.ExtensionFilter("All Files", "*.*"),
//                    new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
//                    new FileChooser.ExtensionFilter("Text Files", "*.txt")
//            );
//
//            Stage stage = (Stage) listEvents.getScene().getWindow(); // Or another component in your scene
//            File selectedFile = fileChooser.showOpenDialog(stage);
//
//            if (selectedFile != null) {
//                try {
//                    // Create the main directory if it doesn't exist
//                    File mainDir = new File("user_medicalinfo");
//                    if (!mainDir.exists()) {
//                        mainDir.mkdir();
//                    }
//
//                    // Create the user-specific directory if it doesn't exist
//                    File userDir = new File(mainDir, username);
//                    if (!userDir.exists()) {
//                        userDir.mkdir();
//                    }
//
//                    // Copy the file to the user-specific directory
//                    Files.copy(selectedFile.toPath(), Paths.get(userDir.getPath(), selectedFile.getName()));
//                    System.out.println("File uploaded successfully!");
//                } catch (IOException e) {
//                    System.out.println("Failed to upload file: " + e.getMessage());
//                }
//            }
//        } else {
//            System.out.println("No user selected.");
//        }
//    }
}
