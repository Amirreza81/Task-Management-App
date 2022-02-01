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

public class TeamItemView implements Initializable {
    public Label lblStatus;
    public Button btnSelect;
    public Label lblTeamTitle;
    public BorderPane pane;
    private Team selectTeam;

    public void setTeam(Team team) {
        lblTeamTitle.setText(team.getTeamName());
        if (Team.getPendingTeams().contains(team))
            lblStatus.setText("Pending");
        else
            lblStatus.setText("accept");
        selectTeam = team;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnSelect.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                LoggedController.getInstance().setSelectedTeam(selectTeam);
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource("/fxml/ShowTeamForLeader.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
            }
        });
    }
}
