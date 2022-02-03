package view.BoardMenu;

import controller.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Board;
import model.Team;
import model.User;

import java.io.IOException;
import java.util.Objects;

public class BoardMenuFirstPageView {
    public TextField boardName;
    public AnchorPane pane;
    public Button makeBoardButton;
    public Label response;
    public Button exitBtn;
    private TableView<Board> tableView;

    public void initialize() {
        if (Controller.controller.
                getLoggedInUser().getRole().equals("Member"))
            pane.getChildren().remove(makeBoardButton);
        makeBoardsTable();
    }

    private void makeBoardsTable() {
        if (tableView != null) pane.getChildren().remove(tableView);
        ObservableList<Board> list = FXCollections.observableArrayList(
                Controller.controller.getLoggedTeam().getBoards());
        tableView = new TableView<>();
        tableView.setLayoutX(50);
        tableView.setLayoutY(58.0);
        TableColumn<Board, String> boardName = new TableColumn<>("BoardName");
        boardName.setPrefWidth(200);
        tableView.setPrefWidth(200);
        boardName.setCellValueFactory(new PropertyValueFactory<>("boardName"));
        tableView.getColumns().addAll(boardName);
        tableView.setItems(list);
        tableView.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/Table.css")).toExternalForm());
        pane.getChildren().add(tableView);
    }

    public void loginBoard(ActionEvent actionEvent) throws IOException {
        String boardNameText = boardName.getText();
        Board board = Board.getBoardByName(Controller.controller.getLoggedTeam().getBoards(), boardNameText);
        if (board == null) {
            response.setText("there is no board with this name");
            return;
        }
        if (!board.isCreated())
            if (Controller.controller.getLoggedInUser().getRole().equals("Member")) {
                response.setText("board creation hasn't finished");
                return;
            }
        Controller.controller.setSelectedBoard(boardNameText);
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource
                ("/fxml/BoardMenuSecondPageForLeader.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void makeBoard(ActionEvent actionEvent) throws IOException {
        String boardNameText = boardName.getText();
        int controllerResponse = Controller.controller.makeBoard(boardNameText);
        if (controllerResponse == 1)
            response.setText("there is already a board with this name");
        if (controllerResponse == 2)
            response.setText("board successfully created! now u can login to board");
        makeBoardsTable();
    }


    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void back(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource
                ("/fxml/TeamMenuSecondPage.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}

