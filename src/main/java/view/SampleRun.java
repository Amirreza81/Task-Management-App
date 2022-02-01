package view;

import controller.JsonController;
import controller.LoggedController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Team;
import model.User;

public class SampleRun extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        JsonController.getInstance().readFromJson();
//        Team team = Team.getTeamByName("Yakuza1",Team.getAllTeams());
        User user = User.getUserByUsername("AmirReza");
        LoggedController.getInstance().setLoggedInUser(user);
//        LoggedController.getInstance().setLoggedTeam(team);
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/TeamMenuFirstPage.fxml"));
        primaryStage.setTitle("phase2");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
