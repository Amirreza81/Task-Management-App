package controller;

import model.Board;
import model.Task;
import model.Team;
import model.User;

import java.util.HashMap;

public class LoggedController {
    private static HashMap<Thread,LoggedController> instance = new HashMap<>();
    private User loggedInUser;
    private Team loggedTeam;
    private Board selectedBoard;
    private Task selectedTask;
    private Team selectedTeam;
    private Team selectedTeamForTask;


    private LoggedController() {

    }

    public static LoggedController getInstance(Thread thread) {
        if (instance.get(thread)==null){
            instance.put(thread,new LoggedController());
        }
        return instance.get(thread);
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public Team getLoggedTeam() {
        return loggedTeam;
    }

    public void setLoggedTeam(Team loggedTeam) {
        this.loggedTeam = loggedTeam;
    }

    public Board getSelectedBoard() {
        return selectedBoard;
    }

    public void setSelectedBoard(Board selectedBoard) {
        this.selectedBoard = selectedBoard;
    }

    public Task getSelectedTask() {
        return selectedTask;
    }

    public void setSelectedTask(Task selectedTask) {
        this.selectedTask = selectedTask;
    }

    public Team getSelectedTeam() {
        return selectedTeam;
    }

    public void setSelectedTeam(Team selectedTeam) {
        this.selectedTeam = selectedTeam;
    }

    public Team getSelectedTeamForTask() {
        return selectedTeamForTask;
    }

    public void setSelectedTeamForTask(Team selectedTeamForTask) {
        this.selectedTeamForTask = selectedTeamForTask;
    }
}
