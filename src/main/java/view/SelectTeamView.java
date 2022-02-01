package view;

import controller.JsonController;
import controller.LoggedController;
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
        ArrayList<Team> teams = new ArrayList<>(LoggedController.getInstance().getLoggedInUser().getUserTeams());
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
        JsonController.getInstance().updateJson();
        System.exit(0);
    }

    public void leave(ActionEvent actionEvent) throws IOException {
        if (LoggedController.getInstance().getLoggedInUser().getRole().equals("Leader")) {
            LoggedController.getInstance().setSelectedTeam(null);
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/LeaderMenu.fxml"));
            ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
        } else {
            LoggedController.getInstance().setSelectedTeam(null);
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/MemberMenu.fxml"));
            ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
        }
    }
}
