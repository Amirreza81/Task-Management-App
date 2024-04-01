import controller.*;
import model.Board;
import model.Task;
import model.Team;
import model.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Matcher;

public class Server {
    private static Server instance = null;

    private Server() {
    }

    public static Server getInstance() {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }

    public void run() {
        JsonController.getInstance().readFromJson();
        try {
            ServerSocket serverSocket = new ServerSocket(7777);
            while (true) {
                Socket socket = serverSocket.accept();
                makeThreadAndProcess(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeThreadAndProcess(Socket socket) {
        new Thread(() -> {
            try {
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                int response = processCommand(dataInputStream, dataOutputStream , socket);
                if (response==-1){
                    dataInputStream.close();
                    socket.close();
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }).start();
    }
    private static void startThreadForChatSocket(Socket socket, DataOutputStream dataOutputStream, DataInputStream dataInputStream, String token) {
        new Thread(() -> {
            try {
                while (true) {
                    System.out.println("while");
                    String message;
                    message = dataInputStream.readUTF();
                    System.out.println("im here");
                    if (message.equals("close_chat_socket")) {
                        System.out.println("chat closed");
                        synchronized (LoggedController.getDataForChat()) {
                            LoggedController.getDataForChat().get(token).writeUTF("close");
                            dataOutputStream.flush();
                            LoggedController.getDataForChat().remove(token);
                        }
                        synchronized (LoggedController.getOnlineCounter()) {
                            LoggedController.getOnlineCounter().get(token).writeUTF("close");
                            LoggedController.getOnlineCounter().get(token).flush();
                            LoggedController.getOnlineCounter().remove(token);
                        }
                        break;
                    } else if (message.matches("user_data .+")) {
                        message = message.replaceFirst("user_data ", "");
                        User user = User.getUserByUsername(message);
                        dataOutputStream.writeUTF(user.getUserName() + " " + user.getFullName() + " " + user.getScore());
                        dataOutputStream.flush();
                    } else {
                        System.out.println(message);
                        String result = ChatMenuController.getInstance().sendMessage(token, message);
                        dataOutputStream.writeUTF(result);
                        dataOutputStream.flush();
                    }
                }
                dataOutputStream.close();
                dataInputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    private int processCommand(DataInputStream dataInputStream, DataOutputStream dataOutputStream,Socket socket)
            throws IOException, ParseException {
        while (true) {
            String input;
            try {
                input = dataInputStream.readUTF();
            } catch (SocketException e) {
                break;
            }
            if (input.startsWith("chat")){
                recognizeChatCommand(input,dataOutputStream,dataInputStream,socket);
                return 0;
            }
            else {
                String result = recognizeCommand(input);
                // there should be a way to say that the client is sending non-meaningful commands and close the socket
                if (result.equals("")) break;
                dataOutputStream.writeUTF(result);
                dataOutputStream.flush();
            }

        }
        return -1;
    }

    private void recognizeChatCommand(String in,DataOutputStream dataOutputStream,
                                      DataInputStream dataInputStream,Socket socket) throws IOException {
        System.out.println(in);
        if (in.matches("chat_Socket_Read .+")) {
            in = in.replaceFirst("chat_Socket_Read ", "");
            LoggedController.getDataForChat().put(in, dataOutputStream);
            System.out.println("puted");
        } else if (in.matches("chat_send_socket .+")) {
            System.out.println("R method");
            String token = in.replaceFirst("chat_send_socket ", "");
            startThreadForChatSocket(socket, dataOutputStream, dataInputStream, token);
            dataOutputStream.writeUTF("success " + LoggedController.instance.keySet().size());
        }
        else if (in.matches("chat_online_member_counter .+")) {
            in = in.replaceFirst("chat_online_member_counter ", "");
            LoggedController.getOnlineCounter().put(in, dataOutputStream);
        }
    }

    private String recognizeCommand(String input) throws ParseException {
        if (input.startsWith("user")) {
            return recognizeRegisterLoginCommand(input);
        } else if (input.startsWith("get")) {
            return recognizeGetObjectsCommand(input);
        } else if (input.startsWith("Profile")) {
            return recognizeProfileMenuCommand(input);
        } else if (input.startsWith("board")) {
            return recognizeBoardMenuCommand(input);
        } else if (input.startsWith("admin")) {
            return recognizeAdminMenuCommand(input);
        } else if (input.startsWith("set")) {
            return recognizeSetObjectCommand(input);
        } else if (input.startsWith("task")) {
            return recognizeTaskCommand(input);
        } else if (input.startsWith("team")) {
            return recognizeTeamCommand(input);
        } else if (input.startsWith("invite")){
            return recognizeInviteCommand(input);
        }

        // this means the command is not meaningful
        return "";
    }

    private String recognizeInviteCommand(String input) {
        Matcher matcher;
        if ((matcher = Controller.controller.getCommandMatcher
                ("invite --email (.*) --team (.*)", input)).matches()) {
            int result = Controller.controller.invite(matcher.group(1), matcher.group(2));
            return "" + result;
        }
        return "-1";
    }

    private String recognizeTeamCommand(String input) {
        Matcher matcher;
        if ((matcher = Controller.controller.getCommandMatcher
                ("team creatTeam --teamName ([^ ]+) --token (.*)", input)).matches()) {
            int result = Controller.controller.creatTeam(LoggedController.getInstance(matcher.group(2)).getLoggedInUser(),
                    matcher.group(1));
            return "" + result;
        } else if ((matcher = Controller.controller.getCommandMatcher
                ("team addMember --memberName ([^ ]+) --token (.*)", input)).matches()) {
            int result = Controller.controller.addMember(LoggedController.getInstance(matcher.group(2)).getLoggedInUser(),
                    LoggedController.getInstance(matcher.group(2)).getSelectedTeam(),
                    matcher.group(1));
            return "" + result;
        } else if ((matcher = Controller.controller.getCommandMatcher
                ("team deleteMember --memberName ([^ ]+) --token (.*)", input)).matches()) {
            int result = Controller.controller.deleteMember
                    (LoggedController.getInstance(matcher.group(2)).getLoggedInUser(),
                            LoggedController.getInstance(matcher.group(2)).getSelectedTeam(),
                            matcher.group(1));
            return "" + result;
        } else if ((matcher = Controller.controller.getCommandMatcher
                ("team suspendMember --memberName ([^ ]+) --token (.*)", input)).matches()) {
            int result = Controller.controller.suspendMember
                    (LoggedController.getInstance(matcher.group(2)).getLoggedInUser(),
                            LoggedController.getInstance(matcher.group(2)).getSelectedTeam(),
                            matcher.group(1));
            return "" + result;
        }
        return "-1";
    }

    private String recognizeSetObjectCommand(String input) {
        Matcher matcher;
        if ((matcher = Controller.controller.getCommandMatcher
                ("set setSelectedTeamForTask --teamName ([^ ]+) --token (.*)", input)).matches()) {
            LoggedController.getInstance(matcher.group(2)).setSelectedTeamForTask
                    (Controller.controller.findTeam(matcher.group(1)));
            return "successful";
        } else if ((matcher = Controller.controller.getCommandMatcher
                ("set setSelectedTask --taskTitle ([^ ]+) --token (.*)", input)).matches()) {
            LoggedController.getInstance(matcher.group(2)).setSelectedTask
                    (Task.getTaskByTitle
                            (LoggedController.getInstance(matcher.group(2)).getSelectedTeamForTask().getAllTasks(),
                                    matcher.group(1)));
            return "successful";
        } else if ((matcher = Controller.controller.getCommandMatcher
                ("set setSelectedTeam --teamName ([^ ]+) --token (.*)", input)).matches()) {
            LoggedController.getInstance(matcher.group(2)).setSelectedTeam
                    (Controller.controller.findTeam(matcher.group(1)));
            return "successful";
        } else if ((matcher = Controller.controller.getCommandMatcher
                ("set setSelectedBoard --boardName ([^ ]+) --token (.*)", input)).matches()) {
            LoggedController.getInstance(matcher.group(2)).setSelectedBoard(Board.getBoardByName(
                    LoggedController.getInstance(matcher.group(2)).getLoggedTeam().getBoards(), matcher.group(1)));
            return "successful";
        } else if ((matcher = Controller.controller.getCommandMatcher
                ("set setLoggedTeam --teamName ([^ ]+) --token (.*)", input)).matches()) {
            LoggedController.getInstance(matcher.group(2)).setLoggedTeam(Controller.controller.findTeam(matcher.group(1)));
            return "successfully";
        } else if (input.equals("set Data")) {
            JsonController.getInstance().updateJson();
            return "successful";
        }
        return "-1";
    }

    private String recognizeTaskCommand(String input) throws ParseException {
        Matcher matcher;
        if ((matcher = Controller.controller.getCommandMatcher("task editTaskTitle --taskTitle ([^ ]+) --token (.*)", input)).matches()) {
            Controller.controller.editTaskTitle(LoggedController.getInstance(matcher.group(2)).getLoggedInUser()
                    , LoggedController.getInstance(matcher.group(2)).getSelectedTask(),
                    matcher.group(1));
            return "successful";
        } else if ((matcher = Controller.controller.getCommandMatcher("task editTaskPriority --taskPriority ([^ ]+) --token (.*)", input)).matches()) {
            Controller.controller.editTaskPriority(LoggedController.getInstance(matcher.group(2)).getLoggedInUser()
                    , LoggedController.getInstance(matcher.group(2)).getSelectedTask(),
                    matcher.group(1));
            return "successful";
        } else if ((matcher = Controller.controller.getCommandMatcher("task editTaskDescription --taskDescription ([^ ]+) --token (.*)", input)).matches()) {
            Controller.controller.editTaskDescription(LoggedController.getInstance(matcher.group(2)).getLoggedInUser()
                    , LoggedController.getInstance(matcher.group(2)).getSelectedTask(),
                    matcher.group(1));
            return "successful";
        } else if ((matcher = Controller.controller.getCommandMatcher("task editTaskDeadline --taskDeadline ([^ ]+) --token (.*)", input)).matches()) {
            int result = Controller.controller.editTaskDeadline(LoggedController.getInstance(matcher.group(2)).getLoggedInUser()
                    , LoggedController.getInstance(matcher.group(2)).getSelectedTask(),
                    matcher.group(1));
            return "" + result;
        } else if ((matcher = Controller.controller.getCommandMatcher("task removeAssignedUsers --assignedUsers ([^ ]+) --token (.*)", input)).matches()) {
            int result = Controller.controller.removeAssignedUsers(LoggedController.getInstance(matcher.group(2)).getLoggedInUser()
                    , LoggedController.getInstance(matcher.group(2)).getSelectedTask(),
                    User.getUserByUsername(matcher.group(1)));
            return "" + result;
        } else if ((matcher = Controller.controller.getCommandMatcher("task assignMember --taskId ([^ ]+) --username ([^ ]+) --token (.*)", input)).matches()) {
            int result = Controller.controller.assignMember(LoggedController.getInstance(matcher.group(3)).getLoggedInUser()
                    , LoggedController.getInstance(matcher.group(3)).getSelectedTeamForTask()
                    , matcher.group(1)
                    , matcher.group(2));
            return "" + result;
        } else if ((matcher = Controller.controller.getCommandMatcher("task sendNotificationForTask --taskId ([^ ]+) --memberName ([^ ]+) --token (.*)", input)).matches()) {
            int result = Controller.controller.sendNotificationForTask(LoggedController.getInstance(matcher.group(3)).getLoggedInUser()
                    , LoggedController.getInstance(matcher.group(3)).getSelectedTeamForTask()
                    , Task.getTaskById(LoggedController.getInstance(matcher.group(3)).getSelectedTeamForTask(), matcher.group(1))
                    , matcher.group(2));
            return "" + result;
        } else if ((matcher = Controller.controller.getCommandMatcher("task creatTask --taskTitle ([^ ]+) --startTime ([^ ]+) --deadline ([^ ]+) --description ([^ ]+) --priority ([^ ]+) --token (.*)", input)).matches()) {
            int result = Controller.controller.creatTask(LoggedController.getInstance(matcher.group(6)).getLoggedInUser()
                    , LoggedController.getInstance(matcher.group(6)).getSelectedTeamForTask()
                    , matcher.group(1)
                    , matcher.group(2)
                    , matcher.group(3)
                    , matcher.group(4)
                    , matcher.group(5));
            return "" + result;
        }
        return "-1";
    }

    private String recognizeBoardMenuCommand(String input) throws ParseException {
        Matcher matcher;
        if ((matcher = Controller.controller.getCommandMatcher
                ("board updateFailed --token (.*)", input)).matches()) {
            Controller.controller.updateFailed(LoggedController.getInstance(matcher.group(1)).getSelectedBoard());
            return "successful";
        } else if ((matcher = Controller.controller.getCommandMatcher
                ("board BoardFailedPercentage --token (.*)", input)).matches()) {
            return "" + Controller.controller.getBoardFailedPercentage
                    (LoggedController.getInstance(matcher.group(1)).getSelectedBoard());
        } else if ((matcher = Controller.controller.getCommandMatcher
                ("board BoardCompletionPercentage --token (.*)", input)).matches()) {
            return "" + Controller.controller.getBoardCompletionPercentage
                    (LoggedController.getInstance(matcher.group(1)).getSelectedBoard());
        } else if ((matcher = Controller.controller.getCommandMatcher
                ("board removeBoard --token (.*)", input)).matches()) {
            LoggedController loggedController = LoggedController.getInstance(matcher.group(1));
            return "" + Controller.controller.removeBoard(loggedController.getLoggedInUser(),
                    loggedController.getSelectedBoard().getTeam(),
                    loggedController.getSelectedBoard().getBoardName());
        } else if ((matcher = Controller.controller.getCommandMatcher
                ("board --done --token (.*)", input)).matches()) {
            LoggedController loggedController = LoggedController.getInstance(matcher.group(1));
            return "" + Controller.controller.boardDone(loggedController.getLoggedInUser(),
                    loggedController.getSelectedBoard().getTeam(), loggedController.getSelectedBoard().getBoardName());
        } else if ((matcher = Controller.controller.getCommandMatcher
                ("board --new --name ([^ ]+) --token (.*)", input)).matches()) {
            LoggedController loggedController = LoggedController.getInstance(matcher.group(2));
            return "" + Controller.controller.makeBoard(
                    loggedController.getLoggedInUser(), loggedController.getLoggedTeam(), matcher.group(1));
        } else if ((matcher = Controller.controller.getCommandMatcher
                ("^board --open --task ([^ ]+) --deadline ([^ ]+) --token (.*)", input)).matches()) {
            LoggedController loggedController = LoggedController.getInstance(matcher.group(3));
            return "" + Controller.controller.updateDeadline(loggedController.getLoggedInUser()
                    , loggedController.getSelectedBoard().getTeam(),
                    matcher.group(1), matcher.group(2), loggedController.getSelectedBoard().getBoardName());
        } else if ((matcher = Controller.controller.getCommandMatcher
                ("^board --open --task ([^ ]+) --deadline ([^ ]+) --token (.*)$", input)).matches()) {
            LoggedController loggedController = LoggedController.getInstance(matcher.group(3));
            return "" + Controller.controller.updateDeadline(loggedController.getLoggedInUser()
                    , loggedController.getSelectedBoard().getTeam(),
                    matcher.group(1), matcher.group(2), loggedController.getSelectedBoard().getBoardName());
        } else if ((matcher = Controller.controller.getCommandMatcher
                ("^board --new  --category ([^ ]+) --token (.*)$", input)).matches()) {
            LoggedController loggedController = LoggedController.getInstance(matcher.group(2));
            return "" + Controller.controller.addCategory(loggedController.getLoggedInUser(),
                    loggedController.getSelectedBoard().getTeam(),
                    loggedController.getSelectedBoard().getBoardName(),
                    matcher.group(1));
        } else if ((matcher = Controller.controller.getCommandMatcher
                ("^board --add ([^ ]+) --token (.*)$", input)).matches()) {
            LoggedController loggedController = LoggedController.getInstance(matcher.group(2));
            return "" + Controller.controller.boardAddTask(loggedController.getLoggedInUser(),
                    loggedController.getSelectedBoard().getTeam(),
                    loggedController.getSelectedBoard().getBoardName(), matcher.group(1));
        } else if ((matcher = Controller.controller.getCommandMatcher
                ("^board --force --category ([^ ]+) --task ([^ ]+) --token (.*)$", input)).matches()) {
            LoggedController loggedController = LoggedController.getInstance(matcher.group(3));
            return "" + Controller.controller.forceCategory(loggedController.getLoggedInUser(),
                    loggedController.getSelectedBoard().getTeam(), matcher.group(1),
                    loggedController.getSelectedBoard().getBoardName(), matcher.group(2));
        } else if ((matcher = Controller.controller.getCommandMatcher
                ("^board --category ([^ ]+) --column ([^ ]+) --token (.*)$", input)).matches()) {
            LoggedController loggedController = LoggedController.getInstance(matcher.group(3));
            return "" + Controller.controller.changeColumn(loggedController.getLoggedInUser(),
                    loggedController.getSelectedBoard().getTeam(),
                    loggedController.getSelectedBoard().getBoardName(),
                    matcher.group(1), Integer.parseInt(matcher.group(2)));
        } else if ((matcher = Controller.controller.getCommandMatcher
                ("board --category next --task ([^ ]+) --token (.*)", input)).matches()) {
            LoggedController loggedController = LoggedController.getInstance(matcher.group(2));
            return "" + Controller.controller.goToNextCategory(loggedController.getLoggedInUser(),
                    loggedController.getSelectedBoard().getTeam(), loggedController.getSelectedBoard().getBoardName(),
                    matcher.group(1));
        }


        return "-1";

    }

    private String recognizeProfileMenuCommand(String input) {
        Matcher matcher;
        if ((matcher = Controller.controller.getCommandMatcher("Profile --change --username ([^ ]+) --token (.*)", input)).matches()) {
            int response = Controller.controller.changeUserName(LoggedController.getInstance(matcher.group(2)).getLoggedInUser(), matcher.group(1));
            return "" + response;
        } else if ((matcher = Controller.controller.getCommandMatcher("Profile --change --oldPassword ([^ ]+) --newPassword ([^ ]+) --token (.*)", input)).matches()) {
            int response = Controller.controller.changePassword(LoggedController.getInstance(matcher.group(3)).getLoggedInUser(), matcher.group(1), matcher.group(2));
            return "" + response;
        } else if ((matcher = Controller.controller.getCommandMatcher("Profile --changeUserNameForAdmin --username ([^ ]+) --username2 ([^ ]+) --token (.*)", input)).matches()) {
            int response = Controller.controller.changeUserNameForAdmin(LoggedController.getInstance(matcher.group(3)).getLoggedInUser(), matcher.group(1), matcher.group(2));
            return "" + response;
        } else if ((matcher = Controller.controller.getCommandMatcher("Profile --changePasswordForAdmin --username ([^ ]+) --oldPassword ([^ ]+) --newPassword ([^ ]+) --token (.*)", input)).matches()) {
            int response = Controller.controller.changePasswordForAdmin(LoggedController.getInstance(matcher.group(4)).getLoggedInUser()
                    , matcher.group(1)
                    , matcher.group(2)
                    , matcher.group(3));
            return "" + response;
        }
        return "-1";
    }

    private String recognizeAdminMenuCommand(String input) {
        Matcher matcher;
        if ((matcher = Controller.controller.getCommandMatcher("admin ban user --user ([^ ]+) --token (.*)", input)).matches()) {
            int response = Controller.controller.banUser(matcher.group(1));
            return "" + response;
        } else if ((matcher = Controller.controller.getCommandMatcher("admin change role --user ([^ ]+) --role ([^ ]+) --token (.*)", input)).matches()) {
            int response = Controller.controller.changeRole(matcher.group(1), matcher.group(2));
            return "" + response;
        } else if ((matcher = Controller.controller.getCommandMatcher("admin show --pendingTeams --token (.*)", input)).matches()) {
            int response = Controller.controller.showPendingTeams();
            return "" + response;
        } else if ((matcher = Controller.controller.getCommandMatcher("admin accept --teams ([A-Za-z0-9 ]+) --token (.*)", input)).matches()) {
            int response = Controller.controller.acceptTeam(matcher.group(1));
            return "" + response;
        } else if ((matcher = Controller.controller.getCommandMatcher("admin reject --teams ([A-Za-z0-9 ]+) --token (.*)", input)).matches()) {
            int response = Controller.controller.rejectTeam(matcher.group(1));
            return "" + response;
        } else if ((matcher = Controller.controller.getCommandMatcher("admin send --notification (.*) --team (.*) --token (.*)", input)).matches()) {
            int response = Controller.controller.sendNotificationForTeam(LoggedController.getInstance(matcher.group(3)).getLoggedInUser(), matcher.group(2), matcher.group(1));
            return "" + response;
        } else if ((matcher = Controller.controller.getCommandMatcher("admin send --notification (.*) --all --token (.*)", input)).matches()) {
            int response = Controller.controller.sendToAll(matcher.group(1), LoggedController.getInstance(matcher.group(2)).getLoggedInUser());
            return "" + response;
        } else if ((matcher = Controller.controller.getCommandMatcher("admin send --notification (.*) --user ([^ ]+) --token (.*)", input)).matches()) {
            int response = Controller.controller.sendNotificationForUser(LoggedController.getInstance(matcher.group(3)).getLoggedInUser(), matcher.group(2), matcher.group(1));
            return "" + response;
        } else if ((matcher = Controller.controller.getCommandMatcher("admin remove user --user ([^ ]+) --token (.*)", input)).matches()) {
            int response = Controller.controller.removeUser(matcher.group(1));
            return "" + response;
        } else if ((matcher = Controller.controller.getCommandMatcher("admin --changeEmail --username %s --email ([^ ]+) --token (.*)", input)).matches()) {
            int response = Controller.controller.changeEmail(User.getUserByUsername(matcher.group(1)), matcher.group(2));
            return "" + response;
        } else if ((matcher = Controller.controller.getCommandMatcher("admin --hidden --username ([^ ]+)", input)).matches()) {
            int response = Controller.controller.hiddenUser(matcher.group(1));
            return "" + response;
        }
        return "-1";
    }

    private String recognizeGetObjectsCommand(String input) {
        Matcher matcher;
        if ((matcher = Controller.controller.getCommandMatcher
                ("get LoggedUser --token (.*)"
                        , input)).matches()) {
            JsonObjectController<User> jsonObjectController = new JsonObjectController<User>();
            return jsonObjectController.createJsonObject
                    (LoggedController.getInstance(matcher.group(1)).getLoggedInUser());
        } else if ((matcher = Controller.controller.getCommandMatcher
                ("get LoggedBoard --token (.*)"
                        , input)).matches()) {
            JsonObjectController<Board> jsonObjectController = new JsonObjectController<Board>();
            return jsonObjectController.createJsonObject
                    (LoggedController.getInstance(matcher.group(1)).getSelectedBoard());
        } else if ((matcher = Controller.controller.getCommandMatcher
                ("get LoggedTeam --token (.*)"
                        , input)).matches()) {
            JsonObjectController<Team> jsonObjectController = new JsonObjectController<Team>();
            return jsonObjectController.createJsonObject
                    (LoggedController.getInstance(matcher.group(1)).getLoggedTeam());
        } else if ((matcher = Controller.controller.getCommandMatcher
                ("get SelectedTask --token (.*)"
                        , input)).matches()) {
            JsonObjectController<Task> jsonObjectController = new JsonObjectController<Task>();
            return jsonObjectController.createJsonObject
                    (LoggedController.getInstance(matcher.group(1)).getSelectedTask());
        } else if ((matcher = Controller.controller.getCommandMatcher
                ("get SelectedTeamForTask --token (.*)"
                        , input)).matches()) {
            JsonObjectController<Team> jsonObjectController = new JsonObjectController<Team>();
            return jsonObjectController.createJsonObject
                    (LoggedController.getInstance(matcher.group(1)).getSelectedTeamForTask());
        } else if ((matcher = Controller.controller.getCommandMatcher
                ("get SelectedTeam --token (.*)"
                        , input)).matches()) {
            JsonObjectController<Team> jsonObjectController = new JsonObjectController<Team>();
            return jsonObjectController.createJsonObject
                    (LoggedController.getInstance(matcher.group(1)).getSelectedTeam());
        } else if ((matcher = Controller.controller.getCommandMatcher
                ("get allUsers --token (.*)"
                        , input)).matches()) {
            JsonObjectController<ArrayList<User>> jsonObjectController = new JsonObjectController<ArrayList<User>>();
            return jsonObjectController.createJsonObject
                    (User.getUsers());
        } else if ((matcher = Controller.controller.getCommandMatcher
                ("get allTeams --token (.*)"
                        , input)).matches()) {
            JsonObjectController<ArrayList<Team>> jsonObjectController = new JsonObjectController<ArrayList<Team>>();
            return jsonObjectController.createJsonObject
                    (Team.getAllTeams());
        }else if ((matcher = Controller.controller.getCommandMatcher
                ("get pendingTeams --token (.*)"
                        , input)).matches()) {
            JsonObjectController<ArrayList<Team>> jsonObjectController = new JsonObjectController<ArrayList<Team>>();
            return jsonObjectController.createJsonObject
                    (Team.getPendingTeams());
        }
        return "-1";

    }

    private String recognizeRegisterLoginCommand(String input) throws ParseException {
        Matcher matcher;
        if ((matcher = Controller.controller.getCommandMatcher
                ("user create --username ([^ ]+) --password1 ([^ ]+) --password2 ([^ ]+) --email Address ([^ ]+)$"
                        , input)).matches()) {
            int response = Controller.controller.register(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4));
            return Integer.toString(response);
        } else if ((matcher = Controller.controller.getCommandMatcher
                ("user login --username ([^ ]+) --password ([^ ]+)"
                        , input)).matches()) {
            int response = Controller.controller.logIn(matcher.group(1), matcher.group(2));
            String token = makeToken();
            if (response == 3 || response == 4 || response == 5) {
                LoggedController.getInstance(token).setLoggedInUser(User.getUserByUsername(matcher.group(1)));
            }
            return ("" + response + " --token " + token);
        }
        return "-1";
    }

    private String makeToken() {
        String token = UUID.randomUUID().toString();
        return token;
    }
}


