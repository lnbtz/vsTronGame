package trongame.view.screens;

import config.Config;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import trongame.controller.IGameController;
import trongame.controller.IPublisher;
import trongame.view.IGameView;

public class StartScreen extends Pane {
    IGameController controller;
    IGameView view;
    private final Button btnStart;

    public StartScreen(IGameController controller, IGameView view) {
        this.view = view;
        btnStart = new Button("PLAY - LETS GO! :D");
        this.setHeight(Config.HEIGHT);
        this.setWidth(Config.WIDTH);
        this.controller = controller;
        btnStart.relocate(Config.HEIGHT / 2 - 60, Config.WIDTH / 2 - 60);
        btnStart.setOnAction(click -> {
            btnClickEvent();
        });
        this.getChildren().add(btnStart);
    }

    private void btnClickEvent() {
        ((IPublisher) controller).subscribe(view);
    }
}

