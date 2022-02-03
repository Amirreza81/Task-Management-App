package view.BoardMenu;

import controller.Controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import model.Board;
import model.Category;
import model.Task;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class BoardMenuTaskItem implements Initializable {
    public BorderPane pane;
    public Label lblTaskPriority;
    public Button btnSelect;
    public Label lblTaskTitle;
    private Task selectTask;
    private BoardMenuAddTaskPageView parent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnSelect.setText("addTask");
        btnSelect.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Add Task");
                dialog.setHeaderText("Enter Category Name in which you want to add this task");
                dialog.setContentText("Category Name : ");
                Optional<String> input = dialog.showAndWait();
                if (input.isPresent()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    Category category = Category.getCategoryByName
                            (Controller.controller.getLoggedBoard().getAllCategories(), dialog.getEditor().getText());
                    if (category == null) {
                        alert.setAlertType(Alert.AlertType.ERROR);
                        alert.setContentText("invalid Category name");
                    }
                    int response = 0;
                    try {
                        response = Controller.controller.boardAddTask
                                (Integer.toString(selectTask.getCreationId()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (response == 4) {
                        alert.setAlertType(Alert.AlertType.ERROR);
                        alert.setContentText("this task is already in board");
                    }
                    if (response == 5) {
                        alert.setAlertType(Alert.AlertType.ERROR);
                        alert.setContentText("this task has been expired");
                    }
                    if (response == 6) {
                        alert.setAlertType(Alert.AlertType.ERROR);
                        alert.setContentText("nobody has assigned to this task");
                    }
                    if (response == 7) {
                        try {
                            Controller.controller.forceCategory
                                    (dialog.getEditor().getText(), selectTask.getTitle());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        alert.setAlertType(Alert.AlertType.INFORMATION);
                        alert.setContentText("task added to board successfully!");
                    }
//                    DialogPane dialogPane = alert.getDialogPane();
//                    dialogPane.getStylesheets().add(Objects.requireNonNull(parent.getClass().getResource("/CSS/DialogPane.css")).toExternalForm());
//                    dialogPane.getStyleClass().add("DialogPane");
                    alert.showAndWait();
                }

            }
        });
    }

    public void setTask(Task task) {
        lblTaskTitle.setText(task.getTitle());
        lblTaskPriority.setText(task.getPriority());
        selectTask = task;
    }

    public void setParent(BoardMenuAddTaskPageView parent) {
        this.parent = parent;
    }
}
