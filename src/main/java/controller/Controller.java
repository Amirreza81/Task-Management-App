package controller;

import model.Board;
import model.Task;
import model.Team;
import model.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {

    private static DataInputStream inputStream;
    private static DataOutputStream outputStream;
    public  static final Controller controller = new Controller();
    public static String token;

    public static void setupConnection() {
        try {
            Socket socket = new Socket("localhost", 7777);
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int register(String username, String password1, String password2, String email) {
        try {
            outputStream.writeUTF(String.format
                    ("user create --username %s --password1 %s --password2 %s --email Address %s"
                            ,username,password1,password2,email));
            outputStream.flush();
            String result = inputStream.readUTF();
            return Integer.parseInt(result);
        } catch (IOException e) {
            return -1;
        }
    }
    public User getLoggedInUser(){
        try {
            outputStream.writeUTF("get LoggedUser --token "+token);
            outputStream.flush();
            String result = inputStream.readUTF();
            JsonObjectController jsonObjectController = new JsonObjectController(User.class);
            return (User) jsonObjectController.createJsonObject(result);
        }
         catch (IOException e) {
        return null;
        }
    }
    public Team getLoggedTeam(){
        try {
            outputStream.writeUTF("get LoggedTeam --token "+token);
            outputStream.flush();
            String result = inputStream.readUTF();
            JsonObjectController jsonObjectController = new JsonObjectController(Team.class);
            return (Team) jsonObjectController.createJsonObject(result);
        }
        catch (IOException e) {
            return null;
        }
    }
    public Task getSelectedTask(){
        try {
            outputStream.writeUTF("get SelectedTask --token "+token);
            outputStream.flush();
            String result = inputStream.readUTF();
            JsonObjectController jsonObjectController = new JsonObjectController(Task.class);
            return (Task) jsonObjectController.createJsonObject(result);
        }
        catch (IOException e) {
            return null;
        }
    }
    public Task getSelectedTeamForTask(){
        try {
            outputStream.writeUTF("get SelectedTeamForTask --token "+token);
            outputStream.flush();
            String result = inputStream.readUTF();
            JsonObjectController jsonObjectController = new JsonObjectController(Task.class);
            return (Task) jsonObjectController.createJsonObject(result);
        }
        catch (IOException e) {
            return null;
        }
    }


    public Board getLoggedBoard() {
        try {
            outputStream.writeUTF("get LoggedBoard --token "+token);
            outputStream.flush();
            String result = inputStream.readUTF();
            JsonObjectController jsonObjectController = new JsonObjectController(Board.class);
            return (Board) jsonObjectController.createJsonObject(result);
        }
        catch (IOException e) {
            return null;
        }

    }


    public int logIn(String username1, String password1) {
        try {
            outputStream.writeUTF(String.format
                    ("user login --username %s --password %s"
                            ,username1,password1));
            outputStream.flush();
            String result = inputStream.readUTF();
            Matcher matcher = getCommandMatcher("^(\\d) --token (.*)$",result);
            matcher.matches();
            String response = matcher.group(1);
            token = matcher.group(2);
            return Integer.parseInt(response);
        } catch (IOException e) {
            return -1;
        }

    }
    public Matcher getCommandMatcher(String pattern, String input) {
        Pattern pattern1 = Pattern.compile(pattern);
        Matcher matcher = pattern1.matcher(input);
        matcher.matches();
        return matcher;

    }
}
