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
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Task;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;

public class CreateTaskPageForLeaderView implements Initializable {
    public Button btnTaskList;
    public TextField deadlineFiled;
    public TextField descriptionField;
    public TextField taskTitleField;
    public Button btnCreate;
    public Label lblError;
    public AnchorPane pane;
    public TextField startTimeField;
    public ChoiceBox priorityChoice;
    public ChoiceBox members;
    public ListView membersList;
    public Button addMember;
    public ImageView exit;
    public Button btnLeave;
    private int result;
    private Task task;

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
        priorityChoice.getItems().addAll("Lowest", "Low", "High", "Highest");
        priorityChoice.setValue("Lowest");
        members.getItems().clear();
        for (User member : Controller.controller.getSelectedTeamForTask().getTeamMembers()) {
            members.getItems().add(member.getUserName());
        }
        members.setValue(Controller.controller.getSelectedTeamForTask().getTeamMembers().get(0).getUserName());
    }

    public void goToTaskList(ActionEvent actionEvent) throws IOException {
        Task.setSelectTask(null);
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/TaskListForLeader.fxml"));
        ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
    }

    public void Create(ActionEvent actionEvent) throws ParseException, IOException {
        if (taskTitleField.getText() == null || priorityChoice.getValue().toString() == null || deadlineFiled.getText() == null)
            lblError.setText("Fill in all the fields");
        else {
            result = Controller.controller.creatTask(taskTitleField.getText(), startTimeField.getText(), deadlineFiled.getText(), descriptionField.getText(), priorityChoice.getValue().toString());
            if (result == 1) {
                lblError.setText("There is another task with this title!");
            } else if (result == 2) {
                lblError.setText("Invalid start date!");
            } else if (result == 3) {
                lblError.setText("Invalid deadline!");
            } else if (result == 4) {
                membersList.getItems().clear();
                task = Task.getTaskByTitle(Controller.controller.getSelectedTeamForTask().getAllTasks(), taskTitleField.getText());
                lblError.setText("Task created successfully!");
            }

        }

    }


    public void addMember(ActionEvent actionEvent) throws IOException {
        if (membersList.getItems().contains(members.getValue().toString()))
            lblError.setText("Old added to list!");
        else {
            membersList.getItems().add(members.getValue().toString());
            result = Controller.controller.assignMember(String.valueOf(task.getCreationId()),
                    members.getValue().toString());
            Controller.controller.sendNotificationForTask(String.valueOf(task.getCreationId()),
                    members.getValue().toString());
            lblError.setText("User successfully added!");
        }
    }

    public void exit(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void leave(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/LeaderMenu.fxml"));
        ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
    }
}
