package view;

import controller.Controller;
import controller.GlobalChatController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.GlobalChatMessage;
import model.PopUpMessage;
import model.Team;
import model.User;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GlobalChatView {
    public ImageView sendButton;
    public TextField textPlace;
    public GridPane gridPane;
    public int j = 0;
    public TextArea textArea;
    public String textToAppend;
    public String sendData;
    public TextArea input;
    public Label onlineLabel;


    public VBox chatVBox;
    public TextField messageText;
    public Label onlineNumberLabel;
    public ScrollPane scrollPane;
    public Button backBtn;

    @FXML
    public void initialize() {
        GlobalChatController.getInstance().setView(this);
        GlobalChatController.getInstance().initializeNetworkToSend();
        GlobalChatController.getInstance().initializeNetworkToReceive();
        scrollPane.setContent(chatVBox);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.vvalueProperty().bind(chatVBox.heightProperty());
        chatVBox.setSpacing(7);
        showOnlineCount();
        getPrevMessage();

    }

    private void getPrevMessage() {
        Team team = Controller.controller.getLoggedTeam();
        for (String s : team.getChatRoom().getAllMassages()){
            GlobalChatController.getInstance().setTextToAppend(s);
            addMessage();
        }
    }


    public void enter(KeyEvent keyEvent) {
        if (keyEvent.getCode().toString().equals("ENTER")) {
            keyEvent.consume();
            sendMessage();
        }
    }

    private void sendMessage() {
        User user = Controller.controller.getLoggedInUser();
        String username = user.getUserName();
        try {
            if (user.isHidden()){
                username= "hidden";
            }
        }
        catch (Exception nullPoint){
            //
        }
        sendData = "<" + username + "> : " + messageText.getText();
        GlobalChatMessage globalChatMessage = GlobalChatController.getInstance().sendChatMessage(sendData);
        if (globalChatMessage != GlobalChatMessage.MESSAGE_SENT) {
//            new PopUpMessage(globalChatMessage.getAlertType(), globalChatMessage.getLabel());
            messageText.clear();
            return;
        }
        messageText.clear();
    }

    public void sendByClick(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() != MouseButton.PRIMARY)
            return;
        sendMessage();
    }

    public void addMessage() {
//        String avatar = GlobalChatController.getInstance().getAvatarToAppend();
        String message = GlobalChatController.getInstance().getTextToAppend();
        Pattern pattern = Pattern.compile("<(?<username>.+)> : .+");
        Matcher matcher = pattern.matcher(message);
        String username = "";
        if (matcher.find())
            username = matcher.group("username");
//      ImageView imageView = new ImageView(avatar);
//        imageView.setFitHeight(30);
//        imageView.setFitWidth(30);
        HBox hBox = new HBox();
        Label label = new Label(message);
//        label.getStylesheets().add(getClass().getResource("/project/CSS/global_chat_view.css").toString());
        label.setId("label");
        label.setWrapText(true);
        hBox.getChildren().addAll( label);
        chatVBox.getChildren().add(hBox);
//        String finalUsername = username;
//        imageView.setCursor(Cursor.HAND);
//        imageView.setOnMouseClicked(mouseEvent -> {
//            if (mouseEvent.getButton() != MouseButton.PRIMARY)
//                return;
//            String[] data = GlobalChatController.getInstance().askForUserData(finalUsername);
//            String selectedUsername = data[0];
//            String selectedNickname = data[1];
//            String score = data[2];
//            Stage window = new Stage();
//            window.initOwner(LoginView.getStage());
//            window.initStyle(StageStyle.UNDECORATED);
//            window.initModality(Modality.WINDOW_MODAL);
//            PopUpMessage.setStage(window);
//            ImageView userImageView = new ImageView(imageView.getImage());
//            userImageView.setFitWidth(50);
//            userImageView.setFitHeight(80);
//            Label usernameLabel = new Label("Username : " + selectedUsername);
//            Label nicknameLabel = new Label("Nickname : " + selectedNickname);
//            Label scoreLabel = new Label("Score : " + score);
//            Button button = new Button("Close");
//            button.setOnAction(action -> window.close());
//            VBox dataBox = new VBox(usernameLabel, nicknameLabel, scoreLabel, new Label(""), button);
//            dataBox.setSpacing(10);
//            dataBox.setAlignment(Pos.CENTER);
//            HBox mainBox = new HBox(userImageView, dataBox);
//            mainBox.setSpacing(15);
//            mainBox.setAlignment(Pos.CENTER);
//            mainBox.getStylesheets().add(getClass().getResource("/project/CSS/global_chat_view.css").toString());
//            Scene scene = new Scene(mainBox, 250, 250);
//            window.setScene(scene);
//            window.getScene().getStylesheets().add(String.valueOf(getClass().getResource("/project/CSS/window.css")));
//            window.showAndWait();
//            PopUpMessage.setStage(LoginView.getStage());
//        });
    }

    public void showOnlineCount() {
        onlineNumberLabel.setText("Online : " + GlobalChatController.getInstance().getOnlineCount());
    }

    public void back(javafx.event.ActionEvent actionEvent) throws IOException {
        GlobalChatController.getInstance().close();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/TeamMenuSecondPage.fxml"));
        ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).setScene(new Scene(root));
    }
}
