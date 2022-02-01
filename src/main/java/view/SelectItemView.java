package view;

import controller.LoggedController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Team;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SelectItemView implements Initializable {
    public Label lblRole;
    public Button btnSelect;
    public Label lblTeamTitle;
    public BorderPane pane;
    private Team selectTeam;

    public void setTeam(Team team) {
        lblTeamTitle.setText(team.getTeamName());
        if (LoggedController.getInstance().getLoggedInUser().getUserName().equals(team.getTeamLeader().getUserName()))
            lblRole.setText("leader");
        else
            lblRole.setText("member");
        selectTeam = team;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnSelect.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                LoggedController.getInstance().setSelectedTeamForTask(selectTeam);
                Parent root = null;
                if (LoggedController.getInstance().getLoggedInUser().getUserName().equals(selectTeam.getTeamLeader().getUserName())) {
                    try {
                        root = FXMLLoader.load(getClass().getResource("/fxml/TaskListForLeader.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        root = FXMLLoader.load(getClass().getResource("/fxml/TaskList.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
            }
        });
    }
}
