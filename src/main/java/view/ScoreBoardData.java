package view;

import controller.Controller;
import model.User;

import java.util.ArrayList;
import java.util.Comparator;

public class ScoreBoardData {

    int rank;
    String username;
    int score;
    static final ArrayList<ScoreBoardData> scoreBoardData;

    static {
        scoreBoardData = new ArrayList<>();
    }


    public ScoreBoardData(String username, int score, int rank) {
        this.rank = rank;
        this.username = username;
        this.score = score;
    }

    public static void getAndSetDataFromUser() {
        scoreBoardData.clear();
        ArrayList<User> users = getSortedUsers();
        int counter = 1;
        for (User user : users) {
            scoreBoardData.add(new ScoreBoardData(user.getUserName(), user.getScore(), counter));
            counter++;
        }
    }

    public static ArrayList<ScoreBoardData> getScoreBoardData() {
        return scoreBoardData;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }
    
    public static ArrayList<User> getSortedUsers() {
        ArrayList<User> users = new ArrayList<>(Controller.controller.getLoggedTeam().getTeamMembers());
//        System.out.println(users.size());
        Comparator<User> comparator = Comparator.comparingInt(User::getScore).reversed();
        users.sort(comparator);
        users.removeIf(user -> user.getUserName().equals(""));
        return users;
    }

}
