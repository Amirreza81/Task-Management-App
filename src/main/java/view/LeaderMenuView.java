package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class LeaderMenuView {
    public Button profileInLeaderMenu;
    public Button teamMenuInLeaderMenu;
    public Button taskPageInLeaderMenu;
    public Button calenderInLeaderMenu;
    public Button logOut;
    public Button specialCommands;

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
}
