package trongame.view;

import config.Config;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
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

    IGameController tronController;
    Stage window;

    private final GameScreen gameScreen;
    private final StartScreen startScreen;

    private final LobbyScreen lobbyScreen;
    private final EndScreen endScreen;
    int id;

    public TronView(Stage primaryStage, IGameController controller) throws IOException {
        this.tronController = controller;
        this.window = primaryStage;
        window.setTitle("TRON - Light Cycles");
        this.startScreen = new StartScreen(tronController, this);
        this.lobbyScreen = new LobbyScreen(tronController);
        this.endScreen = new EndScreen(tronController);
        this.gameScreen = new GameScreen(tronController, this);
        this.showStartScreen();
    }

    @Override
    public void showStartScreen() {
        showScreen(startScreen);
    }

    @Override
    public void showEndScreen(String outcome) {
        showScreen(endScreen);
        endScreen.renderOutcome(outcome);
    }

    @Override
    public void showGameScreen() {
        showScreen(gameScreen);
        gameScreen.requestFocus();
    }

    @Override
    public void showLobbyScreen() {
        showScreen(lobbyScreen);
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
    public void updatePlayercount(int playercount) {
        this.lobbyScreen.updatePlayercount(playercount);
    }

    @Override
    public void deletePlayer(List<Integer> positions) {
        gameScreen.deletePlayer(positions);
    }

    private void showScreen(Pane screen) {
        Scene scene = new Scene(screen, Config.WIDTH, Config.HEIGHT + 15);
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
        this.startScreen.setId(String.valueOf(id));
        this.lobbyScreen.setId(String.valueOf(id));
        this.endScreen.setId(String.valueOf(id));
        this.gameScreen.setId(String.valueOf(id));
    }
}
