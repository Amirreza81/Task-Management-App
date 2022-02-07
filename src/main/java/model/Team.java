package model;

import java.util.ArrayList;
import java.util.HashMap;

public class Team {

    private static ArrayList<Team> acceptedTeams = new ArrayList<>();
    private static ArrayList<Team> pendingTeams = new ArrayList<>();
    private static ArrayList<Team> allTeams = new ArrayList<>();
    private static int teamNumberCreator = 1;
    private static Team selectTeam;
    private int teamNumber;
    private String teamName;
    private ArrayList<User> teamMembers;
    private ArrayList<User> suspendedMembers;
    private ArrayList<Board> boards;
    private User teamLeader;
    private Scoreboard scoreboard;
    private RoadMap roadMap;
    private ChatRoom chatRoom;
    private ArrayList<Task> allTasks;
    private ArrayList<Notification> notifications;
    private ArrayList<String> invitedFriends;
    private Date creationDate;
    private HashMap<User, Date> joiningDateForMembers;


    public Team(String teamName, User teamLeader, Date creationDate) {
        this.teamName = teamName;
        this.teamLeader = teamLeader;
        this.creationDate = creationDate;
        this.teamNumber = teamNumberCreator;
        teamNumberCreator++;
        pendingTeams.add(this);
        allTeams.add(this);
        roadMap = new RoadMap(this);
        this.teamMembers = new ArrayList<>();
        this.allTasks = new ArrayList<>();
        this.suspendedMembers = new ArrayList<>();
        this.notifications = new ArrayList<>();
        this.boards = new ArrayList<>();
        teamLeader.getJoiningDate().put(this, this.creationDate);
        teamLeader.getUserTeams().add(this);
        this.scoreboard = new Scoreboard(this);
        this.invitedFriends = new ArrayList<>();
    }

    public static Team getTeamByName(String teamName, ArrayList<Team> teams) {
        for (Team team : teams) {
            if (team.teamName.equals(teamName)) return team;
        }
        return null;
    }

    public ArrayList<String> getInvitedFriends() {
        return invitedFriends;
    }

    public void setInvitedFriends(ArrayList<String> invitedFriends) {
        this.invitedFriends = invitedFriends;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public static void setAcceptedTeams(ArrayList<Team> acceptedTeams) {
        Team.acceptedTeams = acceptedTeams;
    }

    public static void setPendingTeams(ArrayList<Team> pendingTeams) {
        Team.pendingTeams = pendingTeams;
    }

    public static void setAllTeams(ArrayList<Team> allTeams) {
        Team.allTeams = allTeams;
    }

    public static int getTeamNumberCreator() {
        return teamNumberCreator;
    }

    public static Team getSelectTeam() {
        return selectTeam;
    }

    public static void setSelectTeam(Team selectTeam) {
        Team.selectTeam = selectTeam;
    }

    public static void setTeamNumberCreator(int teamNumberCreator) {
        Team.teamNumberCreator = teamNumberCreator;
    }

    public void setTeamLeader(User teamLeader) {
        this.teamLeader = teamLeader;
    }

    public void setScoreboard(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    public void setRoadMap(RoadMap roadMap) {
        this.roadMap = roadMap;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public static ArrayList<Team> getAcceptedTeams() {
        return acceptedTeams;
    }

    public static ArrayList<Team> getPendingTeams() {
        return pendingTeams;
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    public String getTeamName() {
        return teamName;
    }

    public ArrayList<User> getTeamMembers() {
        return teamMembers;
    }

    public User getTeamLeader() {
        return teamLeader;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public RoadMap getRoadMap() {
        return roadMap;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public ArrayList<Task> getAllTasks() {
        return allTasks;
    }

    public ArrayList<User> getSuspendedMembers() {
        return suspendedMembers;
    }

    public static ArrayList<Team> getAllTeams() {
        return allTeams;
    }

    public ArrayList<Board> getBoards() {
        return boards;
    }
}
