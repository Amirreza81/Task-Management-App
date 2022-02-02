import controller.Controller;
import controller.JsonController;
import controller.JsonObjectController;
import controller.LoggedController;
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
                processCommand(dataInputStream, dataOutputStream);
                dataInputStream.close();
                socket.close();
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void processCommand(DataInputStream dataInputStream, DataOutputStream dataOutputStream) throws IOException, ParseException {
        while (true) {
            String input;
            try {
                input = dataInputStream.readUTF();
            } catch (SocketException e) {
                break;
            }
            String result = recognizeCommand(input);
            // there should be a way to say that the client is sending non-meaningful commands and close the socket
            if (result.equals("")) break;
            dataOutputStream.writeUTF(result);
            dataOutputStream.flush();
        }
    }

    private String recognizeCommand(String input) throws ParseException {
        String[] parts = input.split(" ");
        Matcher matcher;
        if (input.startsWith("user")) {
            return recognizeRegisterLoginCommand(input);
        } else if (input.startsWith("get")) {
            return recognizeGetObjectsCommand(input);
        } else if (input.startsWith("Profile")) {
            return recognizeProfileMenuCommand(input);
        } else if (input.startsWith("board")) {
            return recognizeBoardMenuCommand(input);
        } else if (input.startsWith("admin")){
            return recognizeAdminMenuCommand(input);
        }

        // this means the command is not meaningful
        return "";
    }

    private String recognizeBoardMenuCommand(String input) throws ParseException {
        Matcher matcher;
        if ((matcher = Controller.controller.getCommandMatcher("board updateFailed --token (.*)", input)).matches()){
            Controller.controller.updateFailed(LoggedController.getInstance(matcher.group(1)).getSelectedBoard());
            return "successful";
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
        } else if ((matcher = Controller.controller.getCommandMatcher("admin send --notification (.*) --user ([^ ]+) --token %s", input)).matches()) {
            int response = Controller.controller.sendNotificationForUser(LoggedController.getInstance(matcher.group(3)).getLoggedInUser(), matcher.group(2), matcher.group(1));
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


