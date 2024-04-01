package view;

import controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;

public class ChangeInformationView {
    public TextField username;
    public AnchorPane pane;
    public TextField email;
    public TextField password;
    public Button Record;
    public Button back;
    public Button Search;
    public Label error;
    private User findUser;

    public void Record(ActionEvent actionEvent) throws IOException {
        Controller.controller.changeUserNameForAdmin(findUser.getUserName(), username.getText());
        Controller.controller.changeEmail(username.getText(), email.getText());
        int response = Controller.controller.changePasswordForAdmin(username.getText(), findUser.getPassword(), password.getText());
        if (response == 1) {
            error.setText("wrong old password !");
        } else if (response == 2) {

            error.setText("Please type a New Password !");
        } else if (response == 3) {

            error.setText("Please Choose A strong Password (Containing at least 8 characters including 1 digit \" +\n" +
                    "                    \"and 1 Capital Letter)");

        } else if (response == 4) {
            error.setText("This username does not exist !");
        } else if (response == 0) {
            error.setText("Information changed successfully");
        }
    }

    public void back(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/AdminMenu.fxml"));
        ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
    }

    public void Search(ActionEvent actionEvent) {
        for (User user : Controller.controller.getAllUsers()) {
            if (user.getUserName().equals(username.getText()))
                findUser = user;
        }
        if (findUser == null)
            error.setText("This user does not exist");
        else {
            email.setText(findUser.getEmail());
            password.setText(findUser.getPassword());
        }
    }
}
