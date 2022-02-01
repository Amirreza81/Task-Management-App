package view;

import controller.LoggedController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Task;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class TaskItemForLeaderView implements Initializable {
    public BorderPane pane;
    public Label lblTaskPriority;
    public Button btnSelect;
    public Label lblTaskTitle;
    private Task selectTask;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnSelect.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                LoggedController.getInstance().setSelectedTask(selectTask);
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource("/fxml/TaskPageForLeader.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
            }
        });
    }

    public void setTask(Task task) {
        lblTaskTitle.setText(task.getTitle());
        lblTaskPriority.setText(task.getPriority());
        selectTask = task;
    }

}
