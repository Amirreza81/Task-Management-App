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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Task;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;

public class TaskPageForLeaderView implements Initializable {
    private final Task selectTask = LoggedController.getInstance().getSelectedTask();
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
    private int result;

    public void goToTaskList(ActionEvent actionEvent) throws IOException {
        Task.setSelectTask(null);
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/TaskListForLeader.fxml"));
        ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        taskTitleField.setText(selectTask.getTitle());
        deadlineFiled.setText(selectTask.getDeadline().toString());
        PriorityField.setText(selectTask.getPriority());
        lblDateOfCreation.setText(selectTask.getDateOfCreation().toString());
        if (selectTask.getDescription().equals(""))
            descriptionField.setText("description is null!");
        else
            descriptionField.setText(selectTask.getDescription());
        for (User user : selectTask.getAssignedUser()) {
            membersList.getItems().add(user.getUserName());
        }

    }

    public void Edit(ActionEvent actionEvent) throws ParseException {
        if (!taskTitleField.getText().equals(selectTask.getTitle())) {
            controller.controller.editTaskTitle(LoginView.LoginUser, selectTask, taskTitleField.getText());
        }
        if (!PriorityField.getText().equals(selectTask.getPriority()))
            controller.controller.editTaskPriority(LoginView.LoginUser, selectTask, taskTitleField.getText());
        if (!descriptionField.getText().equals(selectTask.getDescription()))
            controller.controller.editTaskDescription(LoginView.LoginUser, selectTask, taskTitleField.getText());
        if (!deadlineFiled.getText().equals(selectTask.getDeadline()))
            result = controller.controller.editTaskDeadline(LoginView.LoginUser, selectTask, taskTitleField.getText());
        if (result == 1) {
            lblError.setText("New deadline is invalid!");
        } else
            lblError.setText("Task updated successfully!");
    }

    public void goToCreateTask(ActionEvent actionEvent) throws IOException {
        LoggedController.getInstance().setSelectedTask(null);
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/CreateTaskPageForLeader.fxml"));
        ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
    }

    public void exit(MouseEvent mouseEvent) {
        LoggedController.getInstance().setSelectedTask(null);
        JsonController.getInstance().updateJson();
        System.exit(0);
    }

    public void deleteMember(ActionEvent actionEvent) {
        String selectedItem = membersList.getSelectionModel().getSelectedItem().toString();
        result = controller.controller.removeAssignedUsers(User.getUserByUsername("Amir"), selectTask, User.getUserByUsername(selectedItem));
        membersList = null;
        for (User user : selectTask.getAssignedUser()) {
            membersList.getItems().add(user.getUserName());
        }
    }

    public void leave(ActionEvent actionEvent) throws IOException {
        LoggedController.getInstance().setSelectedTeam(null);
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/LeaderMenu.fxml"));
        ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
    }
}
