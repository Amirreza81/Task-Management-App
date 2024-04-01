package view.BoardMenu;

import controller.Controller;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import model.Board;
import model.Task;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class InCategoryItemsView implements Initializable {
    public Button btnSelect;
    private Task task;
    private CategoryItemView parentController;
    @FXML
    public void initialize(URL location, ResourceBundle resources){
        btnSelect.setStyle("-fx-background-color: #eecd26; ");
    }
    public void setTask(Task task){
        this.task = task;
        btnSelect.setText(task.getTitle());
    }
    public void goToNextCategory() throws IOException {
        boolean confirmation = false;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("confirmation");
        alert.setContentText("are u sure u want to put this task to next category?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) confirmation = true;
        if(confirmation){
            int response = Controller.controller.goToNextCategory(task.getTitle());
            if (response==0){
                parentController.getParentController().response.setText("this task is not assigned to you so u cant move it");
            }
            else {
                parentController.getParentController().updateHBOX();
                parentController.getParentController().makeDoneColumn();
            }
        }
    }

    public void setParentController(CategoryItemView parentController) {
        this.parentController = parentController;
    }
}
