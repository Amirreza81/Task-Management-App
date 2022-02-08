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

public class TaskPageForLeaderView implements Initializable {
    public AnchorPane pane;
    public TextField deadlineFiled;
    public TextField descriptionField;
    public TextField PriorityField;
    public TextField taskTitleField;
    public Button btnTaskList;
    public Button btnEdit;
    public Label lblError;
    public Label lblDateOfCreation;
    public ListView membersList;
    public Button btnCreateTask;
    public ImageView exit;
    public Button DeleteMember;
    public Button leave;
    public Button AddMember;
    public ChoiceBox members;
    private int result;

    public void goToTaskList(ActionEvent actionEvent) throws IOException {
        Task.setSelectTask(null);
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/TaskListForLeader.fxml"));
        ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refresh();
    }

    private void refresh() {
        Task selectedTask = getTask();
        taskTitleField.setText(selectedTask.getTitle());
        deadlineFiled.setText(selectedTask.getDeadline().toString());
        PriorityField.setText(selectedTask.getPriority());
        lblDateOfCreation.setText(selectedTask.getDateOfCreation().toString());
        if (selectedTask.getDescription().equals(""))
            descriptionField.setText("description is null!");
        else
            descriptionField.setText(selectedTask.getDescription());
        membersList.getItems().clear();
        for (User user : selectedTask.getAssignedUser()) {
            membersList.getItems().add(user.getUserName());
        }
        members.getItems().clear();
        for (User member : Controller.controller.getSelectedTeamForTask().getTeamMembers()) {
            members.getItems().add(member.getUserName());
        }
        members.setValue(Controller.controller.getSelectedTeamForTask().getTeamMembers().get(0).getUserName());
    }

    public Task getTask() {
        return Controller.controller.getSelectedTask();
    }

    public void Edit(ActionEvent actionEvent) throws ParseException, IOException {
        Task selectTask = getTask();
        if (!taskTitleField.getText().equals(selectTask.getTitle())) {
            Controller.controller.editTaskTitle(taskTitleField.getText());
        }
        if (!PriorityField.getText().equals(selectTask.getPriority()))
            Controller.controller.editTaskPriority(PriorityField.getPromptText());
        if (!descriptionField.getText().equals(selectTask.getDescription()))
            Controller.controller.editTaskDescription(descriptionField.getText());
        if (!deadlineFiled.getText().equals(selectTask.getDeadline().toString()))
            result = Controller.controller.editTaskDeadline(taskTitleField.getText());
        if (result == 1) {
            lblError.setText("New deadline is invalid!");
        } else
            lblError.setText("Task updated successfully!");
    }

    public void goToCreateTask(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/CreateTaskPageForLeader.fxml"));
        ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
    }

    public void exit(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void deleteMember(ActionEvent actionEvent) throws IOException {
        Task selectTask = getTask();
        String selectedItem = membersList.getSelectionModel().getSelectedItem().toString();
        result = Controller.controller.removeAssignedUsers(selectedItem);
        membersList.getItems().clear();
        for (User user : selectTask.getAssignedUser()) {
            membersList.getItems().add(user.getUserName());
        }
    }

    public void leave(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/LeaderMenu.fxml"));
        ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
    }

    public void addMember(ActionEvent actionEvent) throws IOException {
        Task selectTask = getTask();
        if (membersList.getItems().contains(members.getValue().toString()))
            lblError.setText("Old added to list!");
        else {
            membersList.getItems().add(members.getValue().toString());
            result = Controller.controller.assignMember(String.valueOf(selectTask.getCreationId()),
                    members.getValue().toString());
            Controller.controller.sendNotificationForTask(String.valueOf(selectTask.getCreationId()),
                    members.getValue().toString());
            lblError.setText("User successfully added!");
        }
    }
}
