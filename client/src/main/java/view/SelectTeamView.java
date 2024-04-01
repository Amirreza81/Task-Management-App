package view;

import controller.Controller;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Team;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SelectTeamView implements Initializable {
    public AnchorPane pane;
    public ImageView exit;
    public Button leave;
    public VBox vTeamItem;

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
        vTeamItem.getChildren().clear();
        ArrayList<Team> teams = new ArrayList<>(Controller.controller.getLoggedInUser().getUserTeams());
        Node[] nodes = new Node[teams.size()];
        for (int i = 0; i < nodes.length; i++) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SelectItem.fxml"));
                SelectItemView controller = new SelectItemView();
                loader.setController(controller);
                nodes[i] = loader.load();
                vTeamItem.getChildren().add(nodes[i]);
                controller.setTeam(teams.get(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void exit(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void leave(ActionEvent actionEvent) throws IOException {
        if (Controller.controller.getLoggedInUser().getRole().equals("Leader")) {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/LeaderMenu.fxml"));
            ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
        } else {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/MemberMenu.fxml"));
            ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
        }
    }
}
