package trongame.view;

import config.Config;
import javafx.scene.Scene;
import javafx.stage.Stage;
import trongame.controller.IGameController;
import trongame.view.screens.EndScreen;
import trongame.view.screens.GameScreen;
import trongame.view.screens.LobbyScreen;
import trongame.view.screens.StartScreen;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class TronView implements IGameView {

    private Scene lobbyScene;
    private Scene endScene;
    private Scene gameScene;
    IGameController tronController;
    Stage window;

    private GameScreen gameScreen;
    private StartScreen startScreen;
    private Scene startScene;

    private LobbyScreen lobbyScreen;
    private EndScreen endScreen;
    int id;

    public TronView(Stage primaryStage, IGameController controller) throws IOException {
        this.tronController = controller;
        this.window = primaryStage;
        window.setTitle("TRON - Light Cycles");

        this.startScreen = new StartScreen(tronController, this);
        this.startScene = new Scene(startScreen, Config.WIDTH, Config.HEIGHT + 15);

        this.lobbyScreen = new LobbyScreen(tronController);
        this.lobbyScene = new Scene(lobbyScreen, Config.WIDTH, Config.HEIGHT + 15);

        this.gameScreen = new GameScreen(tronController, this);
        this.gameScene = new Scene(gameScreen, Config.WIDTH, Config.HEIGHT + 15);

        this.endScreen = new EndScreen(tronController);
        this.endScene = new Scene(endScreen, Config.WIDTH, Config.HEIGHT + 15);
        this.showStartScreen();
    }

    @Override
    public void showStartScreen() {
        showScreen(startScene);
    }

    @Override
    public void showLobbyScreen() {
        lobbyScreen.resetScreen();
        showScreen(lobbyScene);
    }

    @Override
    public void showGameScreen() {
        gameScreen.resetScreen();
        showScreen(gameScene);
        gameScreen.requestFocus();
    }

    @Override
    public void showEndScreen(String outcome) {
        endScreen.resetScreen();
        showScreen(endScene);
        endScreen.renderOutcome(outcome);
    }

    @Override
    public void updateGameUI(HashMap<Integer, int[]> playerNumbersAndPositions) {
        gameScreen.updateScreen(playerNumbersAndPositions);
    }

    @Override
    public void updateTimer(int time) {
        lobbyScreen.updateTimer(time);
    }

    @Override
    public void updatePlayerCount(int playerCount) {
        this.lobbyScreen.updatePlayerCount(playerCount);
    }

    @Override
    public void deletePlayer(List<Integer> positions) {
        gameScreen.deletePlayer(positions);
    }

    private void showScreen(Scene scene) {
        window.setScene(scene);
        window.show();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int viewId) {
        this.id = viewId;
    }
}
