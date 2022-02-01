package model;

public class Log {

    private User user;
    private String date;

    public Log(User user, String date) {
        this.user = user;
        this.date = date;
        this.user.getAllLogs().add(this);
    }

    public User getUser() {
        return user;
    }

    public String getDate() {
        return date;
    }
}
