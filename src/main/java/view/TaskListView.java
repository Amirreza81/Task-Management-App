package view;

import controller.JsonController;
import controller.LoggedController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import model.Task;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TaskListView implements Initializable {
    @FXML
    public VBox vTaskItem;
    public ImageView exit;
    public Button leave;
    public AnchorPane pane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<Task> tasks = LoggedController.getInstance().getLoggedInUser().getAllTasksForUser();
        Node[] nodes = new Node[tasks.size()];
        for (int i = 0; i < nodes.length; i++) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TaskItem.fxml"));
                TaskItemView controller = new TaskItemView();
                loader.setController(controller);
                nodes[i] = loader.load();
                vTaskItem.getChildren().add(nodes[i]);
                controller.setTask(tasks.get(i));
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
        LoggedController.getInstance().setSelectedTeam(null);
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/LeaderMenu.fxml"));
        ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
    }
}
