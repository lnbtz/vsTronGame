package trongame.view.screens;

import config.Config;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.layout.Pane;
import trongame.controller.IGameController;

public class LobbyScreen extends Pane {

    Label timer;
    Label currentPlayercount;

    IGameController tronController;
    public LobbyScreen(IGameController tronController) {
        this.tronController = tronController;
        this.setHeight(Config.HEIGHT);
        this.setWidth(Config.WIDTH);

        /*
        // timer label
        Label timerLabel = new Label("Waiting...");
        timerLabel.setFont(new Font(30));
        timerLabel.setTextFill(Color.BLACK);
        timerLabel.setMinSize(200, 35);
        timerLabel.setMaxSize(200, 35);
        // timer label VBox
        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.setLayoutY(30);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPrefWidth(Config.WIDTH);
        vBox.getChildren().add(timerLabel);
        this.getChildren().add(vBox);
         */

        // setup Timer
        timer = new Label("");
        timer.setFont(new Font(30));
        timer.setMinSize(500, 40);
        timer.setMaxSize(500,40);
        timer.setTextFill(Color.RED);

        // setup plyercount
        currentPlayercount = new Label("");
        currentPlayercount.setFont(new Font(30));
        currentPlayercount.setMinSize(500, 40);
        currentPlayercount.setMaxSize(500, 40);
        currentPlayercount.setTextFill(Color.RED);

        VBox vBox1 = new VBox();
        vBox1.setSpacing(20);
        vBox1.setLayoutY(100);
        vBox1.setAlignment(Pos.CENTER);
        vBox1.setPrefWidth(Config.WIDTH);
        this.getChildren().add(vBox1);

        vBox1.getChildren().add(timer);
        vBox1.getChildren().add(currentPlayercount);
    }

    public void updatePlayercount(int playercount){
        currentPlayercount.setText("Number of players: " + playercount);
    }
    public void updateTimer(int time){
        timer.setText("Game starts in: " + time);
    }
}
