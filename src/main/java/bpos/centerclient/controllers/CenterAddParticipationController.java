package bpos.centerclient.controllers;

import bpos.centerclient.RestComunication.services.ClientService;
import bpos.centerclient.RestComunication.utils.WebSocketManager;
import bpos.common.model.*;
import com.sun.javafx.UnmodifiableArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CenterAddParticipationController {

    private ClientService clientService;
    private Stage stage;
    private Center centerLogged;
    @FXML
    private Button addmedicalinfo;

    @FXML
    private Button returnbutton1;

    @FXML
    private TextField cnptextfield;

    @FXML
    private Button searchButton;

    @FXML
    private Button revertButton;


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
    private TextField pointstextfield;

    @FXML
    private ComboBox<String> tipcombo;

    private Optional<Center> loggedUser;

    public void setCenterParticipation(Stage stage, ClientService clientService, Center centerLogged) {
        this.clientService = clientService;
        this.stage = stage;
        this.centerLogged = centerLogged;

        loadStudents();
        loadPersons();
    }

    public void initialize() {

        tipcombo.getItems().addAll("Transfuzie", "Trombocite", "Plasma");
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
        persons.removeIf(person -> "Admin".equals(person.getPersonalDate().getFirstName()));

        if (persons != null && !persons.isEmpty()) {
            personList.setAll(persons);
            personsTable.setItems(personList);
        } else {
            System.out.println("No persons found or failed to load persons.");
            //personsTable.setItems(FXCollections.observableArrayList()); // Clear the table
        }
    }

//    @FXML
//    private void handleUploadDocument(ActionEvent event) {
//        Object selectedObject = null;
//
//        selectedObject = studentsTable.getSelectionModel().getSelectedItem();
//
//        if (selectedObject == null) {
//            selectedObject = personsTable.getSelectionModel().getSelectedItem();
//        }
//        if (selectedObject != null) {
//            String username = null;
//
//            if (selectedObject instanceof Student) {
//                // Dacă obiectul selectat este un student
//                Student selectedUser = (Student) selectedObject;
//                username = selectedUser.getPersonLogInfo().getUsername();
//            } else if (selectedObject instanceof Person) {
//                // Dacă obiectul selectat este o persoană
//                Person selectedPerson = (Person) selectedObject;
//                // Presupunând că PersonalData are o metodă getUsername()
//                username = selectedPerson.getPersonLogInfo().getUsername();
//            }
//
//            if (username != null) {
//                FileChooser fileChooser = new FileChooser();
//                fileChooser.setTitle("Select Document to Upload");
//                fileChooser.getExtensionFilters().addAll(
//                        new FileChooser.ExtensionFilter("All Files", "*.*"),
//                        new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
//                        new FileChooser.ExtensionFilter("Text Files", "*.txt")
//                );
//
//                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Obține fereastra curentă
//                File selectedFile = fileChooser.showOpenDialog(stage);
//
//                if (selectedFile != null) {
//                    try {
//                        // Creează directorul principal dacă nu există
//                        File mainDir = new File("user_medicalinfo");
//                        if (!mainDir.exists()) {
//                            mainDir.mkdir();
//                        }
//
//                        // Creează directorul specific utilizatorului dacă nu există
//                        File userDir = new File(mainDir, username);
//                        if (!userDir.exists()) {
//                            userDir.mkdir();
//                        }
//
//                        // Copiază fișierul în directorul specific utilizatorului
//                        Files.copy(selectedFile.toPath(), Paths.get(userDir.getPath(), selectedFile.getName()));
//                        System.out.println("File uploaded successfully!");
//                    } catch (IOException e) {
//                        System.out.println("Failed to upload file: " + e.getMessage());
//                    }
//                }
//            } else {
//                System.out.println("No user selected.");
//            }
//        }
//    }

    @FXML
    private void handleUploadDocument(ActionEvent event) {
        Object selectedObject = studentsTable.getSelectionModel().getSelectedItem();

        if (selectedObject == null) {
            selectedObject = personsTable.getSelectionModel().getSelectedItem();
        }
        if (selectedObject != null) {
            String username = null;

            if (selectedObject instanceof Student) {
                Student selectedUser = (Student) selectedObject;
                username = selectedUser.getPersonLogInfo().getUsername();
            } else if (selectedObject instanceof Person) {
                Person selectedPerson = (Person) selectedObject;
                username = selectedPerson.getPersonLogInfo().getUsername();
            }

            if (username != null) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Select Document to Upload");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("All Files", "."),
                        new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                        new FileChooser.ExtensionFilter("Text Files", "*.txt")
                );

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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

                        // Copy the file in JavaFX
                        Files.copy(selectedFile.toPath(), Paths.get(userDir.getPath(), selectedFile.getName()));

                        // Send the file to the server
                        clientService.uploadFileToServer(selectedFile, username);

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

    @FXML
    private void handleSearchByCnp() {
        String cnp = cnptextfield.getText();
        if (cnp != null && !cnp.isEmpty()) {
            List<Person> foundPersons = clientService.findByCnpPerson(cnp);
            List<Student> foundStudents = clientService.findByCnpStudent(cnp);

            if (foundStudents != null && !foundStudents.isEmpty()) {
                studentList.setAll(foundStudents);
                studentsTable.setItems(studentList);
                personsTable.setItems(FXCollections.observableArrayList()); // Clear the person table
            }else if (foundPersons != null && !foundPersons.isEmpty()) {
                personList.setAll(foundPersons);
                personsTable.setItems(personList);
                studentsTable.setItems(FXCollections.observableArrayList()); // Clear the student table
            } else {
                System.out.println("No persons or students found with the given CNP.");
                personsTable.setItems(FXCollections.observableArrayList()); // Clear the person table
                studentsTable.setItems(FXCollections.observableArrayList()); // Clear the student table
            }
        } else {
            System.out.println("Please enter a CNP to search.");
        }
    }




//    @FXML
//    private void handleSearchByCnp() {
//        String cnp = cnptextfield.getText();
//        if (cnp != null && !cnp.isEmpty()) {
//            List<Person> foundPersons = clientService.findByCnpPerson(cnp);
//            List<Student> foundStudents = clientService.findByCnpStudent(cnp);
//
//            if (foundPersons != null && !foundPersons.isEmpty()) {
//                personList.setAll(foundPersons);
//                personsTable.setItems(personList);
//                studentsTable.setItems(FXCollections.observableArrayList()); // Clear the student table
//            } else if (foundStudents != null && !foundStudents.isEmpty()) {
//                studentList.setAll(foundStudents);
//                studentsTable.setItems(studentList);
//                personsTable.setItems(FXCollections.observableArrayList()); // Clear the person table
//            } else {
//                System.out.println("No persons or students found with the given CNP.");
//                personsTable.setItems(FXCollections.observableArrayList()); // Clear the person table
//                studentsTable.setItems(FXCollections.observableArrayList()); // Clear the student table
//            }
//        } else {
//            System.out.println("Please enter a CNP to search.");
//        }
//    }

    @FXML
    private void handleRevertSearch() {
        loadPersons();
        loadStudents();
        cnptextfield.clear();
    }

    @FXML
    private void handleaddDonation() {
        Object selectedObject = null;

        selectedObject = studentsTable.getSelectionModel().getSelectedItem();

        if (selectedObject == null) {
            selectedObject = personsTable.getSelectionModel().getSelectedItem();
        }

        if (selectedObject != null) {
            Person person = (Person) selectedObject;

            String selectedType = tipcombo.getValue();
            String pointsText = pointstextfield.getText();
            if (selectedType != null && pointsText != null && !pointsText.isEmpty()) {
                try {
                    int points = Integer.parseInt(pointsText);

                    DonationType donationType = new DonationType();
                    switch (selectedType) {
                        case "Transfuzie":
                            donationType.setId(1);  // Set appropriate ID
                            donationType.setName("Transfuzie");
                            break;
                        case "Trombocite":
                            donationType.setId(2);  // Set appropriate ID
                            donationType.setName("Trombocite");
                            break;
                        case "Plasma":
                            donationType.setId(3);  // Set appropriate ID
                            donationType.setName("Plasma");
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid donation type selected");
                    }

                    Donation donation = new Donation();
                    donation.setDonationType(donationType);
                    donation.setPoints(points);
                    donation.setId(0);

                    clientService.addDonation(donation, person);
//                    if (savedDonation != null) {
//                        System.out.println("Donation added successfully");
//                    } else {
//                        System.out.println("Failed to add donation");
//                    }
                } catch (NumberFormatException e) {
                    System.out.println("Points must be a valid number");
                }
            } else {
                System.out.println("Please select a donation type and enter points");
            }
        } else {
            System.out.println("No user selected.");
        }
    }




    @FXML
    private void handlereturn1(ActionEvent event) {

        loggedUser = Optional.of(centerLogged);
        // Închide fereastra curentă
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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

