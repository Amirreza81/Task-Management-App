package controller;

import com.gilecode.yagson.com.google.gson.reflect.TypeToken;
import model.Board;
import model.Task;
import model.Team;
import model.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {

    private static DataInputStream inputStream;
    private static DataOutputStream outputStream;
    public static final Controller controller = new Controller();
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
                            , username, password1, password2, email));
            outputStream.flush();
            String result = inputStream.readUTF();
            return Integer.parseInt(result);
        } catch (IOException e) {
            return -1;
        }
    }

    public User getLoggedInUser() {
        try {
            outputStream.writeUTF("get LoggedUser --token " + token);
            outputStream.flush();
            String result = inputStream.readUTF();
            JsonObjectController jsonObjectController = new JsonObjectController(User.class);
            return (User) jsonObjectController.createJsonObject(result);
        } catch (IOException e) {
            return null;
        }
    }

    public Team getLoggedTeam() {
        try {
            outputStream.writeUTF("get LoggedTeam --token " + token);
            outputStream.flush();
            String result = inputStream.readUTF();
            JsonObjectController jsonObjectController = new JsonObjectController(Team.class);
            return (Team) jsonObjectController.createJsonObject(result);
        } catch (IOException e) {
            return null;
        }
    }

    public Task getSelectedTask() {
        try {
            outputStream.writeUTF("get SelectedTask --token " + token);
            outputStream.flush();
            String result = inputStream.readUTF();
            JsonObjectController jsonObjectController = new JsonObjectController(Task.class);
            return (Task) jsonObjectController.createJsonObject(result);
        } catch (IOException e) {
            return null;
        }
    }

    public Team getSelectedTeamForTask() {
        try {
            outputStream.writeUTF("get SelectedTeamForTask --token " + token);
            outputStream.flush();
            String result = inputStream.readUTF();
            JsonObjectController jsonObjectController = new JsonObjectController(Team.class);
            return (Team) jsonObjectController.createJsonObject(result);
        } catch (IOException e) {
            return null;
        }
    }


    public Board getLoggedBoard() {
        try {
            outputStream.writeUTF("get LoggedBoard --token " + token);
            outputStream.flush();
            String result = inputStream.readUTF();
            JsonObjectController jsonObjectController = new JsonObjectController(Board.class);
            return (Board) jsonObjectController.createJsonObject(result);
        } catch (IOException e) {
            return null;
        }

    }


    public int logIn(String username1, String password1) {
        try {
            outputStream.writeUTF(String.format
                    ("user login --username %s --password %s"
                            , username1, password1));
            outputStream.flush();
            String result = inputStream.readUTF();
            Matcher matcher = getCommandMatcher("^(\\d) --token (.*)$", result);
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
                            , newUsername, token));
            outputStream.flush();
            String result = inputStream.readUTF();
            Matcher matcher = getCommandMatcher("(\\d)", result);
            matcher.matches();
            String response = matcher.group(1);
            return Integer.parseInt(response);
        } catch (IOException e) {
            return -1;
        }
    }

    public int changePassword(String oldPassword, String newPassword) {
        try {
            outputStream.writeUTF(String.format
                    ("Profile --change --oldPassword %s --newPassword %s --token %s"
                            , oldPassword, newPassword, token));
            outputStream.flush();
            String result = inputStream.readUTF();
            Matcher matcher = getCommandMatcher("(\\d)", result);
            matcher.matches();
            String response = matcher.group(1);
            return Integer.parseInt(response);
        } catch (IOException e) {
            System.out.println("Error in ....");
            return -1;
        }
    }

    public void updateFailed() throws IOException {
        outputStream.writeUTF("board updateFailed --token " + token);
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
        outputStream.writeUTF("board BoardFailedPercentage --token " + token);
        outputStream.flush();
        return inputStream.readUTF();
    }


    public int banUser(String userName) {
        try {
            outputStream.writeUTF(String.format
                    ("admin ban user --user %s --token %s"
                            , userName, token));
            outputStream.flush();
            String result = inputStream.readUTF();
            Matcher matcher = getCommandMatcher("(\\d)", result);
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
                            , userName, newRole1, token));
            outputStream.flush();
            String result = inputStream.readUTF();
            Matcher matcher = getCommandMatcher("(\\d)", result);
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
                            , token));
            outputStream.flush();
            String result = inputStream.readUTF();
            Matcher matcher = getCommandMatcher("(\\d)", result);
            matcher.matches();
            String response = matcher.group(1);
            return Integer.parseInt(response);
        } catch (IOException e) {
            return -1;
        }
    }

    public int acceptTeam(String teamName) {
        try {
            outputStream.writeUTF(String.format
                    ("admin accept --teams %s --token %s"
                            , teamName, token));
            outputStream.flush();
            String result = inputStream.readUTF();
            Matcher matcher = getCommandMatcher("(\\d)", result);
            matcher.matches();
            String response = matcher.group(1);
            return Integer.parseInt(response);
        } catch (IOException e) {
            return -1;
        }
    }

    public int rejectTeam(String teamName) {
        try {
            outputStream.writeUTF(String.format
                    ("admin accept --teams %s --token %s"
                            , teamName, token));
            outputStream.flush();
            String result = inputStream.readUTF();
            Matcher matcher = getCommandMatcher("(\\d)", result);
            matcher.matches();
            String response = matcher.group(1);
            return Integer.parseInt(response);
        } catch (IOException e) {
            return -1;
        }
    }

    public void sendNotificationToAll(String notification) {
        try {
            outputStream.writeUTF(String.format
                    ("admin send --notification %s --all --token %s"
                            , notification, token));
            outputStream.flush();
            String result = inputStream.readUTF();
            Matcher matcher = getCommandMatcher("(\\d)", result);
            matcher.matches();
//            String response = matcher.group(1);
//            return Integer.parseInt(response);
        } catch (IOException ignored) {
        }
    }

    public int sendNotificationForTeam(String teamName, String notifications) {
        try {
            outputStream.writeUTF(String.format
                    ("admin send --notification %s --team %s --token %s"
                            , notifications, teamName, token));
            outputStream.flush();
            String result = inputStream.readUTF();
            Matcher matcher = getCommandMatcher("(\\d)", result);
            matcher.matches();
            String response = matcher.group(1);
            return Integer.parseInt(response);
        } catch (IOException e) {
            return -1;
        }
    }

    public int sendNotificationForUser(String userName, String notifications) {
        try {
            outputStream.writeUTF(String.format
                    ("admin send --notification %s --user %s --token %s"
                            , notifications, userName, token));
            outputStream.flush();
            String result = inputStream.readUTF();
            Matcher matcher = getCommandMatcher("(\\d)", result);
            matcher.matches();
            String response = matcher.group(1);
            return Integer.parseInt(response);
        } catch (IOException e) {
            return -1;
        }
    }

    public int removeAssignedUsers(String assignedUsers) throws IOException {
        outputStream.writeUTF(String.format
                ("task removeAssignedUsers --assignedUsers %s --token %s", assignedUsers, token));
        outputStream.flush();
        String result = inputStream.readUTF();
        return Integer.parseInt(result);
    }

    public void setSelectedTask(String taskTitle) throws IOException {
        outputStream.writeUTF(String.format
                ("set setSelectedTask --taskTitle %s --token %s", taskTitle, token));
        outputStream.flush();
        inputStream.readUTF();
    }

    public int creatTeam(String teamName) throws IOException {
        outputStream.writeUTF(String.format
                ("team creatTeam --teamName %s --token %s", teamName, token));
        outputStream.flush();
        String result = inputStream.readUTF();
        return Integer.parseInt(result);
    }

    public int addMember(String memberName) throws IOException {
        outputStream.writeUTF(String.format
                ("team addMember --memberName %s --token %s", memberName, token));
        outputStream.flush();
        String result = inputStream.readUTF();
        return Integer.parseInt(result);
    }

    public void setSelectedTeam(String teamName) throws IOException {
        outputStream.writeUTF(String.format
                ("set setSelectedTeam --teamName %s --token %s", teamName, token));
        outputStream.flush();
        inputStream.readUTF();
    }

    public int deleteMember(String memberName) throws IOException {
        outputStream.writeUTF(String.format
                ("team deleteMember --memberName %s --token %s", memberName, token));
        outputStream.flush();
        String result = inputStream.readUTF();
        return Integer.parseInt(result);
    }

    public int suspendMember(String memberName) throws IOException {
        outputStream.writeUTF(String.format
                ("team suspendMember --memberName %s --token %s", memberName, token));
        outputStream.flush();
        String result = inputStream.readUTF();
        return Integer.parseInt(result);
    }

    public int creatTask(String taskTitle, String startTime, String deadline, String description, String priority) throws IOException {
        outputStream.writeUTF(String.format
                ("task creatTask --taskTitle %s --startTime %s --deadline %s --description %s --priority %s --token %s", taskTitle, startTime, deadline, description, priority, token));
        outputStream.flush();
        String result = inputStream.readUTF();
        return Integer.parseInt(result);
    }

    public int assignMember(String taskId, String username) throws IOException {
        outputStream.writeUTF(String.format
                ("task assignMember --taskId %s --username %s --token %s", taskId, username, token));
        outputStream.flush();
        String result = inputStream.readUTF();
        return Integer.parseInt(result);
    }

    public Team getSelectedTeam() {
        try {
            outputStream.writeUTF("get SelectedTeam --token " + token);
            outputStream.flush();
            String result = inputStream.readUTF();
            JsonObjectController jsonObjectController = new JsonObjectController(Team.class);
            return (Team) jsonObjectController.createJsonObject(result);
        } catch (IOException e) {
            return null;
        }
    }

    public ArrayList<User> getAllUsers(){
        try {
            outputStream.writeUTF("get allUsers --token " + token);
            outputStream.flush();
            String result = inputStream.readUTF();
            JsonObjectController jsonObjectController = new JsonObjectController(new TypeToken<List<User>>() {
            }.getType());
            return (ArrayList<User>) jsonObjectController.createJsonObject2(result);
        } catch (IOException e) {
            return null;
        }
    }
    public ArrayList<User> getAllTeams(){
        try {
            outputStream.writeUTF("get allTeams --token " + token);
            outputStream.flush();
            String result = inputStream.readUTF();
            JsonObjectController jsonObjectController = new JsonObjectController(new TypeToken<List<Team>>() {
            }.getType());
            return (ArrayList<User>) jsonObjectController.createJsonObject2(result);
        } catch (IOException e) {
            return null;
        }
    }


    public void setSelectedBoard(String boardNameText) throws IOException {
        outputStream.writeUTF(String.format
                ("set setSelectedBoard --boardName %s --token %s", boardNameText, token));
        outputStream.flush();
        inputStream.readUTF();
    }

    public int makeBoard(String boardNameText) throws IOException {
        outputStream.writeUTF(String.format
                ("board --new --name %s --token %s", boardNameText, token));
        outputStream.flush();
        String result = inputStream.readUTF();
        return Integer.parseInt(result);
    }

    public int updateDeadline(String title, String text) throws IOException {
        outputStream.writeUTF(String.format
                ("^board --open --task %s --deadline %s --token %s", title,text,token));
        outputStream.flush();
        String result = inputStream.readUTF();
        return Integer.parseInt(result);
    }

    public String getBoardCompletionPercentage() throws IOException {
        outputStream.writeUTF("board BoardCompletionPercentage --token " + token);
        outputStream.flush();
        return inputStream.readUTF();
    }

    public void removeBoard() throws IOException {
        outputStream.writeUTF("board removeBoard --token " + token);
        outputStream.flush();
        inputStream.readUTF();
    }

    public int addCategory(String categoryNameText) throws IOException {
        outputStream.writeUTF(String.format
                ("board --new  --category %S --token %s", categoryNameText,token));
        outputStream.flush();
        String result = inputStream.readUTF();
        return Integer.parseInt(result);
    }

    public void boardDone() throws IOException {
        outputStream.writeUTF("board --done --token " + token);
        outputStream.flush();
        inputStream.readUTF();
    }

    public int boardAddTask(String id) throws IOException {
        outputStream.writeUTF(String.format
                ("board --add %s --token %s",id ,token));
        outputStream.flush();
        String result = inputStream.readUTF();
        return Integer.parseInt(result);
    }

    public void forceCategory(String categoryName, String title) throws IOException {
        outputStream.writeUTF(String.format
                ("board --force --category %s --task %s --token %s",categoryName,title ,token));
        outputStream.flush();
        inputStream.readUTF();
    }

    public void changeColumn(String categoryName, int column) throws IOException {
        outputStream.writeUTF(String.format
                ("board --category %s --column %s --token %s",categoryName, column,token));
        outputStream.flush();
        inputStream.readUTF();
    }

    public int goToNextCategory(String title) throws IOException {
        outputStream.writeUTF(String.format
                ("board --category next --task %s --token %s",title ,token));
        outputStream.flush();
        String result = inputStream.readUTF();
        System.out.println(result);
        return Integer.parseInt(result);
    }

    public void setLoggedTeam(String teamNameText) throws IOException {
        outputStream.writeUTF(String.format
                ("set setLoggedTeam --teamName %s --token %s", teamNameText, token));
        outputStream.flush();
        inputStream.readUTF();
    }

    public void setData() throws IOException {
        outputStream.writeUTF("set Data");
        outputStream.flush();
        inputStream.readUTF();
    }
}
