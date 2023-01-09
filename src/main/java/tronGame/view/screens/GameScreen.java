package tronGame.view.screens;

import config.Config;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import tronGame.controller.IGameController;
import tronGame.view.IGameView;

import java.util.List;
import java.util.Map;

public class GameScreen extends VBox {
    // TODO mapping with IP Address Port or send playerNumber

    IGameController tronController;
    IGameView view;
    Pane gameBoard;
    Rectangle playerColor;


    public GameScreen(IGameController tronController, IGameView gameView) {
        this.view = gameView;
        this.gameBoard = new Pane();
        this.gameBoard.setMinHeight(Config.HEIGHT);
        this.gameBoard.setMinWidth(Config.WIDTH);
        this.gameBoard.setMaxHeight(Config.HEIGHT);
        this.gameBoard.setMaxWidth(Config.WIDTH);
        this.gameBoard.setStyle("-fx-background-color: gray;");


        this.tronController = tronController;

        this.setHeight(Config.HEIGHT + 15);
        this.setWidth(Config.WIDTH);
        /*
        this.setStyle("-fx-background-color: gray;");
         */
        this.setOnKeyPressed(this::takeInput);

        HBox hBox1 = new HBox();
        hBox1.setSpacing(0);
        hBox1.setLayoutY(0);
        //hBox1.setAlignment(Pos.TOP_LEFT);
        hBox1.setStyle("-fx-background-color: white;");
        hBox1.setMinSize(Config.WIDTH, 15);
        hBox1.setMaxSize(Config.WIDTH, 15);

        Label player = new Label("You are:  ");
        player.setFont(new Font(15));
        hBox1.getChildren().add(player);

        this.playerColor = new Rectangle();
        playerColor.setHeight(15);
        playerColor.setWidth(15);


        hBox1.getChildren().add(playerColor);

        this.getChildren().add(hBox1);

        this.getChildren().add(gameBoard);
    }

    private void takeInput(KeyEvent e) {
        switch (e.getCode()) {
            case DOWN: {
                tronController.handleInput(Config.viewId, Config.DOWN);
                break;
            }
            case UP: {
                tronController.handleInput(Config.viewId, Config.UP);
                break;
            }
            case RIGHT: {
                tronController.handleInput(Config.viewId, Config.RIGHT);
                break;
            }
            case LEFT: {
                tronController.handleInput(Config.viewId, Config.LEFT);
                break;
            }
        }
    }


    public void updateScreen(Map<Integer, int[]> playerNumberAndPosition) {
        switch (view.getId()) {
            case Config.RED:
                playerColor.setFill(Color.RED);
                break;
            case Config.BLUE:
                playerColor.setFill(Color.BLUE);
                break;
            case Config.GREEN:
                playerColor.setFill(Color.GREEN);
                break;
            case Config.YELLOW:
                playerColor.setFill(Color.YELLOW);
                break;
            case Config.BLACK:
                playerColor.setFill(Color.BLACK);
                break;
            case Config.PURPLE:
                playerColor.setFill(Color.PURPLE);
                break;
        }
        playerNumberAndPosition.forEach((playerNumber, position) -> {
            renderPosition(playerNumber, position[0], position[1]);
        });
    }

    public void deletePlayer(List<Integer> positions) {
        System.out.println("list with positions to delete " + positions);
        for (int i = 0; i < positions.size(); i += 2) {
            renderPosition(Config.GREEN, positions.get(i + 1), positions.get(i));
        }
    }

    private void renderPosition(Integer playerNumber, int y, int x) {
        Rectangle cycleHead = new Rectangle();
        cycleHead.setX(x * Config.SQUARE_WIDTH);
        cycleHead.setY(y * Config.SQUARE_HEIGHT);
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
        this.gameBoard.getChildren().add(cycleHead);
    }

    public void resetScreen() {
        for (int i = 0; i < Config.ROWS; i++) {
            for (int j = 0; j < Config.COLUMNS; j++) {
                renderPosition(Config.DELETE, i, j);
            }
        }
    }

}
