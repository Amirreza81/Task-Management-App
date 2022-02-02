package view;

import controller.Controller;
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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TaskPageView implements Initializable {
    public Button btnTaskList;
    public Label lblDateOfCreation;
    public Label lblDeadline;
    public Label lblTaskTitle;
    public Label lblDescription;
    public Label lblPriority;
    public AnchorPane pane;
    public ImageView exit;
    public Button leave;


    public void goToTaskList(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/TaskList.fxml"));
        ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
    }

    public Task getTask() {
        return Controller.controller.getSelectedTask();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Task selectTask = getTask();
        lblTaskTitle.setText(selectTask.getTitle());
        lblDeadline.setText(selectTask.getDeadline().toString());
        lblPriority.setText(selectTask.getPriority());
        lblDateOfCreation.setText(selectTask.getDateOfCreation().toString());
        if (selectTask.getDescription().equals(""))
            lblDescription.setText("description is null!");
        else
            lblDescription.setText(selectTask.getDescription());


    }

    public void exit(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void leave(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/LeaderMenu.fxml"));
        ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
    }
}
