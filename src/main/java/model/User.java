package model;

import view.LoginView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class User {

    private static ArrayList<User> users = new ArrayList<>();
    private static int idCreator = 1;
    private int creationId;
    private String fullName;
    private Date birthday;
    private String userName;
    private String password;
    private String email;
    private boolean isHidden = true;
    private String role = "Member";
    private int score = 0;
    private ArrayList<Log> allLogs;
    private ArrayList<Task> allTasksForUser;
    private ArrayList<Notification> notifications;
    private ArrayList<Team> userTeams;
    private HashMap<Team, model.Date> joiningDate;

    public User(String userName, String password,
                String email) {
        if (users.size() == 0) {
            this.role = "System Admin";
        }
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.creationId = idCreator;
        idCreator++;
        users.add(this);
        this.allLogs = new ArrayList<>();
        this.allTasksForUser = new ArrayList<>();
        this.notifications = new ArrayList<>();
        this.userTeams = new ArrayList<>();
        this.joiningDate = new HashMap<>();
    }

    public static User getUserByUsername(String userName) {
        for (User user : users) {
            if (user.userName.equals(userName))
                return user;
        }
        return null;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    public static void setUsers(ArrayList<User> users) {
        User.users = users;
    }

    public int getScore() {
        return score;
    }

    public static int getIdCreator() {
        return idCreator;
    }

    public static void setIdCreator(int idCreator) {
        User.idCreator = idCreator;
    }

    public static ArrayList<User> getUsers() {
        return users;
    }

    public int getCreationId() {
        return creationId;
    }

    public ArrayList<Task> getAllTasksForUser() {
        return allTasksForUser;
    }

    public ArrayList<Team> getUserTeams() {
        return userTeams;
    }

    public HashMap<Team, model.Date> getJoiningDate() {
        return joiningDate;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ArrayList<Log> getAllLogs() {
        return allLogs;
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public String getFullName() {
        return fullName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
//    public void setForFirstTeams(){
//        for(Team team : Team.getAllTeams()){
//            if (team.getTeamMembers().contains(this)){
//                this.userTeams.add(team);
//            }
//        }
//    }

}
