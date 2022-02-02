package view.BoardMenu;

import controller.Controller;
import controller.LoggedController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.*;
import view.TaskItemForLeaderView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class BoardMenuAddTaskPageView {

    public VBox vTaskItem;
    public Label response;
    public AnchorPane pane;
    private TableView<Category> tableView;
    @FXML
    public void initialize() {
        makeCategoriesTable();
        makeTasksVbox();

    }

    private void makeTasksVbox() {
        vTaskItem.getChildren().clear();
        ArrayList<Task> tasks = Controller.controller.getLoggedBoard().getTeam().getAllTasks();
        Node[] nodes = new Node[tasks.size()];
        for (int i = 0; i < nodes.length; i++) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TaskItem.fxml"));
                BoardMenuTaskItem controller = new BoardMenuTaskItem();
                loader.setController(controller);
                nodes[i] = loader.load();
                vTaskItem.getChildren().add(nodes[i]);
                controller.setTask(tasks.get(i));
                controller.setParent(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void makeCategoriesTable() {
        if(tableView!=null)pane.getChildren().remove(tableView);
        ObservableList<Category> list = FXCollections.observableArrayList(board.getAllCategories());
        tableView = new TableView<>();
        tableView.setLayoutX(425);
        tableView.setLayoutY(14);
        TableColumn<Category, String> categoryName = new TableColumn<>("category name");
        categoryName.setPrefWidth(140);
        tableView.setPrefWidth(140);
        tableView.setPrefHeight(302);
        categoryName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableView.getColumns().addAll(categoryName);
        tableView.setItems(list);
        tableView.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/Table.css")).toExternalForm());
        pane.getChildren().add(tableView);
    }
    public void back(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/BoardMenuSecondPageForLeader.fxml"));
        ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).setScene(new Scene(root));
    }
}
