package tronGame.view.screens;

import config.Config;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.layout.Pane;
import tronGame.controller.IGameController;

public class LobbyScreen extends Pane {

    Label timer;
    Label currentPlayerCount;

    IGameController tronController;
    public LobbyScreen(IGameController tronController) {
        this.tronController = tronController;
        this.setHeight(Config.HEIGHT);
        this.setWidth(Config.WIDTH);

        // setup Timer
        timer = new Label("Waiting...");
        timer.setFont(new Font(30));
        timer.setMinSize(500, 40);
        timer.setMaxSize(500,40);
        timer.setTextFill(Color.RED);

        // setup playerCount
        currentPlayerCount = new Label("");
        currentPlayerCount.setFont(new Font(30));
        currentPlayerCount.setMinSize(500, 40);
        currentPlayerCount.setMaxSize(500, 40);
        currentPlayerCount.setTextFill(Color.RED);

        VBox vBox1 = new VBox();
        vBox1.setSpacing(20);
        vBox1.setLayoutY(100);
        vBox1.setAlignment(Pos.CENTER);
        vBox1.setPrefWidth(Config.WIDTH);
        this.getChildren().add(vBox1);

        vBox1.getChildren().add(timer);
        vBox1.getChildren().add(currentPlayerCount);
    }

    public void resetScreen(){
        timer.setText("Waiting...");
    }

    public void updatePlayerCount(int playerCount){
        currentPlayerCount.setText("Number of players: " + playerCount);
    }
    public void updateTimer(int time){
        timer.setText("Game starts in: " + time);
    }
}
