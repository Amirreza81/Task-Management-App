package model;

public class Notification {

    private String text;
    private User sender;
    private int type;  // 0 -> User , 1 -> Team

    public Notification(String text, User sender,
                        int type) {
        this.text = text;
        this.sender = sender;
        this.type = type;
    }

    public User getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }
}
