import controller.ServerController;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

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
        ServerController serverController = ServerController.getInstance();
        String[] parts = input.split(" ");
        if (input.startsWith("serial ")) {
            return serverController.draw(parts[1]);
        } else if (input.startsWith("set number ")) {
            return serverController.changeN(parts[2]);
        } else if (input.equals("list")) {
            return serverController.getWinners();
        }
        // this means the command is not meaningful
        return "";
    }
}
