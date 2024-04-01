package view;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import model.Task;

import java.net.URL;
import java.util.ResourceBundle;

public class SudoItemView implements Initializable {
    public BorderPane pane;
    public Label lblTaskPriority;
    public Label lblTaskTitle;
    private Task selectTask;

    public void setTask(Task task) {
        lblTaskTitle.setText(task.getTitle());
        lblTaskPriority.setText(task.getPriority());
        selectTask = task;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
