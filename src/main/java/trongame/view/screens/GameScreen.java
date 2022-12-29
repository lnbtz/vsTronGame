package trongame.view.screens;

import config.Config;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import trongame.controller.IGameController;
import trongame.controller.TronController;

import java.util.List;
import java.util.Map;

public class GameScreen extends Pane {
    // TODO mapping with IP Address Port or send playerNumber

    IGameController tronController;


    public GameScreen(IGameController tronController) {
        this.tronController = tronController;
        this.setHeight(Config.HEIGHT);
        this.setWidth(Config.WIDTH);
        this.setStyle("-fx-background-color: gray;");
        this.setOnKeyPressed(this::takeInput);
    }

    private void takeInput(KeyEvent e) {
        switch (e.getCode()) {
            case DOWN: {
                tronController.handleInput(Config.VIEW_ID, Config.DOWN);
                break;
            }
            case UP: {
                tronController.handleInput(Config.VIEW_ID, Config.UP);
                break;
            }
            case RIGHT: {
                tronController.handleInput(Config.VIEW_ID, Config.RIGHT);
                break;
            }
            case LEFT: {
                tronController.handleInput(Config.VIEW_ID, Config.LEFT);
                break;
            }
        }
    }


    public void updateScreen(Map<Integer, int[]> playerNumberAndPosition) {
        playerNumberAndPosition.forEach(this::renderPosition);
    }

    public void deletePlayer(List<Integer> positions) {
        for (int i = 0; i < positions.size(); i += 2) {
            renderPosition(Config.DELETE, new int[]{positions.get(i), positions.get(i + 1)});
        }
    }

    private void renderPosition(Integer playerNumber, int[] positions) {
        Rectangle cycleHead = new Rectangle();
        cycleHead.setX(positions[1] * Config.SQUARE_WIDTH);
        cycleHead.setY(positions[0] * Config.SQUARE_HEIGHT);
        cycleHead.setHeight(Config.SQUARE_HEIGHT);
        cycleHead.setWidth(Config.SQUARE_WIDTH);
        switch (playerNumber) {
            case Config.DELETE:
                cycleHead.setFill(Color.GRAY);
                break;
            case Config.RED:
                cycleHead.setFill(Color.RED);
                break;
            case Config.BLUE:
                cycleHead.setFill(Color.BLUE);
                break;
            case Config.GREEN:
                cycleHead.setFill(Color.GREEN);
                break;
            case Config.YELLOW:
                cycleHead.setFill(Color.YELLOW);
                break;
            case Config.BLACK:
                cycleHead.setFill(Color.BLACK);
                break;
            case Config.PURPLE:
                cycleHead.setFill(Color.PURPLE);
                break;
        }
        this.getChildren().add(cycleHead);
    }

}
