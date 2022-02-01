package model;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatRoom {

    private Team team;
    private HashMap<User, ArrayList<Message>> chatRoom;
    private ArrayList<Message> allMassages;

    public ChatRoom(Team team) {
        this.team = team;
        this.chatRoom = new HashMap<>();
        this.team.setChatRoom(this);
        this.allMassages = new ArrayList<>();
    }

    public ArrayList<Message> getAllMassages() {
        return allMassages;
    }

    public HashMap<User, ArrayList<Message>> getChatRoom() {
        return chatRoom;
    }
}
