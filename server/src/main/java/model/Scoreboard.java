package model;

import java.util.HashMap;

public class Scoreboard {

    private Team team;
    private HashMap<User, Integer> scores;

    public Scoreboard(Team team) {
        this.team = team;
        this.scores = new HashMap<>();
        this.team.setScoreboard(this);
    }

    public HashMap<User, Integer> getScores() {
        return scores;
    }
}
