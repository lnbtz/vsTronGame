package trongame.view.screens;

import config.Config;
import trongame.controller.IGameController;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import trongame.controller.IPublisher;
import trongame.controller.TronController;
import trongame.view.IGameView;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class StartScreen extends Pane {
    IGameController controller;
    private final Button btnStart;
    IGameView gameView;

    public StartScreen(IGameController controller, IGameView gameView) {
        this.gameView = gameView;
        btnStart = new Button("Start Game");
        this.setHeight(Config.HEIGHT);
        this.setWidth(Config.WIDTH);
        this.controller = controller;
        btnStart.relocate(Config.HEIGHT / 2 - 60, Config.WIDTH / 2 - 60);
        btnStart.setOnAction(click -> {
            btnClickEvent();
        });
        this.getChildren().add(btnStart);
        Button btnSubscribe = new Button("Subscribe");
        this.setHeight(Config.HEIGHT);
        this.setWidth(Config.WIDTH);
        this.controller = controller;
        btnSubscribe.relocate(Config.WIDTH / 2 - 60, Config.HEIGHT / 2 - 150);
        btnSubscribe.setOnAction(click -> {
            subscribeToGame();
        });
        this.getChildren().add(btnSubscribe);
    }

    private void subscribeToGame() {
        ((IPublisher) controller).subscribe(gameView);
    }

    private void btnClickEvent() {
        controller.handleInput(Config.VIEW_ID, Config.GO_TO_LOBBY);
    }
}

