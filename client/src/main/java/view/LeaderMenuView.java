package view;

import controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.Console;
import java.io.IOException;

public class LeaderMenuView {
    public Button profileInLeaderMenu;
    public Button teamMenuInLeaderMenu;
    public Button taskPageInLeaderMenu;
    public Button calenderInLeaderMenu;
    public Button logOut;
    public Button specialCommands;
    public Button inviteButton;
    public TextField emailOfFriend;
    public Button inviteButtonInInvitePage;
    public Button back7;
    public TextField nameOfTeam;

    public void goToProfile(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Profile.fxml"));
        ((Stage) profileInLeaderMenu.getScene().getWindow()).setScene(new Scene(root));
    }

    public void goToTeamMenu(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/TeamMenuFirstPage.fxml"));
        ((Stage) taskPageInLeaderMenu.getScene().getWindow()).setScene(new Scene(root));
    }

    public void goToTaskPage(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/SelectTeam.fxml"));
        ((Stage) taskPageInLeaderMenu.getScene().getWindow()).setScene(new Scene(root));
    }

    public void goToCalenderMenu(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Calender.fxml"));
        ((Stage) taskPageInLeaderMenu.getScene().getWindow()).setScene(new Scene(root));
    }

    public void backToLogInPage(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        ((Stage) logOut.getScene().getWindow()).setScene(new Scene(root));
    }

    public void goToSpecialCommandsForLeader(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/SpecialCommandsForLeader.fxml"));
        ((Stage) specialCommands.getScene().getWindow()).setScene(new Scene(root));
    }

    public void goToInviteForLeader(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Invite.fxml"));
        ((Stage) inviteButton.getScene().getWindow()).setScene(new Scene(root));
    }

    public void inviteFriend(ActionEvent actionEvent) throws IOException {
        String email = emailOfFriend.getText();
        String teamName = nameOfTeam.getText();
        int response = Controller.controller.invite(email, teamName);
        if (response == 1){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("You invited your friends with this email ( " + email + " ) successfully!");
            alert.showAndWait();
        }
    }

    public void backToLeaderMenuView(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/LeaderMenu.fxml"));
        ((Stage) back7.getScene().getWindow()).setScene(new Scene(root));
    }
}
