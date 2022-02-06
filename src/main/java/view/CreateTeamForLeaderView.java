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
import model.Team;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateTeamForLeaderView implements Initializable {
    public ImageView exit;
    public Button showTeams;
    public TextField teamTitleField;
    public Button btnCreate;
    public ChoiceBox members;
    public ListView membersList;
    public Button addMember;
    public Button deleteMember;
    public AnchorPane pane;
    public Label error;
    public Button leave;
    public Button suspendMember;
    private Team selectedTeam = null;
    private int result;

    public void exit(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void goToShowTeams(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/ShowTeamsForLeader.fxml"));
        ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
    }

    public void Create(ActionEvent actionEvent) throws IOException {
        int status = Controller.controller.creatTeam(teamTitleField.getText());
        if (status == 1)
            error.setText("There is another team with this name!");
        else if (status == 2)
            error.setText("Team name is invalid!");
        else if (status == 3) {
            error.setText("Team created successfully! Waiting For Admin’s confirmation…");
            selectedTeam = Team.getTeamByName(teamTitleField.getText(), Team.getAllTeams());
        }
    }

    public void addMember(ActionEvent actionEvent) throws IOException {

        if (selectedTeam == null) {
            error.setText("firs create team!");
        } else if (membersList.getItems().contains(members.getValue().toString()))
            error.setText("Old added to list!");
        else {
            membersList.getItems().add(members.getValue().toString());
            result = Controller.controller.addMember(members.getValue().toString());
            error.setText("User successfully added!");
        }
    }

    public void deleteMember(ActionEvent actionEvent) throws IOException {
        String selectedItem = membersList.getSelectionModel().getSelectedItem().toString();
        result = Controller.controller.deleteMember(selectedItem);
        error.setText("User successfully removed");
        membersList.getItems().clear();
        for (User user : selectedTeam.getTeamMembers()) {
            membersList.getItems().add(user.getUserName());
        }
    }

    public void leave(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/LeaderMenu.fxml"));
        ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
    }

    public void suspendMember(ActionEvent actionEvent) throws IOException {
        String selectedItem = membersList.getSelectionModel().getSelectedItem().toString();
        result = Controller.controller.suspendMember(selectedItem);
        error.setText("User successfully suspended");
        membersList.getItems().clear();
        for (User user : selectedTeam.getTeamMembers()) {
            membersList.getItems().add(user.getUserName());
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
        for (User member : Controller.controller.getAllUsers()) {
            members.getItems().add(member.getUserName());
        }
        members.setValue(Controller.controller.getAllUsers().get(0).getUserName());
    }
}
