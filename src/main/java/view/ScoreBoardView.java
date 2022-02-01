package view;

import controller.LoggedController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;

import java.util.Objects;


public class ScoreBoardView {
    public AnchorPane pane;
    private User user;

    @FXML
    public void initialize() {

        user = LoggedController.getInstance().getLoggedInUser();
        ScoreBoardData.getAndSetDataFromUser();
        ObservableList<ScoreBoardData> list = FXCollections.observableArrayList(ScoreBoardData.getScoreBoardData());
        TableView<ScoreBoardData> tableView = new TableView<>();
        tableView.setLayoutX(76.0);
        tableView.setLayoutY(58.0);
        TableColumn<ScoreBoardData, Integer> rank = new TableColumn<>("Rank");
        rank.setPrefWidth(45);
        TableColumn<ScoreBoardData, String> username = new TableColumn<>("Username");
        username.setPrefWidth(200);
        TableColumn<ScoreBoardData, Integer> score = new TableColumn<>("Score");
        score.setPrefWidth(83);
        tableView.setPrefWidth(320);
        rank.setCellValueFactory(new PropertyValueFactory<>("rank"));
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        score.setCellValueFactory(new PropertyValueFactory<>("score"));

        tableView.getColumns().addAll(rank, username, score);
        tableView.setItems(list);
        tableView.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/Scoreboard.css")).toExternalForm());
        pane.getChildren().add(tableView);
    }

    public void backToMain(ActionEvent actionEvent) throws IOException {
        Parent url = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/TeamMenuSecondPage.fxml")));
        Scene scene = new Scene(url);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}
