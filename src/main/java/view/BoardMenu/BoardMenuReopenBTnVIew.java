package view.BoardMenu;

import controller.Controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import model.Board;
import model.Task;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Optional;
import java.util.ResourceBundle;

public class BoardMenuReopenBTnVIew implements Initializable {
    public BorderPane pane;
    public Label lblTaskPriority;
    public Button btnSelect;
    public Label lblTaskTitle;
    private Task selectTask;
    private BoardMenuFailedTasksPageView parent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btnSelect.setStyle("-fx-background-color: #ee4f4f; ");
        btnSelect.setText("reopen");
        if (Controller.controller.getLoggedInUser().getRole().equals("Member"))pane.getChildren().remove(btnSelect);
        btnSelect.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("ReOpen TASK");
                dialog.setHeaderText("Enter new deadline");
                dialog.setContentText("new deadline :");
                Optional<String> input = dialog.showAndWait();
                if (input.isPresent()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    int response = 0;
                    try {
                        response = Controller.controller.updateDeadline(selectTask.getTitle(),
                                dialog.getEditor().getText());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (response == 2) {
                        alert.setAlertType(Alert.AlertType.ERROR);
                        alert.setContentText("board construction is not done");
                    }
                    if (response == 5) {
                        alert.setAlertType(Alert.AlertType.ERROR);
                        alert.setContentText("wrong date format");
                    }
                    if (response == 6) {
                        alert.setAlertType(Alert.AlertType.ERROR);
                        alert.setContentText("task successfully reopened");
                    }
//                    DialogPane dialogPane = alert.getDialogPane();
//                    dialogPane.getStylesheets().add(Objects.requireNonNull(parent.getClass().getResource("/CSS/DialogPane.css")).toExternalForm());
//                    dialogPane.getStyleClass().add("DialogPane");
                    alert.showAndWait();
                    try {
                        parent.makeTasksVbox();
                    } catch (ParseException | IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    public void setTask(Task task) {
        lblTaskTitle.setText(task.getTitle());
        lblTaskPriority.setText(task.getPriority());
        selectTask = task;
    }

    public void setParent(BoardMenuFailedTasksPageView parent) {
        this.parent = parent;
    }
}
