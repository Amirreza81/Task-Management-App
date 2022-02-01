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
import model.Date;
import model.Task;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CalenderView implements Initializable {
    public AnchorPane pane;
    public ImageView exit;
    public Button leave;
    public VBox vTaskItem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<Task> tasks = LoggedController.getInstance().getLoggedInUser().getAllTasksForUser();
        Node[] nodes = new Node[tasks.size()];
        for (int i = 0; i < nodes.length; i++) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CalenderItem.fxml"));
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
        if (LoggedController.getInstance().getLoggedInUser().getRole().equals("Leader")) {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/LeaderMenu.fxml"));
            ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
        } else if (LoggedController.getInstance().getLoggedInUser().getRole().equals("Admin")) {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/AdminMenu.fxml"));
            ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
        } else {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/MemberMenu.fxml"));
            ((Stage) pane.getScene().getWindow()).setScene(new Scene(root));
        }
    }
}
