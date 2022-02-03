package view.BoardMenu;

import controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import model.Category;
import model.Task;

import java.io.IOException;
import java.util.ArrayList;

public class CategoryItemView {
    public VBox Vbox;
    public Label lblTaskTitle;
    private Category category;
    private BoardMenuSecondPageForLeaderView parentController;
    int column;

    public void setCategory(Category category,int column){
        this.column=column;
        this.category = category;
    }
    public void updateCategory(){
        lblTaskTitle.setText(category.getName());
        Vbox.getChildren().clear();
        ArrayList<Task> tasks = category.getCategoryTasks();
        Node[] nodes = new Node[tasks.size()];
        for (int i = 0; i < nodes.length; i++) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/InCategoryItems.fxml"));
                nodes[i] = loader.load();
                Vbox.getChildren().add(nodes[i]);
                InCategoryItemsView inCategoryItemsView = loader.getController();
                inCategoryItemsView.setTask(tasks.get(i));
                inCategoryItemsView.setParentController(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public BoardMenuSecondPageForLeaderView getParentController() {
        return parentController;
    }

    public void setParentController(BoardMenuSecondPageForLeaderView parentController) {
        this.parentController = parentController;
    }

    public void handleDragDetection(MouseEvent mouseEvent) {
        Dragboard dragboard = lblTaskTitle.startDragAndDrop(TransferMode.ANY);
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(lblTaskTitle.getText());
        dragboard.setContent(clipboardContent);
        mouseEvent.consume();
    }

    public void handleDragOver(DragEvent dragEvent) {
        if(dragEvent.getDragboard().hasString()){
            dragEvent.acceptTransferModes(TransferMode.ANY);
        }
    }

    public void handleDragDropping(DragEvent dragEvent) throws IOException {
        String categoryName = dragEvent.getDragboard().getString();
        Controller.controller.changeColumn(categoryName,this.column);
    }

    public void handleDragDone(DragEvent dragEvent) {
        parentController.updateHBOX();
    }
}
