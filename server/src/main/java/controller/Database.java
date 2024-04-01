package controller;

import model.Board;
import model.Task;
import model.Team;
import model.User;

import java.util.ArrayList;

public class Database {
    public ArrayList<User> users = User.getUsers();
    public ArrayList<Team> teams = Team.getAllTeams();
    public ArrayList<Team> pending = Team.getPendingTeams();
    public ArrayList<Team> accepted = Team.getAcceptedTeams();
    public ArrayList<Task> tasks = Task.getAllTasks();
    public ArrayList<Board> boards = Board.getAllBoards();
    public Integer uI = User.getIdCreator();
    public Integer tI = Team.getTeamNumberCreator();
    public Integer taI = Task.getIdCreator();
    public Integer bI = Board.getIdCreator();

}
