package view;

import controller.JsonController;
import controller.LoggedController;
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
import model.Task;
import model.Team;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        priorityChoice.getItems().addAll("Lowest", "Low", "High", "Highest");
        priorityChoice.setValue("Lowest");
        for (User member : Team.getTeamByName("Yakuza2", Team.getAllTeams()).getTeamMembers()) {
            members.getItems().add(member.getUserName());
        }
        members.setValue(Team.getTeamByName("Yakuza2", Team.getAllTeams()).getTeamMembers().get(0).getUserName());
    }

    public void goToTaskList(ActionEvent actionEvent) throws IOException {
        Task.setSelectTask(null);
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/TaskListForLeader.fxml"));
        ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
    }

    public void Create(ActionEvent actionEvent) throws ParseException {
        if (taskTitleField.getText() == null || priorityChoice.getValue().toString() == null || deadlineFiled.getText() == null)
            lblError.setText("Fill in all the fields");
        else {
            result = controller.controller.creatTask(LoginView.LoginUser, LoggedController.getInstance().getSelectedTeamForTask(), taskTitleField.getText(), startTimeField.getText(), deadlineFiled.getText(), descriptionField.getText(), priorityChoice.getValue().toString());
            if (result == 1) {
                lblError.setText("There is another task with this title!");
            } else if (result == 2) {
                lblError.setText("Invalid start date!");
            } else if (result == 3) {
                lblError.setText("Invalid deadline!");
            } else if (result == 4) {
                membersList.getItems().clear();
                lblError.setText("Task created successfully!");
            }

        }

    }


    public void addMember(ActionEvent actionEvent) {
        if (membersList.getItems().contains(members.getValue().toString()))
            lblError.setText("Old added to list!");
        else {
            membersList.getItems().add(members.getValue().toString());
            result = controller.controller.assignMember(LoginView.LoginUser,
                    Team.getTeamByName("Yakuza2", Team.getAllTeams()),
                    String.valueOf(Task.getAllTasks().get(Task.getAllTasks().size() - 1).getCreationId()),
                    members.getValue().toString());
            lblError.setText("User successfully added!");
        }
    }

    public void exit(MouseEvent mouseEvent) {
        JsonController.getInstance().updateJson();
        System.exit(0);
    }

    public void leave(ActionEvent actionEvent) throws IOException {
        LoggedController.getInstance().setSelectedTeam(null);
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/LeaderMenu.fxml"));
        ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
    }
}
