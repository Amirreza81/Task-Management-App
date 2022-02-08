package controller;

import model.Date;
import model.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {
    public static final Controller controller = new Controller();

    public int register(String username, String password1, String password2, String email) {
        if (isUsernameAvailable(username)) {
            return 1;
        } else if (!password1.equals(password2)) {
            return 2;
        } else if (isEmailAvailable(email)) {
            return 3;
        } else if (!getCommandMatcher("[A-Za-z0-9.]+(@gmail.com|@yahoo.com)", email).matches()) {
            return 4;
        }
        User user = new User(username, password1, email);
        for (Team team : Team.getAllTeams()) {
            if (team.getInvitedFriends() == null) continue;
            for (String emailOfInvitedFriends : team.getInvitedFriends()) {
                if (emailOfInvitedFriends.equals(email)) {
                    team.getTeamMembers().add(user);
                }
            }
        }
        return 0;
    }

    public int logIn(String username, String password) throws ParseException {
        if (!isUsernameAvailable(username)) {
            return 1;
        } else if (!password.equals(User.getUserByUsername(username).getPassword())) {
            return 2;
        }
        //View.print("user logged in successfully!");
        DateTimeFormatter currentDate = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        Log log = new Log(findUser(username), currentDate.format(now));
        String role = User.getUserByUsername(username).getRole();
        if (role.equals("Member")) {
            //View.runMemberMenu(User.getUserByUsername(username));
            return 3;
        } else if (role.equals("Leader")) {
            //View.runLeaderMenu(User.getUserByUsername(username));
            return 4;
        } else if (role.equals("System Admin")) {
            //View.runAdminMenu(User.getUserByUsername(username));
            return 5;
        }
        return 0;
    }

    public int printMenu(User user) {
        if (user.getRole().equals("Member")) {
            return 1;
        } else if (user.getRole().equals("Leader")) {
            return 2;
        } else if (user.getRole().equals("System Admin")) {
            return 3;
        }
        return 0;
    }

    public int changePassword(User user, String oldPassword, String newPassword) {
        if (!oldPassword.equals(user.getPassword())) {
            return 1;
        } else if (newPassword.equals(oldPassword)) {
            return 2;
        } else if (!getCommandMatcher("(?=.*[A-Z])(?=.*\\d)(?!.*[&%$]).{8,}", newPassword).matches()) {
            return 3;
        }
        user.setPassword(newPassword);
        return 0;
    }

    public int changeUserName(User user, String newUsername) {
        if (!getCommandMatcher(".{4,}", newUsername).matches()) {
            return 1;
        } else if (isUsernameAvailable(newUsername)) {
            return 2;
        } else if (!getCommandMatcher("[A-Za-z0-9_]+", newUsername).matches()) {
            return 3;
        } else if (newUsername.equals(user.getUserName())) {
            return 4;
        }
        user.setUserName(newUsername);
        return 0;
    }

    public ArrayList<String> showTeams(User user) {
        ArrayList<String> result = new ArrayList<>();
        ArrayList<Integer> teamID = new ArrayList<>();
        for (Team team : user.getUserTeams()) {
            teamID.add(team.getTeamNumber());
        }
        Collections.sort(teamID);
        for (Team team : user.getUserTeams()) {
            for (Integer id : teamID) {
                if (team.getTeamNumber() == id) {
                    result.add(team.getTeamName());
                    break;
                }
            }
        }
//        HashMap<Team, Date> joiningDate = sortJoiningDates(user.getJoiningDate());
//        HashMap<Date, ArrayList<Team>> data = new HashMap<>();
//        for (Map.Entry<Team, Date> unit : joiningDate.entrySet()) {
//            ArrayList<Team> teams;
//            teams = data.get(unit.getValue());
//            if (teams == null) {
//                teams = new ArrayList<>();
//                teams.add(unit.getKey());
//                data.put(unit.getValue(), teams);
//            } else {
//                teams.add(unit.getKey());
//            }
//        }
//        ArrayList<Date> check = new ArrayList<Date>();
//        for (Map.Entry<Team, Date> unit : joiningDate.entrySet()) {
//            if (check.contains(unit.getValue())) continue;
//            check.add(unit.getValue());
//            ArrayList<Team> teams = data.get(unit.getValue());
//            ArrayList<String> names = new ArrayList<>();
//            for (Team team : teams) {
//                names.add(team.getTeamName());
//            }
//            Collections.sort(names);
//            for (String name : names) {
//                result.add(name);
//            }
//        }
        return result;
    }

    public int showTeam(User user, String teamName) {
        for (Team team : Team.getAllTeams()) {
            if (teamName.equals(team.getTeamName())) {
                return 1;
            }
        }
        return 0;
    }

    public ArrayList<User> sortUsersByDate(User user) {

        return null;
    }

    public int editTaskTitle(User user, Task task, String command) {
        if (!user.getRole().equals("Leader"))
            return 0;
        else {
            task.setTitle(command);
            return 1;
        }
    }

    public int editTaskDescription(User user, Task task, String command) {
        if (!user.getRole().equals("Leader"))
            return 0;
        else {
            task.setDescription(command);
            return 1;
        }
    }

    public int editTaskPriority(User user, Task task, String command) {
        if (!user.getRole().equals("Leader"))
            return 0;
        else {
            task.setPriority(command);
            return 1;
        }
    }

    public int editTaskDeadline(User user, Task task, String command) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd|HH:mm");
        Matcher matcher = getCommandMatcher("([\\d]{4})/([\\d]{2})/([\\d]{2})[|]([\\d]{2}):([\\d]{2})", command);
        matcher.matches();
        if (!user.getRole().equals("Leader"))
            return 0;
        else if (Integer.parseInt(matcher.group(2)) > 12 ||
                Integer.parseInt(matcher.group(2)) < 0 ||
                (Integer.parseInt(matcher.group(2)) > 6 && Integer.parseInt(matcher.group(3)) > 30) ||
                (Integer.parseInt(matcher.group(2)) <= 6 && Integer.parseInt(matcher.group(3)) > 31) ||
                Integer.parseInt(matcher.group(4)) > 24 ||
                Integer.parseInt(matcher.group(5)) > 60
        )
            return 1;
        else if (new Date(command).getDate().before(task.getDeadline().getDate()))
            return 1;
        else {
            task.setDeadline(new Date(command));
            return 2;
        }
    }

    public int removeAssignedUsers(User user, Task task, User userForEdit) {
        if (!user.getRole().equals("Leader"))
            return 0;
        else if (userForEdit == null)
            return 1;
        else {
            task.getAssignedUser().remove(userForEdit);
            return 2;
        }
    }

    public int addAssignedUsers(User user, Task task, User userForEdit) {
        if (!user.getRole().equals("Leader"))
            return 0;
        else if (userForEdit == null)
            return 1;
        else {
            task.getAssignedUser().add(userForEdit);
            return 2;
        }
    }

    public void sendMessage(User user, ChatRoom chatRoom, String command) {

    }

    public ArrayList<String> showTasks(Team team) throws ParseException {
        ArrayList<String> arrayList = new ArrayList();
        ArrayList<Task> sorted = new ArrayList<>();
        ArrayList<Task> tasks = team.getAllTasks();
        if (tasks.isEmpty()) {
            arrayList.add("no task to show ");
            return arrayList;
        }
        ArrayList<java.util.Date> dates = new ArrayList<>();
        for (Task task : tasks) {
            dates.add(task.getDeadline().getDate());
        }
        Collections.sort(dates);
        for (java.util.Date date : dates) {
            for (Task task : tasks) {
                if (task.getDeadline().getDate().equals(date))
                    if (!sorted.contains(task))
                        sorted.add(task);
            }
        }
        int rank = 1;
        for (Task task : sorted) {
            arrayList.add("" + rank + ". " + showTask("" + task.getCreationId()));
            rank++;
        }
        return arrayList;
    }

    public String showTask(String taskId) {
        StringBuilder stringBuilder = new StringBuilder();
        Task task = Task.getTaskByIdWithoutTeam(taskId);
        if (task == null) {
            stringBuilder.append("there is no task whit this id");
            return stringBuilder.toString();
        }
        stringBuilder.append(task.getTitle());
        stringBuilder.append(" : id ");
        stringBuilder.append(taskId);
        stringBuilder.append(" ,Creation date: ");
        stringBuilder.append(task.getDateOfCreation().toString());
        stringBuilder.append(" ,deadline: ");
        stringBuilder.append(task.getDeadline().toString());
        stringBuilder.append(" ,assigned to : ");
        stringBuilder.append(getAssignedMembers(task));
        stringBuilder.append(" priority : ");
        stringBuilder.append(task.getPriority());
        return stringBuilder.toString();
    }

    public int makeBoard(User user, Team team, String boardName) {
        if (!user.getRole().equals("Leader"))
            return 0;
        Board board = Board.getBoardByName(team.getBoards(), boardName);
        if (board != null)
            return 1;
        else {
            board = new Board(boardName, user, team);
            return 2;
        }
    }

    public int removeBoard(User user, Team team, String boardName) {
        if (!user.getRole().equals("Leader"))
            return 0;
        Board board = Board.getBoardByName(team.getBoards(), boardName);
        if (board == null)
            return 1;
        else {
            team.getBoards().remove(board);
            return 2;
        }
    }

    public int addCategory(User user, Team team, String boardName, String categoryName) {
        if (!user.getRole().equals("Leader"))
            return 0;
        Board board = Board.getBoardByName(team.getBoards(), boardName);
        if (board == null)
            return 1;
        Category category = Category.getCategoryByName(board.getAllCategories(), categoryName);
        if (category != null)
            return 2;
        else {
            category = new Category(board, categoryName);
            return 3;
        }
    }

    public int changeColumn(User user, Team team, String boardName, String categoryName, int column) {
        if (!user.getRole().equals("Leader"))
            return 0;
        Board board = Board.getBoardByName(team.getBoards(), boardName);
        if (board == null)
            return 1;
        Category category = Category.getCategoryByName(board.getAllCategories(), categoryName);
        if (category == null)
            return 2;
        if (column < 0 || column > board.getAllCategories().size() - 1)
            return 3;
        else {
            board.setAllCategories(changeArrangement(board.getAllCategories(), category, column));
            return 4;
        }

    }

    public ArrayList<Category> changeArrangement(ArrayList<Category> categories, Category category, int column) {
        ArrayList<Category> rearranged = new ArrayList<>();
        categories.remove(category);
        for (int i = 0; i < column; i++) {
            rearranged.add(categories.get(i));
        }
        rearranged.add(category);
        for (int i = column; i < categories.size(); i++) {
            rearranged.add(categories.get(i));
        }
        return rearranged;
    }

    public int boardDone(User user, Team team, String boardName) {
        if (!user.getRole().equals("Leader"))
            return 0;
        Board board = Board.getBoardByName(team.getBoards(), boardName);
        if (board == null)
            return 1;
        if (board.getAllCategories().isEmpty())
            return 2;
        else {
            board.setCreated(true);
            return 3;
        }


    }

    public int boardAddTask(User user, Team team, String boardName, String taskId) {
        if (!user.getRole().equals("Leader"))
            return 0;
        Board board = Board.getBoardByName(team.getBoards(), boardName);
        if (board == null)
            return 1;
        if (!board.isCreated())
            return 2;
        Task task = Task.getTaskById(team, taskId);
        if (task == null)
            return 3;
        if (board.getBoardTask().contains(task))
            return 4;
        if (Date.getTimeBetween(Date.getNow(), task.getDeadline()) < 0)
            return 5;
        if (task.getAssignedUser().isEmpty())
            return 6;
        else {
            board.getBoardTask().add(task);
            return 7;
        }
    }


    public int boardAssignMember(User user, Team team, String username, String boardName, String taskId) {
        if (!user.getRole().equals("Leader"))
            return 0;
        Board board = Board.getBoardByName(team.getBoards(), boardName);
        if (board == null)
            return 1;
        if (!board.isCreated())
            return 2;
        Task task = Task.getTaskById(team, taskId);
        if (task == null)
            return 3;
        if (!board.getBoardTask().contains(task))
            return 3;
        User user1 = User.getUserByUsername(username);
        if (user1 == null)
            return 4;
        if (board.getDone().contains(task))
            return 5;
        else {
            if (!task.getAssignedUser().contains(user1)) {
                task.getAssignedUser().add(user1);
            }
            return 6;
        }
    }

    public int forceCategory(User user, Team team, String categoryName, String boardName, String taskTitle) {
        if (!user.getRole().equals("Leader"))
            return 0;
        Board board = Board.getBoardByName(team.getBoards(), boardName);
        if (board == null)
            return 1;
        if (!board.isCreated())
            return 2;
        Task task = Task.getTaskByTitle(board.getBoardTask(), taskTitle);
        if (task == null)
            return 3;
        if (!board.getBoardTask().contains(task))
            return 3;
        if (Date.getTimeBetween(Date.getNow(), task.getDeadline()) < 0)
            return 4;
        Category category = Category.getCategoryByName(board.getAllCategories(), categoryName);
        if (category == null)
            return 5;
        else {
            removeFromCategories(task, board);
            category.getCategoryTasks().add(task);
            return 6;
        }
    }

    private int removeFromCategories(Task task, Board board) {
        int index = -1;
        int it = 0;
        for (Category category : board.getAllCategories()) {
            if (category.getCategoryTasks().contains(task)) {
                index = it;
                category.getCategoryTasks().remove(task);
            }
            it++;
        }
        return index;
    }

    //public ArrayList<Category> updateColumns(ArrayList<Category> oldColumns, Category category){
    //    return null;
    //}

    public int goToNextCategory(User user, Team team, String boardName, String taskTitle) {
        Board board = Board.getBoardByName(team.getBoards(), boardName);
        if (board == null)
            return 1;
        if (!board.isCreated())
            return 2;
        Task task = Task.getTaskByTitle(board.getBoardTask(), taskTitle);
        if (task == null)
            return 3;
        if (!board.getBoardTask().contains(task))
            return 3;
        if (user.getRole().equals("Member")) {
            if (!task.getAssignedUser().contains(user))
                return 0;
        }
//        if (Date.getTimeBetween(Date.getNow(), task.getDeadline()) < 0)
//            return 4;
        int index = removeFromCategories(task, board);
        if (index == -1)
            return 5;
        else {
            if (index == board.getAllCategories().size() - 1) {
                board.getDone().add(task);
                increaseScore(team, task);
                return 6;
            }
            board.getAllCategories().get(index + 1).getCategoryTasks().add(task);
            return 6;
        }
    }

    private void increaseScore(Team team, Task task) {
        for (User user1 : task.getAssignedUser()) {
            Integer score = team.getScoreboard().getScores().get(user1);
            if (score == null) {
                score = 10;
                user1.setScore(user1.getScore() + 10);
            } else {
                score = score + 10;
                user1.setScore(user1.getScore() + 10);
            }
            team.getScoreboard().getScores().put(user1, score);
        }
    }

    private void decreaseScore(Team team, Task task) {
        for (User user1 : task.getAssignedUser()) {
            Integer score = team.getScoreboard().getScores().get(user1);
            if (score == null) {
                score = -5;
                user1.setScore(user1.getScore() - 5);
            } else {
                score = score - 5;
                user1.setScore(user1.getScore() - 5);
            }
            team.getScoreboard().getScores().put(user1, score);
        }
    }

    public void updateFailed(Board board) throws ParseException {
        for (Task task : board.getBoardTask()) {
            if (!board.getFailed().contains(task) && !board.getDone().contains(task)) {
                if (task.getDeadline().getDate().before(Date.getNow().getDate())) {
                    removeFromCategories(task, board);
                    board.getBoardTask().remove(task);
                    board.getFailed().add(task);
                    decreaseScore(board.getTeam(), task);
                }
            }

        }
    }


    public ArrayList<String> showDoneOrFailed(Team team, String doneOrFailed, String boardName) {
        ArrayList<String> result = new ArrayList<>();
        Board board = Board.getBoardByName(team.getBoards(), boardName);
        if (board == null) {
            result.add("There is no board with this name");
        } else {
            if (doneOrFailed.equals("done")) {
                result.add("done tasks : ");
                for (Task task : board.getDone()) {
                    result.add("task id :" + task.getCreationId() + " task title : " + task.getTitle());
                }
            } else {
                result.add("Failed tasks : ");
                for (Task task : board.getFailed()) {
                    result.add("task id :" + task.getCreationId() + " task title : " + task.getTitle());
                }
            }
        }
        return result;
    }

    public int updateDeadline(User user, Team team, String taskTitle, String deadline, String boardName) {
        if (!user.getRole().equals("Leader"))
            return 0;
        Board board = Board.getBoardByName(team.getBoards(), boardName);
        if (board == null)
            return 1;
        if (!board.isCreated())
            return 2;
        Task task = Task.getTaskByTitle(board.getBoardTask(), taskTitle);
        if (task == null)
            return 3;
        if (!board.getBoardTask().contains(task))
            return 3;
        if (!board.getFailed().contains(task))
            return 4;
        Matcher matcher = getCommandMatcher("([\\d]{4})/([\\d]{2})/([\\d]{2})[|]([\\d]{2}):([\\d]{2})", deadline);
        if (!matcher.matches())
            return 5;
        else {
            task.setDeadline(new Date(deadline));
            board.getFailed().remove(task);
            return 6;
        }
    }

    public int getBoardCompletionPercentage(Board board) {
        float percentage = (float) (board.getDone().size()) / (float) (board.getBoardTask().size());
        return (int) (percentage * 100);
    }

    public int getBoardFailedPercentage(Board board) {
        float percentage = (float) (board.getFailed().size()) / (float) (board.getBoardTask().size());
        return (int) (percentage * 100);
    }

    public Category getCategory(Board board, Task task) {
        for (Category category : board.getAllCategories()) {
            if (category.getCategoryTasks().contains(task))
                return category;
        }
        return null;
    }

    public String getAssignedMembers(Task task) {
        StringBuilder stringBuilder = new StringBuilder();
        for (User user : task.getAssignedUser()) {
            stringBuilder.append(",");
            stringBuilder.append(user.getUserName());
        }
        return stringBuilder.toString();
    }

    public String getStatus(Board board, Task task) {
        String status = "in progress";
        if (board.getDone().contains(task))
            status = "done";
        if (board.getFailed().contains(task))
            status = "failed";
        return status;
    }


    public ArrayList<String> boardShow(Team team, String boardName) {
        ArrayList<String> arrayList = new ArrayList<>();
        Board board = Board.getBoardByName(team.getBoards(), boardName);
        if (board == null) {
            arrayList.add("invalid boardName ");
            return arrayList;
        }
        arrayList.add("Board name : " + boardName);
        arrayList.add("Board Completion : " + getBoardCompletionPercentage(board) + " %");
        arrayList.add("Board Failed : " + getBoardFailedPercentage(board) + " %");
        arrayList.add("Board leader : " + team.getTeamLeader().getUserName());
        arrayList.add("Board tasks : ");
        arrayList.add("Highest Priority :");
        for (Task task : board.getBoardTask()) {
            if (task.getPriority().equals("Highest")) {
                addTaskDetails(arrayList, board, task);
            }
        }
        for (Task task : board.getBoardTask()) {
            if (task.getPriority().equals("High")) {
                addTaskDetails(arrayList, board, task);
            }
        }
        for (Task task : board.getBoardTask()) {
            if (task.getPriority().equals("Low")) {
                addTaskDetails(arrayList, board, task);
            }
        }
        for (Task task : board.getBoardTask()) {
            if (task.getPriority().equals("Lowest")) {
                addTaskDetails(arrayList, board, task);
            }
        }
        return arrayList;
    }

    private void addTaskDetails(ArrayList<String> arrayList, Board board, Task task) {
        arrayList.add("Title : " + task.getTitle());
        String categoryName = "no category";
        if (getCategory(board, task) != null)
            categoryName = getCategory(board, task).getName();
        arrayList.add("Category : " + categoryName);
        arrayList.add(("Description : " + task.getDescription()));
        arrayList.add("Creation Date : " + task.getDateOfCreation().toString());
        arrayList.add("Deadline : " + task.getDeadline().toString());
        arrayList.add("Assigned to : " + getAssignedMembers(task));
        arrayList.add("Status : " + getStatus(board, task));
        arrayList.add("--");
    }

//    public ArrayList<String> showChatRoom(Team team) {
//        ArrayList<String> result = new ArrayList<>();
//        for (Message message : team.getChatRoom().getAllMassages()) {
//            result.add(message.getSender().getUserName() + " : \"" + message.getText() + "\"");
//        }
//        return result;
//    }


    public ArrayList<java.util.Date> showDeadLines(User user) throws ParseException {

        ArrayList<java.util.Date> allTaskDate = new ArrayList<>();
        for (Task task : user.getAllTasksForUser()) {
            allTaskDate.add(task.getDeadline().getDate());
        }
        Collections.sort(allTaskDate);
        HashMap<String, java.util.Date> allTask = new HashMap<>();
        for (Task task : user.getAllTasksForUser()) {
            allTask.put(task.getTeam().getTeamName(), task.getDeadline().getDate());
        }
        return allTaskDate;

    }

    public Team showSpecialTeam(User user, String command) {
        Team selectTeam = null;
        for (Team team : Team.getAcceptedTeams()) {
            if (command.equals(String.valueOf(team.getTeamNumber())) || command.equals(team.getTeamName()))
                selectTeam = team;
        }
        return selectTeam;
    }

    public int creatTeam(User user, String command) {
        Team team = Team.getTeamByName(command, Team.getAllTeams());
        if (team != null)
            return 1;
        else if (!getCommandMatcher("((?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{5,12})", command).matches())
            return 2;
        else if (getCommandMatcher("\\d", command.split("")[0]).matches())
            return 2;
        else {
            Date now = Date.getNow();
            new Team(command, user, now);
            return 3;
        }

    }

    public ArrayList<Task> showTasksForLeader(User user, Team team) {
        ArrayList<Task> tasks = new ArrayList<>();
        for (Task task : team.getAllTasks()) {
            tasks.add(task);
        }
        return tasks;
    }

    public int creatTask(User user, Team team, String title, String dateOfCreation, String deadline, String description, String priority) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd|HH:mm");
        boolean isTaskAlready = false;
        for (Task task : team.getAllTasks()) {
            if (title.equals(task.getTitle())) {
                isTaskAlready = true;
                break;
            }
        }
        Matcher matcher = getCommandMatcher("([\\d]{4})/([\\d]{2})/([\\d]{2})[|]([\\d]{2}):([\\d]{2})", dateOfCreation);
        matcher.matches();
        Matcher matcher2 = getCommandMatcher("([\\d]{4})/([\\d]{2})/([\\d]{2})[|]([\\d]{2}):([\\d]{2})", deadline);
        matcher2.matches();
        if (isTaskAlready) {
            return 1;
        } else if (!matcher.matches()) {
            return 2;
        } else if (Integer.parseInt(matcher.group(2)) > 12 ||
                Integer.parseInt(matcher.group(2)) < 0 ||
                (Integer.parseInt(matcher.group(2)) > 6 && Integer.parseInt(matcher.group(3)) > 30) ||
                (Integer.parseInt(matcher.group(2)) <= 6 && Integer.parseInt(matcher.group(3)) > 31) ||
                Integer.parseInt(matcher.group(4)) > 24 ||
                Integer.parseInt(matcher.group(5)) > 60
        )
            return 2;
        else if (!matcher2.matches()) {
            return 3;
        } else if (Integer.parseInt(matcher2.group(2)) > 12 ||
                Integer.parseInt(matcher2.group(2)) < 0 ||
                (Integer.parseInt(matcher2.group(2)) > 6 && Integer.parseInt(matcher2.group(3)) > 30) ||
                (Integer.parseInt(matcher2.group(2)) <= 6 && Integer.parseInt(matcher2.group(3)) > 31) ||
                Integer.parseInt(matcher2.group(4)) > 24 ||
                Integer.parseInt(matcher2.group(5)) > 60
        )
            return 3;
        else {
            team.getAllTasks().add(new Task(title, new Date(dateOfCreation), new Date(deadline), team, description, priority));
            return 4;
        }
    }

    public ArrayList<String> showMembers(User user, Team team) {
        ArrayList<String> names = new ArrayList<>();
        for (User user1 : team.getTeamMembers()) {
            names.add(user1.getUserName());
        }
        for (User user2 : team.getSuspendedMembers()) {
            names.add(user2.getUserName() + "    (Suspended)");
        }
        Collections.sort(names);
        return names;
    }

    public int addMember(User user, Team team, String command) {
        if (!isUsernameAvailable(command))
            return 1;
        else {
            team.getTeamMembers().add(findUser(command));
            findUser(command).getUserTeams().add(team);
            findUser(command).getJoiningDate().put(team, team.getCreationDate());
            team.getScoreboard().getScores().put(findUser(command), 0);
            return 2;
        }
    }

    public int deleteMember(User user, Team team, String command) {
        if (!isUsernameAvailable(command))
            return 1;
        else {
            team.getTeamMembers().remove(findUser(command));
            findUser(command).getUserTeams().remove(team);
            findUser(command).getJoiningDate().remove(team);
            team.getScoreboard().getScores().remove(findUser(command));
            return 2;
        }
    }

    public int suspendMember(User user, Team team, String command) {
        if (!isUsernameAvailable(command))
            return 1;
        else {
            team.getSuspendedMembers().add(findUser(command));
            team.getTeamMembers().remove(findUser(command));
            return 2;
        }
    }

    public int promoteMember(User user, Team team, String command) {
        if (!isUsernameAvailable(command))
            return 1;
        else {
            team.setTeamLeader(findUser(command));
            findUser(command).setRole("Leader");
            user.setRole("Member");
            team.getTeamMembers().remove(findUser(command));
//            findUser(command).getUserTeams().remove(team);
//            findUser(command).getJoiningDate().remove(team);
            team.getScoreboard().getScores().remove(findUser(command));
            return 2;
        }
    }

    public int assignMember(User user, Team team, String command1, String command2) {
        if (!isUsernameAvailable(command2))
            return 1;
        else if (findTask(team, command1) == null)
            return 2;
        else {
            Task task = findTask(team, command1);
            task.getAssignedUser().add(findUser(command2));
            findUser(command2).getAllTasksForUser().add(task);
            return 3;
        }
    }

    public ArrayList<String> showScoreBoard(User user, Team team) throws ParseException {
        for (Board board : team.getBoards()) {
            updateFailed(board);
        }
        HashMap<User, Integer> scores = sortBoard
                (team.getScoreboard().getScores());
        ArrayList<String> result = new ArrayList<>();
        HashMap<Integer, ArrayList<User>> data = new HashMap<>();
        for (Map.Entry<User, Integer> unit : scores.entrySet()) {
            ArrayList<User> users;
            users = data.get(unit.getValue());
            if (users == null) {
                users = new ArrayList<>();
                users.add(unit.getKey());
                data.put(unit.getValue(), users);
            } else {
                users.add(unit.getKey());
            }
        }

        ArrayList<Integer> check = new ArrayList<Integer>();
        for (Map.Entry<User, Integer> unit : scores.entrySet()) {
            if (check.contains(unit.getValue())) continue;
            check.add(unit.getValue());
            ArrayList<User> users = data.get(unit.getValue());
            ArrayList<String> names = new ArrayList<>();
            for (User user1 : users) {
                names.add(user1.getUserName());
            }
            Collections.sort(names);
            for (String name : names) {
                User user1 = User.getUserByUsername(name);
                int score = scores.get(user1);
                result.add(name + " : " + score);
            }
        }
        return result;

    }

    public ArrayList<String> showRoadMap(User user, Team team) {
        ArrayList<String> result = new ArrayList<>();
        if (team.getRoadMap().getCreationDates().isEmpty()) {
            result.add("no task yet");
            return result;
        }
        HashMap<Task, Date> creationDates = sortRoadMap(team.getRoadMap().getCreationDates());
        HashMap<Date, ArrayList<Task>> data = new HashMap<>();

        for (Map.Entry<Task, Date> unit : creationDates.entrySet()) {
            ArrayList<Task> tasks;
            tasks = data.get(unit.getValue());
            if (tasks == null) {
                tasks = new ArrayList<>();
                tasks.add(unit.getKey());
                data.put(unit.getValue(), tasks);
            } else {
                tasks.add(unit.getKey());
            }
        }

        ArrayList<Date> check = new ArrayList<Date>();
        for (Map.Entry<Task, Date> unit : creationDates.entrySet()) {
            if (check.contains(unit.getValue())) continue;
            check.add(unit.getValue());
            ArrayList<Task> tasks = data.get(unit.getValue());
            ArrayList<String> titles = new ArrayList<>();
            for (Task task : tasks) {
                titles.add(task.getTitle() + " : " + team.getRoadMap().getTasksStatus().get(task) + " % done");
            }
            Collections.sort(titles);
            for (String title : titles) {
                result.add(title);
            }
        }
        return result;
    }

    public ArrayList<User> showScoreBoardForLeader(User user, Team team) {
        ArrayList<Integer> score = new ArrayList<>();
        ArrayList<User> sort = new ArrayList<>();
        for (User user1 : team.getTeamMembers()) {
            score.add(user1.getScore());
        }
        Collections.sort(score);
        int number = 1;
        for (Integer score1 : score) {
            if (number > 1) {
                number--;
                continue;

            }
            for (User user1 : team.getTeamMembers()) {
                if (user1.getScore() == score1) {
                    sort.add(user1);
                    number++;
                }
            }
        }
        return sort;
    }

    public int sendNotificationForUser(User sender, String receiver, String command) {
        if (!isUsernameAvailable(receiver))
            return 1;
        else {
            User receiverUser = findUser(receiver);
            receiverUser.getNotifications().add(new Notification(command, sender, 0));
            return 2;
        }
    }

    //Will only be sent to accepted teams
    public int sendNotificationForTeam(User sender, String team, String command) {
        Team receiverTeam = Team.getTeamByName(team, Team.getAcceptedTeams());
        if (receiverTeam == null)
            return 1;
        else {
            Notification notification = new Notification(command, sender, 1);
            for (User user : Controller.controller.findTeam(team).getTeamMembers()) {
                user.getNotifications().add(notification);
            }
            return 2;
        }
    }

    public int showProfile(String username) {
        if (!isUsernameAvailable(username)) {
            return 1;
        }
        return 0;
    }

    public int banUser(String username) {
        if (!isUsernameAvailable(username)) {
            return 1;
        }
        User findUser = User.getUserByUsername(username);
        for (Team team : findUser.getUserTeams()) {
            team.getScoreboard().getScores().remove(findUser);
            team.getTeamMembers().remove(findUser);
        }
        User.getUsers().remove(User.getUserByUsername(username));
        return 0;
    }

    public int changeRole(String username, String role) {
        if (!isUsernameAvailable(username)) {
            return 1;
        }
        findUser(username).setRole(role);
        return 0;
    }

    public int showPendingTeams() {
        if (Team.getPendingTeams().size() == 0) {
            return 1;
        }
        return 0;
    }

    public int acceptTeam(String teamName) {
        int counter = 0;
        String[] teamsNames = teamName.split(" ");
        for (String string : teamsNames) {
            for (Team team : Team.getPendingTeams()) {
                if (string.equals(team.getTeamName())) {
                    counter++;
                }
            }
        }
        if (counter == teamsNames.length) {
            for (String string : teamsNames) {
                Team.getPendingTeams().remove(findTeam(string));
                Team.getAcceptedTeams().add(findTeam(string));
            }
            return 0;
        }
        return 1;
    }

    public int rejectTeam(String teamName) {
        int counter = 0;
        String[] teamsNames = teamName.split(" ");
        for (String string : teamsNames) {
            for (Team team : Team.getPendingTeams()) {
                if (string.equals(team.getTeamName())) {
                    counter++;
                }
            }
        }
        if (counter == teamsNames.length) {
            for (String string : teamsNames) {
                findTeam(string).getTeamMembers().get(0).getUserTeams().remove(findTeam(string));
                findTeam(string).getTeamMembers().get(0).getJoiningDate().remove(findTeam(string));
                Team.getPendingTeams().remove(findTeam(string));
            }
            return 0;
        }
        return 1;
    }

    public HashMap<User, Integer> sortBoard(HashMap<User, Integer> hashMap) {
        List<Map.Entry<User, Integer>> valueList =
                new LinkedList<Map.Entry<User, Integer>>(hashMap.entrySet());
        Comparator comparator = new Comparator<Map.Entry<User, Integer>>() {
            public int compare(Map.Entry<User, Integer> operand1, Map.Entry<User, Integer> operand2) {
                return (operand2.getValue()).compareTo(operand1.getValue());
            }
        };
        Collections.sort(valueList, comparator);

        HashMap<User, Integer> sorted = new LinkedHashMap<User, Integer>();
        for (Map.Entry<User, Integer> unit : valueList) {
            sorted.put(unit.getKey(), unit.getValue());
        }
        return sorted;

    }

    public HashMap<Task, Date> sortRoadMap(HashMap<Task, Date> hashMap) {
        // a random date for comparing to other dates
        Date comparingDate = new Date("1300/01/01|00:00");
        List<Map.Entry<Task, Date>> valueList =
                new LinkedList<Map.Entry<Task, Date>>(hashMap.entrySet());
        Comparator comparator = new Comparator<Map.Entry<Task, Date>>() {
            public int compare(Map.Entry<Task, Date> operand1, Map.Entry<Task, Date> operand2) {
                return (Date.getDaysBetween(operand2.getValue(), comparingDate)).compareTo
                        (Date.getDaysBetween(operand1.getValue(), comparingDate));
            }
        };
        Collections.sort(valueList, comparator);

        HashMap<Task, Date> sorted = new LinkedHashMap<Task, Date>();
        for (Map.Entry<Task, Date> unit : valueList) {
            sorted.put(unit.getKey(), unit.getValue());
        }
        return sorted;
    }

    public HashMap<Team, Date> sortJoiningDates(HashMap<Team, Date> hashMap) {
        // a random date for comparing to other dates
        Date comparingDate = new Date("1300/01/01|00:00");
        List<Map.Entry<Team, Date>> valueList =
                new LinkedList<Map.Entry<Team, Date>>(hashMap.entrySet());
        Comparator comparator = new Comparator<Map.Entry<Team, Date>>() {
            public int compare(Map.Entry<Team, Date> operand1, Map.Entry<Team, Date> operand2) {
                try {
                    return (operand1.getValue().getDate()).compareTo
                            (operand2.getValue().getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        };
        Collections.sort(valueList, comparator);

        HashMap<Team, Date> sorted = new LinkedHashMap<Team, Date>();
        for (Map.Entry<Team, Date> unit : valueList) {
            sorted.put(unit.getKey(), unit.getValue());
        }
        return sorted;
    }

    public Matcher getCommandMatcher(String pattern, String input) {
        Pattern pattern1 = Pattern.compile(pattern);
        Matcher matcher = pattern1.matcher(input);
        matcher.matches();
        return matcher;

    }

    public User findAssignedUsers(Team team, String command) {
        for (User user : team.getTeamMembers()) {
            if (user.getUserName().equals(command))
                return user;
        }
        return null;
    }

    public Team findTeam(String teamName) {
        for (Team team : Team.getAllTeams()) {
            if (team.getTeamName().equals(teamName)) {
                return team;
            }
        }
        return null;
    }

    private boolean isUsernameAvailable(String command) {
        for (User user : User.getUsers()) {
            if (user.getUserName().equals(command))
                return true;
        }
        return false;
    }

    private boolean isEmailAvailable(String command) {
        for (User user : User.getUsers()) {
            if (user.getEmail().equals(command))
                return true;
        }
        return false;
    }

    private boolean isTeamAvailable(String command) {
        for (Team team : Team.getAllTeams()) {
            if (team.getTeamName().equals(command)) {
                return true;
            }
        }
        return false;
    }

    private User findUser(String command) {
        for (User user : User.getUsers()) {
            if (user.getUserName().equals(command))
                return user;
        }
        return null;
    }


    private Task findTask(Team team, String command) {
        for (Task task : team.getAllTasks()) {
            if (Integer.parseInt(command) == task.getCreationId())
                return task;
        }
        return null;
    }


    public int sendToAll(String notification1, User loggedInUser) {
        Notification notification = new Notification(notification1, loggedInUser, 0);
        for (User user : User.getUsers()) {
            user.getNotifications().add(notification);
        }
        return 1;
    }

    public int hiddenUser(String username) {
        if (User.getUserByUsername(username).isHidden()) {
            User.getUserByUsername(username).setHidden(false);
        } else {
            User.getUserByUsername(username).setHidden(true);
        }
        return 1;
    }

    public int sendNotificationForTask(User user, Team team, Task task, String memberName) {
        for (User user1 : team.getTeamMembers()) {
            if (user1.getUserName().equals(memberName))
                sendNotificationForUser(user, user1.getUserName(), ("You have successfully added to task " + task.getTitle()));
            else
                sendNotificationForUser(user, user1.getUserName(), ("" + memberName + " successfully added to task x " + task.getTitle()));
        }
        return 1;
    }

    public int changeUserNameForAdmin(User loggedInUser, String oldUsername, String newUsername) {
        if (!isUsernameAvailable(oldUsername))
            return 5;
        else if (!getCommandMatcher(".{4,}", newUsername).matches()) {
            return 1;
        } else if (isUsernameAvailable(newUsername)) {
            return 2;
        } else if (!getCommandMatcher("[A-Za-z0-9_]+", newUsername).matches()) {
            return 3;
        } else if (newUsername.equals(findUser(oldUsername).getUserName())) {
            return 4;
        }
        findUser(oldUsername).setUserName(newUsername);
        return 0;
    }

    public int changePasswordForAdmin(User loggedInUser, String username, String oldPassword, String newPassword) {
        User user;
        if (loggedInUser.getRole().equals("System Admin") && username != null)
            user = findUser(username);
        else
            user = loggedInUser;
        if (user == null)
            return 4;
        else if (!oldPassword.equals(user.getPassword())) {
            return 1;
        } else if (newPassword.equals(oldPassword)) {
            return 2;
        } else if (!getCommandMatcher("(?=.*[A-Z])(?=.*\\d)(?!.*[&%$]).{8,}", newPassword).matches()) {
            return 3;
        }
        user.setPassword(newPassword);
        return 0;
    }

    public int removeUser(String username) {
        if (!isUsernameAvailable(username)) {
            return 1;
        }
        User findUser = User.getUserByUsername(username);
        ArrayList<Task> tasks = findUser.getAllTasksForUser();
        for (Task task : tasks) {
            task.getAssignedUser().remove(findUser);
        }
        ArrayList<Team> teams = findUser.getUserTeams();
        for (Team team : teams) {
            team.getTeamMembers().remove(findUser);
        }
        User.getUsers().remove(findUser);
        return 0;
    }

    public int invite(String email, String teamName) {
        Team team = Team.getTeamByName(teamName, Team.getAllTeams());
        team.getInvitedFriends().add(email);
        return 1;
    }

    public int changeEmail(User user, String email) {
        user.setEmail(email);
        return 1;
    }
}
