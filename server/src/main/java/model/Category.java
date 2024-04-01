package model;

import java.util.ArrayList;

public class Category {

    private Board board;
    private String name;
    private int column;
    private final ArrayList<Task> categoryTasks;

    public Category(Board board, String name) {
        this.board = board;
        this.name = name;
        this.categoryTasks = new ArrayList<>();
        this.board.getAllCategories().add(this);
    }

    public ArrayList<Task> getCategoryTasks() {
        return categoryTasks;
    }

    public Board getBoard() {
        return board;
    }

    public String getName() {
        return name;
    }

    public static Category getCategoryByName(ArrayList<Category> categories, String categoryName) {
        for (Category category : categories) {
            if (category.name.equals(categoryName)) return category;
        }
        return null;
    }
}
