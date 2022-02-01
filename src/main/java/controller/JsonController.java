package controller;

import com.gilecode.yagson.YaGson;
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
//        try {
//            String jsonTask = new String(Files.readAllBytes(Paths.get("src\\main\\resources\\json\\Task.json")));
//            if (jsonTask.length() > 0) {
//                ArrayList<Task> tasks = new YaGson().fromJson(jsonTask, new TypeToken<List<Task>>() {
//                }.getType());
//                for (Task task : tasks) {
//                    Task.getAllTasks().add(task);
//                }
//            }
//        } catch (IOException e) {
//            System.out.println("can't read from json");
//        }
//        try {
//            String jsonTeam = new String(Files.readAllBytes(Paths.get("src\\main\\resources\\json\\Team.json")));
//            if (jsonTeam.length() > 0) {
//                ArrayList<ArrayList<Team>> teams = new YaGson().fromJson(jsonTeam, new TypeToken<List<List<Team>>>() {
//                }.getType());
//                for (Team team : teams.get(0)) {
//                    Team.getAllTeams().add(team);
//                }
//                for (Team team : teams.get(1)) {
//                    Team.getPendingTeams().add(team);
//                }
//                for (Team team : teams.get(2)) {
//                    Team.getAcceptedTeams().add(team);
//                }
//
//            }
//        } catch (IOException e) {
//            System.out.println("can't read from json");
//        }
//
//        try {
//            String jsonBoard = new String(Files.readAllBytes(Paths.get("src\\main\\resources\\json\\Board.json")));
//            if (jsonBoard.length() > 0) {
//                ArrayList<Board> boards = new YaGson().fromJson(jsonBoard, new TypeToken<List<Board>>() {
//                }.getType());
//                for (Board board : boards) {
//                    Board.getAllBoards().add(board);
//                }
//            }
//        } catch (IOException e) {
//            System.out.println("can't read from json");
//        }
//        try {
//            String jsonBoard = new String(Files.readAllBytes(Paths.get("src\\main\\resources\\json\\UserId.json")));
//            if (jsonBoard.length() > 0) {
//                User.setIdCreator(new YaGson().fromJson(jsonBoard, Integer.class));
//            }
//        } catch (IOException e) {
//            System.out.println("can't read from json");
//        }
//        try {
//            String jsonBoard = new String(Files.readAllBytes(Paths.get("src\\main\\resources\\json\\TeamId.json")));
//            if (jsonBoard.length() > 0) {
//                Team.setTeamNumberCreator(new YaGson().fromJson(jsonBoard, Integer.class));
//            }
//        } catch (IOException e) {
//            System.out.println("can't read from json");
//        }
//        try {
//            String jsonBoard = new String(Files.readAllBytes(Paths.get("src\\main\\resources\\json\\TaskId.json")));
//            if (jsonBoard.length() > 0) {
//                Task.setIdCreator(new YaGson().fromJson(jsonBoard, Integer.class));
//            }
//        } catch (IOException e) {
//            System.out.println("can't read from json");
//        }
//        try {
//            String jsonBoard = new String(Files.readAllBytes(Paths.get("src\\main\\resources\\json\\BoardId.json")));
//            if (jsonBoard.length() > 0) {
//                Board.setIdCreator(new YaGson().fromJson(jsonBoard, Integer.class));
//            }
//        } catch (IOException e) {
//            System.out.println("can't read from json");
//        }

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
//        try {
//            FileWriter writerUser = new FileWriter("src\\main\\resources\\json\\user.json");
//            writerUser.write(new YaGson().toJson(User.getUsers()));
//            writerUser.close();
//        } catch (IOException e) {
//            System.out.println("can't create or update user");
//        }
//        try {
//            FileWriter writerUser = new FileWriter("src\\main\\resources\\json\\json.json");
//            writerUser.write(new YaGson().toJson(User.getUsers()));
//            writerUser.close();
//        } catch (IOException e) {
//            System.out.println("can't create or update user");
//        }
//        try {
//            FileWriter writerTask = new FileWriter("src\\main\\resources\\json\\Task.json");
//            writerTask.write(new YaGson().toJson(Task.getAllTasks()));
//            writerTask.close();
//        } catch (IOException e) {
//            System.out.println("can't create or update task");
//        }
//        try {
//            FileWriter writerTeam = new FileWriter("src\\main\\resources\\json\\Team.json");
//            ArrayList<ArrayList<Team>> teams = new ArrayList<>();
//            teams.add(Team.getAllTeams());
//            teams.add(Team.getPendingTeams());
//            teams.add(Team.getAcceptedTeams());
//            writerTeam.write(new YaGson().toJson(teams));
//            writerTeam.close();
//        } catch (IOException e) {
//            System.out.println("can't create or update team");
//        }
//
//        try {
//            FileWriter writerBoard = new FileWriter("src\\main\\resources\\json\\Board.json");
//            writerBoard.write(new YaGson().toJson(Board.getAllBoards()));
//            writerBoard.close();
//        } catch (IOException e) {
//            System.out.println("can't create or update board");
//        }
//        try {
//            FileWriter writerBoard = new FileWriter("src\\main\\resources\\json\\UserId.json");
//            writerBoard.write(new YaGson().toJson(User.getIdCreator()));
//            writerBoard.close();
//        } catch (IOException e) {
//            System.out.println("can't create or update board");
//        }
//        try {
//            FileWriter writerBoard = new FileWriter("src\\main\\resources\\json\\TeamId.json");
//            writerBoard.write(new YaGson().toJson(Team.getTeamNumberCreator()));
//            writerBoard.close();
//        } catch (IOException e) {
//            System.out.println("can't create or update board");
//        }
//        try {
//            FileWriter writerBoard = new FileWriter("src\\main\\resources\\json\\TaskId.json");
//            writerBoard.write(new YaGson().toJson(Task.getIdCreator()));
//            writerBoard.close();
//        } catch (IOException e) {
//            System.out.println("can't create or update board");
//        }
//        try {
//            FileWriter writerBoard = new FileWriter("src\\main\\resources\\json\\BoardId.json");
//            writerBoard.write(new YaGson().toJson(Board.getIdCreator()));
//            writerBoard.close();
//        } catch (IOException e) {
//            System.out.println("can't create or update board");
//        }
    }
}
