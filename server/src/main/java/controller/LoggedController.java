package controller;

import model.Board;
import model.Task;
import model.Team;
import model.User;

import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.List;

public class LoggedController {
    public static HashMap<String,LoggedController> instance = new HashMap<>();
    private static HashMap<String,DataOutputStream>  dataForChat = new HashMap<>();
    private static HashMap<String, DataOutputStream> onlineCounter = new HashMap<>();
    private User loggedInUser;
    private Team loggedTeam;
    private Board selectedBoard;
    private Task selectedTask;
    private Team selectedTeam;
    private Team selectedTeamForTask;




    private LoggedController() {

    }

    public static LoggedController getInstance(String token) {
        if (instance.get(token)==null){
            instance.put(token,new LoggedController());
        }
        return instance.get(token);
    }

    public static HashMap<String, DataOutputStream> getDataForChat() {
        return dataForChat;
    }

    public static void setDataForChat(HashMap<String, DataOutputStream> dataForChat) {
        LoggedController.dataForChat = dataForChat;
    }

    public static HashMap<String, DataOutputStream> getOnlineCounter() {
        return onlineCounter;
    }

    public static void setOnlineCounter(HashMap<String, DataOutputStream> onlineCounter) {
        LoggedController.onlineCounter = onlineCounter;
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
