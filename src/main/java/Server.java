import controller.Controller;
import controller.JsonController;
import controller.JsonObjectController;
import controller.LoggedController;
import model.Board;
import model.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void processCommand(DataInputStream dataInputStream, DataOutputStream dataOutputStream) throws IOException {
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

    private String recognizeCommand(String input) {
        String[] parts = input.split(" ");
        Matcher matcher;
        if ((matcher = Controller.controller.getCommandMatcher
                ("user create --username ([^ ]+) --password1 ([^ ]+) --password2 ([^ ]+) --email Address ([^ ]+)$"
                        , input)).matches()) {
            int response = Controller.controller.register(matcher.group(1),matcher.group(2),matcher.group(3),matcher.group(4));
            return Integer.toString(response);
        } else if (input.equals("get LoggedUser")) {
            JsonObjectController<User> jsonObjectController = new JsonObjectController<User>();
            return jsonObjectController.createJsonObject
                    (LoggedController.getInstance(Thread.currentThread()).getLoggedInUser());
        } else if (input.equals("get LoggedBoard")) {
            JsonObjectController<Board> jsonObjectController = new JsonObjectController<Board>();
            return jsonObjectController.createJsonObject
                    (LoggedController.getInstance(Thread.currentThread()).getSelectedBoard());
        }
        // this means the command is not meaningful
        return "";
    }
}
