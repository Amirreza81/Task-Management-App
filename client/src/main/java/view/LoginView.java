package view;

import controller.Controller;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;

public class LoginView extends Application {
    private static Stage stage;
    public TextField username;
    public PasswordField password;
    public Button login;
    public Label errorLabel;
    public TextField welcomeText;
    public Button register;
    public Button exit;

    public static Stage getStage() {
        return stage;
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Controller.setupConnection();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        primaryStage.setTitle("phase3");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        stage = primaryStage;

    }


    public static void main(String[] args) {
        launch(args);
    }

    public void login(ActionEvent actionEvent) throws ParseException, IOException {
        String username1 = username.getText();
        String password1 = password.getText();
        int response = Controller.controller.logIn(username1, password1);
        if (username1.isEmpty() || password1.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setContentText("You should fill the fields!");
            alert.showAndWait();
        } else if (response == 1) {
            errorLabel.setText("There is not any user with username: " + username1 + "!");
            username.clear();
            password.clear();
        } else if (response == 2) {
            errorLabel.setText("Username and password didn’t match!");
            username.clear();
            password.clear();
        }
        if (response == 3) {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/MemberMenu.fxml"));
            ((Stage) errorLabel.getScene().getWindow()).setScene(new Scene(root));
        } else if (response == 4) {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/LeaderMenu.fxml"));
            ((Stage) errorLabel.getScene().getWindow()).setScene(new Scene(root));
        } else if (response == 5) {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/AdminMenu.fxml"));
            ((Stage) errorLabel.getScene().getWindow()).setScene(new Scene(root));
        }
    }

    public void register(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Register.fxml"));
        ((Stage) errorLabel.getScene().getWindow()).setScene(new Scene(root));
    }

    public void exit(ActionEvent actionEvent) throws IOException {
        boolean confirmation = false;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quit");
        alert.setContentText("Do you want to exit?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) confirmation = true;
        if (confirmation) {
            Controller.controller.setData();
            System.exit(0);
        }
    }
}

