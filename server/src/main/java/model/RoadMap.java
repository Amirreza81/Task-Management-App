package model;

import java.util.HashMap;

public class RoadMap {

    private Team team;
    private HashMap<Task, Integer> tasksStatus;
    private HashMap<Task, Date> creationDates;

    public RoadMap(Team team) {
        this.team = team;
        this.tasksStatus = new HashMap<>();
        this.team.setRoadMap(this);
        this.creationDates = new HashMap<>();
    }

    public HashMap<Task, Date> getCreationDates() {
        return creationDates;
    }


    public HashMap<Task, Integer> getTasksStatus() {
        return tasksStatus;
    }
}
