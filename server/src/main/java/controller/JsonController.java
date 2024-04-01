package controller;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.com.google.gson.JsonObject;
import com.gilecode.yagson.com.google.gson.reflect.TypeToken;
import model.Board;
import model.Task;
import model.Team;
import model.User;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JsonController {

    private static JsonController JsonController;

    public static JsonController getInstance() {
        if (JsonController == null)
            JsonController = new JsonController();
        return JsonController;
    }

    public void readFromJson() {
        try {
            String jsonUser = new String(Files.readAllBytes(Paths.get("src\\main\\resources\\json\\Database.json")));
            if (jsonUser.length() > 0) {
                Database database = new YaGson().fromJson(jsonUser, Database.class);
                User.setUsers(database.users);
                User.setIdCreator(database.uI);
                Team.setAllTeams(database.teams);
                Team.setAcceptedTeams(database.accepted);
                Team.setPendingTeams(database.pending);
                Team.setTeamNumberCreator(database.tI);
                Task.setAllTasks(database.tasks);
                Task.setIdCreator(database.taI);
                Board.setAllBoards(database.boards);
                Board.setIdCreator(database.bI);
            }

        } catch (IOException e) {
            System.out.println("can't read from json");
        }
    }


    public void updateJson() {
        try {
            FileWriter writerUser = new FileWriter("src\\main\\resources\\json\\Database.json");
            Database database = new Database();
            writerUser.write(new YaGson().toJson(database));
            writerUser.close();
        } catch (IOException e) {
            System.out.println("can't create or update user");
        }

    }



}
