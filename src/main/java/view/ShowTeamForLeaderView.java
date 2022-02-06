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

public class ShowTeamForLeaderView implements Initializable {
    public AnchorPane pane;
    public ImageView exit;
    public Button showTeams;
    public Button sudoTask;
    public TextField teamTitleField;
    public ChoiceBox members;
    public ListView membersList;
    public Button addMember;
    public Button deleteMember;
    public Label lblError;
    public Button suspendMember;
    public Button leave;
    private int result;


    public void exit(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void goToShowTeams(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/ShowTeamsForLeader.fxml"));
        ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
    }

    public void goToSUdoTask(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Sudo.fxml"));
        ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
    }

    public void addMember(ActionEvent actionEvent) throws IOException {
        if (membersList.getItems().contains(members.getValue().toString()))
            lblError.setText("Old added to list!");
        else {
            membersList.getItems().add(members.getValue().toString());
            result = Controller.controller.addMember(members.getValue().toString());
            lblError.setText("User successfully added!");
        }
    }

    public void deleteMember(ActionEvent actionEvent) throws IOException {
        String selectedItem = membersList.getSelectionModel().getSelectedItem().toString();
        result = Controller.controller.deleteMember(selectedItem);
        lblError.setText("User successfully removed");
        membersList.getItems().clear();
        for (User user : Controller.controller.getSelectedTeam().getTeamMembers()) {
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
        Team selectTeam = getTeam();
        teamTitleField.setText(selectTeam.getTeamName());
        for (User user : selectTeam.getTeamMembers()) {
            membersList.getItems().add(user.getUserName());
        }
        for (User member : User.getUsers()) {
            members.getItems().add(member.getUserName());
        }
        members.setValue(User.getUsers().get(0).getUserName());
    }

    public Team getTeam() {
        return Controller.controller.getSelectedTeam();
    }

    public void leave(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/LeaderMenu.fxml"));
        ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
    }

    public void suspendMember(ActionEvent actionEvent) throws IOException {
        String selectedItem = membersList.getSelectionModel().getSelectedItem().toString();
        result = Controller.controller.suspendMember(selectedItem);
        lblError.setText("User successfully suspended");
        membersList.getItems().clear();
        for (User user : Controller.controller.getSelectedTeam().getTeamMembers()) {
            membersList.getItems().add(user.getUserName());
        }
    }
}
