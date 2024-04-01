package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MemberMenuView {
    public Button profileInMemberMenu;
    public Button teamMenuInMemberMenu;
    public Button taskPageInMemberMenu;
    public Button calenderInMemberMenu;
    public Button logOut;

    public void goToProfile(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Profile.fxml"));
        ((Stage) profileInMemberMenu.getScene().getWindow()).setScene(new Scene(root));
    }

    public void goToTeamMenu(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/TeamMenuFirstPage.fxml"));
        ((Stage) teamMenuInMemberMenu.getScene().getWindow()).setScene(new Scene(root));
    }

    public void goToTaskPage(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/TaskList.fxml"));
        ((Stage) taskPageInMemberMenu.getScene().getWindow()).setScene(new Scene(root));
    }

    public void goToCalenderMenu(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Calender.fxml"));
        ((Stage) taskPageInMemberMenu.getScene().getWindow()).setScene(new Scene(root));
    }

    public void backToLogInPage(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        ((Stage) logOut.getScene().getWindow()).setScene(new Scene(root));
    }
}
