package view;

import controller.LoggedController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Board;
import model.Team;
import model.User;

import java.io.IOException;
import java.util.Objects;

public class TeamMenuFirstPageView {

    public TextField teamName;
    public Label response;
    public AnchorPane pane;

    private User user;
    private TableView<Team> tableView;
    public void initialize() {
        user = LoggedController.getInstance().getLoggedInUser();
        makeBoardsTable();
    }
    private void makeBoardsTable() {
        if(tableView!=null)pane.getChildren().remove(tableView);
        ObservableList<Team> list = FXCollections.observableArrayList(user.getUserTeams());
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

    public void loginTeam(ActionEvent actionEvent) throws IOException {
        String teamNameText = teamName.getText();
        Team team = Team.getTeamByName(teamNameText,user.getUserTeams());
        if(team==null){
            response.setText("there is no team with this name");
            return;
        }
        LoggedController.getInstance().setLoggedTeam(team);
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource
                ("/fxml/TeamMenuSecondPage.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void back(ActionEvent actionEvent) throws IOException {
        if (user.getRole().equals("Leader")){
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/LeaderMenu.fxml"));
            ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
        }
        else {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/MemberMenu.fxml"));
            ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
        }
    }
}
