package view;

import controller.Controller;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;
import model.Task;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TaskPageForCalenderView implements Initializable {

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
        System.exit(0);
    }

    public void CalenderPage(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Calender.fxml"));
        ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
    }

    public Task getTask() {
        return Controller.controller.getSelectedTask();
    }

    public void leave(ActionEvent actionEvent) throws IOException {
        if (Controller.controller.getLoggedInUser().getRole().equals("Leader")) {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/LeaderMenu.fxml"));
            ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
        } else if (Controller.controller.getLoggedInUser().getRole().equals("Admin")) {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/AdminMenu.fxml"));
            ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
        } else {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/MemberMenu.fxml"));
            ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refresh();
        Timeline mainTimeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> {
            refresh();
        }));
        mainTimeline.setCycleCount(Animation.INDEFINITE);
        mainTimeline.play();
    }

    private void refresh() {
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
}
