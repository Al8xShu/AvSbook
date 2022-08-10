package com.avs.book;

import com.avs.book.controller.EditPageController;
import com.avs.book.controller.MainFrameController;
import com.avs.book.controller.MainPageController;
import com.avs.book.dao.Person;
import com.avs.book.service.PersonService;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;
import java.util.prefs.Preferences;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<Person> personData = FXCollections.observableArrayList();

    public MainApp() {
        Person exPerson = new Person("Ex", "Ample", "For", "Fill",
                "This", "7(777)777-77-77", LocalDate.now(), 12345678);
        personData.add(exPerson);
    }

    public ObservableList<Person> getPersonData() {
        return personData;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AvSbook");
        this.primaryStage.getIcons().add(new Image("file:resources/images/main_icon.png"));
        initMainFrame();
        showMainPage();
    }

    public void initMainFrame() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("MainFrame.fxml"));
            rootLayout = (BorderPane) loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            MainFrameController controller = loader.getController();
            controller.setMainApp(this);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMainPage() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("MainPage.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();
            rootLayout.setCenter(personOverview);
            MainPageController controller = loader.getController();
            controller.setMain(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public boolean showPersonEditPage(Person person) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("EditPage.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            EditPageController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPerson(person);
            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public File getPersonFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    public void setPersonFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());
            primaryStage.setTitle("AvSbook - " + file.getName());
        } else {
            prefs.remove("filePath");
            primaryStage.setTitle("AvSbook");
        }
    }

    public void loadPersonDataFromFIle(File file) {
        try {
            personData.clear();
            BufferedReader reader;
            reader = new BufferedReader(new FileReader(file));
            String newLine = reader.readLine();
            while (newLine != null) {
                Person newPerson = PersonService.personBuilder(newLine);
                personData.add(newPerson);
                newLine = reader.readLine();
            }
            reader.close();
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data!");
            alert.setContentText("Could not load data from file:\n" + file.getPath());
            alert.showAndWait();
        }
    }

    public void savePersonDataToFile(File file) {
        try {
            if (file.exists()) {
                PrintWriter writer = new PrintWriter(file.getPath());
                writer.print("");
                writer.close();
                FileWriter myWriter = new FileWriter(file);
                for (Person personString : personData) {
                    String textParam = personString.getFirstName() + "#" + personString.getLastName()
                            + "#" + personString.getStreet() + "#" + personString.getCity()
                            + "#" + personString.getEmail() + "#" + personString.getPhone()
                            + "#" + PersonService.format(personString.getBirthday()) + "#" + personString.getPostalCode();
                    myWriter.write(textParam + "\n");
                }
                myWriter.close();
            } else {
                FileWriter myWriter = new FileWriter(file);
                for (Person personString : personData) {
                    String textParam = personString.getFirstName() + "#" + personString.getLastName()
                            + "#" + personString.getStreet() + "#" + personString.getCity()
                            + "#" + personString.getEmail() + "#" + personString.getPhone()
                            + "#" + PersonService.format(personString.getBirthday()) + "#" + personString.getPostalCode();
                    myWriter.write(textParam + "\n");
                }
                myWriter.close();
            }
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data!");
            alert.setContentText("Could not save data to file:\n" + file.getPath());
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}