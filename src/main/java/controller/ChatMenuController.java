package controller;
import java.io.IOException;

public class ChatMenuController {
    private static ChatMenuController instance = null;

    private ChatMenuController() {
    }

    public static ChatMenuController getInstance() {
        if (instance == null) instance = new ChatMenuController();
        return instance;
    }

    public String sendMessage(String token, String message) {
        LoggedController loggedController = LoggedController.getInstance(token);
        if(loggedController.getLoggedInUser().getRole().equals("Leader")){
            Controller.controller.sendNotificationForTeam(loggedController.getLoggedInUser(),
                    loggedController.getLoggedTeam().getTeamName(),
                    "Massage from leader of team  "+loggedController.getLoggedTeam().getTeamName()
                            +" has been sent to group chatroom : \n"+message);
        }
        LoggedController.getInstance(token).getLoggedTeam().getChatRoom().getAllMassages().add(message);
        synchronized (LoggedController.getDataForChat()) {
            for (String s : LoggedController.getDataForChat().keySet()) {
                try {
//                    if (s.equals(token))
//                        continue;
                    LoggedController.getDataForChat().get(s).writeUTF(message);
                    LoggedController.getDataForChat().get(s).flush();
                    System.out.println("message : "+ message);
                } catch (IOException e) {
                    System.out.println("ya hagh");
                    return "failed";
                }
            }
        }
        return "success";
    }
}

