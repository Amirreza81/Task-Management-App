package view;

import controller.JsonController;
import controller.LoggedController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Task;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TaskPageForCalenderView implements Initializable {

    private final Task selectTask = LoggedController.getInstance().getSelectedTask();
    public AnchorPane pane;
    public ImageView exit;
    public Button Calender;
    public Button leave;
    public Label lblTaskTitle;
    public Label lblDescription;
    public Label lblPriority;
    public Label lblDateOfCreation;
    public Label lblDeadline;

    public void exit(MouseEvent mouseEvent) {
        JsonController.getInstance().updateJson();
        System.exit(0);
    }

    public void CalenderPage(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Calender.fxml"));
        ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
    }

    public void leave(ActionEvent actionEvent) throws IOException {
        if (LoggedController.getInstance().getLoggedInUser().getRole().equals("Leader")) {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/LeaderMenu.fxml"));
            ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
        } else if (LoggedController.getInstance().getLoggedInUser().getRole().equals("Admin")) {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/AdminMenu.fxml"));
            ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
        } else {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/MemberMenu.fxml"));
            ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        lblTaskTitle.setText(selectTask.getTitle());
        lblDeadline.setText(selectTask.getDeadline().toString());
        lblPriority.setText(selectTask.getPriority());
        lblDateOfCreation.setText(selectTask.getDateOfCreation().toString());
        if (selectTask.getDescription().equals(""))
            lblDescription.setText("description is null!");
        else
            lblDescription.setText(selectTask.getDescription());
    }
}
