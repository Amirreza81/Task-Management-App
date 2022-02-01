package controller;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerController {
    private static ServerController instance = null;
    // there should be a final object for synchronizing , I could use this(serverController object)
    // but I decided to make new object.
    private final Object Lock;
    // as we know it's so important that the object we want to lock in synchronized block be final object.
    private int n;
    private int next_n;
    private int pendingSerialsCount;
    private final ArrayList<String> winners;

    private ServerController() {
        Lock = new Object();
        //default n=10
        n = 10;
        next_n = 10;
        pendingSerialsCount = 0;
        this.winners = new ArrayList<String>();
    }

    public static ServerController getInstance() {
        if (instance == null) {
            instance = new ServerController();
        }
        return instance;
    }


    public String draw(String serial) {
        if (!checkFormat(serial)) {
            return "format error";
        }
        synchronized (Lock) {
            if (pendingSerialsCount == 0) {
                prepareNewDraw();
            }
            if (pendingSerialsCount == n - 1) {
                pendingSerialsCount = 0;
                synchronized (winners) {
                    winners.add(serial);
                }
                return "won";
            }
            pendingSerialsCount++;
            return "lost";
        }
    }

    private void prepareNewDraw() {
        n = next_n;
    }

    private boolean checkFormat(String serial) {
        Pattern pattern = Pattern.compile("^\\d{2,4}[a-z]-[0-9A-F]{6}$");
        Matcher matcher = pattern.matcher(serial);
        return matcher.matches();
    }

    public String changeN(String next_n) {
        if (!next_n.matches("\\d+")) return "format error";
        // when sb is submitting a new serial , it's not allowed to change next n
        // so "changeN" and "draw" methods should lock same object for synchronized block
        synchronized (Lock) {
            this.next_n = Integer.parseInt(next_n);
            return "success";
        }
    }

    public String getWinners() {
        StringBuilder stringBuilder = new StringBuilder();
        synchronized (winners) {
            // when we are iterating on an arraylist it shouldn't change, so we put it in synchronized block
            for (String winner : winners) {
                stringBuilder.append(winner);
                stringBuilder.append("  ");
            }
        }
        String result = stringBuilder.toString();
        if (result == null) {
            return "";
        }
        return result;
    }
}
