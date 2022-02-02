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

    public int changeUserName(String newUsername) {
        try {
            outputStream.writeUTF(String.format
                    ("Profile --change --username %s --token %s"
                            ,newUsername, token));
            outputStream.flush();
            String result = inputStream.readUTF();
            Matcher matcher = getCommandMatcher("(\\d)",result);
            matcher.matches();
            String response = matcher.group(1);
            return Integer.parseInt(response);
        } catch (IOException e) {
            return -1;
        }
    }

    public int changePassword(User loggedInUser, String oldPassword, String newPassword) {
        try {
            outputStream.writeUTF(String.format
                    ("Profile --change --oldPassword %s --newPassword %s --token %s"
                            ,oldPassword, newPassword, token));
            outputStream.flush();
            String result = inputStream.readUTF();
            Matcher matcher = getCommandMatcher("(\\d)",result);
            matcher.matches();
            String response = matcher.group(1);
            return Integer.parseInt(response);
        } catch (IOException e) {
            return -1;
        }
    }

    public void updateFailed() throws IOException {
        outputStream.writeUTF("board updateFailed --token "+token);
        outputStream.flush();
        inputStream.readUTF();
    }

    public void setSelectedTeamForTask(String teamName) throws IOException {
        outputStream.writeUTF(String.format
                ("set setSelectedTeamForTask --teamName %s --token %s", teamName, token));
        outputStream.flush();
        inputStream.readUTF();
    }

    public void editTaskTitle(String title) throws IOException {
        outputStream.writeUTF(String.format
                ("task editTaskTitle --taskTitle %s --token %s", title, token));
        outputStream.flush();
        inputStream.readUTF();
    }

    public void editTaskPriority(String priority) throws IOException {
        outputStream.writeUTF(String.format
                ("task editTaskPriority --taskPriority %s --token %s", priority, token));
        outputStream.flush();
        inputStream.readUTF();
    }

    public void editTaskDescription(String description) throws IOException {
        outputStream.writeUTF(String.format
                ("task editTaskDescription --taskDescription %s --token %s", description, token));
        outputStream.flush();
        inputStream.readUTF();
    }

    public int editTaskDeadline(String deadline) throws IOException {
        outputStream.writeUTF(String.format
                ("task editTaskDeadline --taskDeadline %s --token %s", deadline, token));
        outputStream.flush();
        String result = inputStream.readUTF();
        return Integer.parseInt(result);
    }
    public String getBoardFailedPercentage() throws IOException {
        outputStream.writeUTF("board BoardFailedPercentage --token "+token);
        outputStream.flush();
        return inputStream.readUTF();
    }


    public int banUser(String userName) {
        try {
            outputStream.writeUTF(String.format
                    ("admin ban user --user %s --token %s"
                            ,userName,token));
            outputStream.flush();
            String result = inputStream.readUTF();
            Matcher matcher = getCommandMatcher("(\\d)",result);
            matcher.matches();
            String response = matcher.group(1);
            return Integer.parseInt(response);
        } catch (IOException e) {
            return -1;
        }
    }

    public int changeRole(String userName, String newRole1) {
        try {
            outputStream.writeUTF(String.format
                    ("admin change role --user %s --role %s --token %s"
                            ,userName, newRole1, token));
            outputStream.flush();
            String result = inputStream.readUTF();
            Matcher matcher = getCommandMatcher("(\\d)",result);
            matcher.matches();
            String response = matcher.group(1);
            return Integer.parseInt(response);
        } catch (IOException e) {
            return -1;
        }
    }

    public int showPendingTeams() {
        try {
            outputStream.writeUTF(String.format
                    ("admin show --pendingTeams --token %s"
                            ,userName, newRole1, token));
            outputStream.flush();
            String result = inputStream.readUTF();
            Matcher matcher = getCommandMatcher("(\\d)",result);
            matcher.matches();
            String response = matcher.group(1);
            return Integer.parseInt(response);
        } catch (IOException e) {
            return -1;
        }
    }
}
