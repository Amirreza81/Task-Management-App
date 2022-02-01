package controller;

import model.Board;
import model.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Controller {

    private static DataInputStream inputStream;
    private static DataOutputStream outputStream;
    public  static final Controller controller = new Controller();

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
            outputStream.writeUTF("get LoggedUser");
            outputStream.flush();
            String result = inputStream.readUTF();
            JsonObjectController jsonObjectController = new JsonObjectController(User.class);
            return (User) jsonObjectController.createJsonObject(result);
        }
         catch (IOException e) {
        return null;
        }
    }

    public Board getLoggedBoard() {
        try {
            outputStream.writeUTF("get LoggedBoard");
            outputStream.flush();
            String result = inputStream.readUTF();
            JsonObjectController jsonObjectController = new JsonObjectController(Board.class);
            return (Board) jsonObjectController.createJsonObject(result);
        }
        catch (IOException e) {
            return null;
        }

    }
}
