package view;
import controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterView {
    public TextField username;
    public PasswordField password;
    public PasswordField password2;
    public TextField email;
    public Button SignUp;
    public Label errorLabel1;
    public Label errorLabel2;
    public Label errorLabel3;
    public Button back;
    public TextField fullName;
    public DatePicker birthday;


    public void register(ActionEvent actionEvent) throws IOException {
        String currentUsername = username.getText();
        String currentPassword = password.getText();
        String confirmPassword = password2.getText();
        String currentEmail = email.getText();
        int response = Controller.controller.register(currentUsername, currentPassword, confirmPassword, currentEmail);
        if (currentUsername.isEmpty() || currentPassword.isEmpty() || confirmPassword.isEmpty() || currentEmail.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setContentText("You should fill the fields!");
            alert.showAndWait();
        }
        else if (response == 1){
            errorLabel1.setText("user with username " + currentUsername + " already exists!");
            username.clear();
            password.clear();
            password2.clear();
            email.clear();
            fullName.clear();
        }
        else if (response == 2){
            errorLabel2.setText("Your passwords are not the same!");
            username.clear();
            password.clear();
            password2.clear();
            email.clear();
            fullName.clear();
        }
        else if (response == 3){
            errorLabel3.setText("User with this email already exists!");
            username.clear();
            password.clear();
            password2.clear();
            email.clear();
            fullName.clear();
        }
        else if (response == 4){
            errorLabel3.setText("Email address is invalid!");
            username.clear();
            password.clear();
            password2.clear();
            email.clear();
            fullName.clear();
        }
        else if (response == 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Register");
            alert.setContentText("Sign in successfully! :)\n" +
                    "Now you can log in.");
            alert.showAndWait();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
            ((Stage) errorLabel1.getScene().getWindow()).setScene(new Scene(root));
        }
    }

    public void back(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        ((Stage) back.getScene().getWindow()).setScene(new Scene(root));
    }
}
