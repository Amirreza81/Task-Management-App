package view;

import controller.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Log;
import model.Notification;
import model.Team;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ProfileView  {
    public ImageView pictureOfUser;
    public Button editProfile;
    public Button notification;
    public Label userNameInProfilePage;
    public Label emailInProfilePage;
    public Label roleInProfilePage;
    public Label scoreInProfilePage;
    public Button back;
    public Button update;
    public Button changeUsername;
    public TextField currentUsername;
    public TextField newUsername;
    public TextField currentPassword;
    public TextField newPassword;
    public Button changePassword;
    public Button back2;
    public Button team;
    public Button log;
    public Button back3;
    public Label notifications;
    public Button updateNotifications;
    public Button updateLog;
    public Button back4;
    public Label logs;
    public Button back5;
    public Button updateTeam;
    public AnchorPane pane;
    private TableView<Team> tableView;

    public void editProfile(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/EditProfile.fxml"));
        ((Stage) editProfile.getScene().getWindow()).setScene(new Scene(root));
    }

    public void goToNotification(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/ShowNotification.fxml"));
        ((Stage) notification.getScene().getWindow()).setScene(new Scene(root));
    }

    public void backToMenu(ActionEvent actionEvent) throws IOException {
        if (Controller.controller.getLoggedInUser().getRole().equals("Member")) {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/MemberMenu.fxml"));
            ((Stage) back.getScene().getWindow()).setScene(new Scene(root));
        } else if (Controller.controller.getLoggedInUser().getRole().equals("Leader")) {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/LeaderMenu.fxml"));
            ((Stage) back.getScene().getWindow()).setScene(new Scene(root));
        }
    }

    public void updateProfile(ActionEvent actionEvent) {
        userNameInProfilePage.setText(Controller.controller.getLoggedInUser().getUserName());
        emailInProfilePage.setText(Controller.controller.getLoggedInUser().getEmail());
        roleInProfilePage.setText(Controller.controller.getLoggedInUser().getRole());
        scoreInProfilePage.setText(Integer.toString(Controller.controller.getLoggedInUser().getScore()));
    }

    public void showTeams(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/ShowTeamsInProfile.fxml"));
        ((Stage) team.getScene().getWindow()).setScene(new Scene(root));
    }

    public void showLogs(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/ShowLog.fxml"));
        ((Stage) log.getScene().getWindow()).setScene(new Scene(root));
    }

    public void changeUsername(ActionEvent actionEvent) {
        if (currentUsername.getText().equals("") || newUsername.getText().equals("") || currentUsername.getText() == null || newUsername.getText() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setContentText("You should fill the fields!");
            alert.showAndWait();
            return;
        }
        int response = Controller.controller.changeUserName(newUsername.getText());
        if (response == 1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setContentText("Your new username must include at least 4 characters!");
            alert.showAndWait();
        } else if (response == 2) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setContentText("username already taken !");
            alert.showAndWait();
        } else if (response == 3) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setContentText("New username contains Special Characters! Please remove them and try again");
            alert.showAndWait();
        } else if (response == 4) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setContentText("you already have this username !");
            alert.showAndWait();
        } else if (response == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Change username successfully!");
            alert.showAndWait();
        }

    }

    public void changePassword(ActionEvent actionEvent) {
        if (currentPassword.getText().equals("") || newPassword.getText().equals("") || currentPassword.getText() == null || newPassword.getText() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setContentText("You should fill the fields!");
            alert.showAndWait();
            return;
        }
        int response = Controller.controller.changePassword(Controller.controller.getLoggedInUser(), currentPassword.getText(), newPassword.getText());
        if (response == 1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setContentText("wrong old password !");
            alert.showAndWait();
        } else if (response == 2) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setContentText("Please type a New Password !");
            alert.showAndWait();
        } else if (response == 3) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setContentText("Please Choose A strong Password (Containing at least 8 characters including 1 digit \" +\n" +
                    "                    \"and 1 Capital Letter)");
            alert.showAndWait();
        } else if (response == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Change password successfully !");
            alert.showAndWait();
        }
    }

    public void backToProfilePage(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Profile.fxml"));
        ((Stage) back2.getScene().getWindow()).setScene(new Scene(root));
    }

    public void backToProfile(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Profile.fxml"));
        ((Stage) back3.getScene().getWindow()).setScene(new Scene(root));
    }

    public void updateNotifications(ActionEvent actionEvent) {
        if (LoggedController.getInstance().getLoggedInUser().getNotifications().size() == 0) {
            notifications.setText("No notification for you!");
            return;
        }
        ArrayList<String> allNotifications = new ArrayList<>();
        int rank = 1;
        for (Notification notification : LoggedController.getInstance().getLoggedInUser().getNotifications()) {
            allNotifications.add(rank + ". From " + notification.getSender().getUserName() + ":\n" + notification.getText() + "\n");
            rank++;
        }
        notifications.setText(allNotifications.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
    }

    public void updateLog(ActionEvent actionEvent) {
        ArrayList<String> showLogs = new ArrayList<>();
        int rank = 1;
        for (Log log : LoggedController.getInstance().getLoggedInUser().getAllLogs()) {
            showLogs.add(rank + ". " + log.getDate());
            rank++;
        }
        logs.setText(showLogs.toString().replaceAll(",", "\n").replaceAll("\\[", "").replaceAll("\\]", ""));
    }

    public void backToTheProfile(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Profile.fxml"));
        ((Stage) back4.getScene().getWindow()).setScene(new Scene(root));
    }

    public void updateTeam(ActionEvent actionEvent) {
        if(tableView!=null)pane.getChildren().remove(tableView);
        ObservableList<Team> list = FXCollections.observableArrayList(LoggedController.getInstance().getLoggedInUser().getUserTeams());
        tableView = new TableView<>();
        tableView.setLayoutX(50);
        tableView.setLayoutY(58.0);
        TableColumn<Team, String> teamName = new TableColumn<>("TeamName");
        teamName.setPrefWidth(200);
        tableView.setPrefWidth(200);
        teamName.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        tableView.getColumns().addAll(teamName);
        tableView.setItems(list);
        tableView.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/Table.css")).toExternalForm());
        pane.getChildren().add(tableView);
    }

    public void backToTheProfile2(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Profile.fxml"));
        ((Stage) back5.getScene().getWindow()).setScene(new Scene(root));
    }
}
