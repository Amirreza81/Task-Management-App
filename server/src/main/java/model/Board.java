package model;

import java.util.ArrayList;

public class Board {

    private static ArrayList<Board> allBoards = new ArrayList<>();
    private  ArrayList<Task> failed;
    private  ArrayList<Task> done;
    private static int idCreator = 1;
    private int creationId;
    private boolean isCreated;
    private Team team;
    private String boardName;
    private ArrayList<Category> allCategories;
    private ArrayList<User> membersOfBoards;
    private ArrayList<Task> boardTasks;
    private User creator;
    private String column;

    public Board(String boardName, User creator, Team team) {
        this.creationId = idCreator;
        idCreator++;
        this.boardName = boardName;
        this.creator = creator;
        this.team = team;
        this.failed = new ArrayList<>();
        this.done = new ArrayList<>();
        this.membersOfBoards = new ArrayList<>();
        this.allCategories = new ArrayList<>();
        this.boardTasks = new ArrayList<>();
        this.team.getBoards().add(this);
    }


    public void setAllCategories(ArrayList<Category> allCategories) {
        this.allCategories = allCategories;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public void setCreated(boolean created) {
        isCreated = created;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public static ArrayList<Board> getAllBoards() {
        return allBoards;
    }

    public boolean isCreated() {
        return isCreated;
    }

    public ArrayList<Category> getAllCategories() {
        return allCategories;
    }

    public ArrayList<Task> getBoardTask() {
        return boardTasks;
    }

    public Team getTeam() {
        return team;
    }

    public String getColumn() {
        return column;
    }

    public String getBoardName() {
        return boardName;
    }

    public  ArrayList<Task> getFailed() {
        return failed;
    }

    public  ArrayList<Task> getDone() {
        return done;
    }

    public ArrayList<User> getMembersOfBoards() {
        return membersOfBoards;
    }

    public User getCreator() {
        return creator;
    }

    public static int getIdCreator() {
        return idCreator;
    }

    public static void setAllBoards(ArrayList<Board> allBoards) {
        Board.allBoards = allBoards;
    }

    public static void setIdCreator(int idCreator) {
        Board.idCreator = idCreator;
    }

    public static Board getBoardByName(ArrayList<Board> boards, String boardName) {
        for (Board board : boards) {
            if (board.boardName.equals(boardName)) return board;
        }
        return null;
    }
}
