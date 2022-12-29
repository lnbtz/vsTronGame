package trongame.controller;

import config.Config;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;
import trongame.model.IGameModel;
import trongame.model.TronModel;
import trongame.view.IGameView;

import java.util.*;

public class TronController implements IGameController, IPublisher {
    int numberOfPlayers = 0;
    List<Integer> listOfPlayers = new ArrayList<>();
    List<IGameView> subscribedViews = new ArrayList<>();
    TronModel tronModel;
    boolean timerStarted = false;

    boolean gameOver = false;
    String outcome;

    @Override
    public void handleInput(int playerNumber, int input) {
        if (input == Config.GO_TO_LOBBY) changeScreen(Config.GO_TO_LOBBY);
        else if (input == Config.GO_TO_GAME) changeScreen(Config.GO_TO_GAME);
        else if (input == Config.GO_TO_START_SCREEN) changeScreen(Config.GO_TO_START_SCREEN);
        else {
            tronModel.handleSteeringEvent(playerNumber, input);
        }
    }


    public void gameOver(String outcome) {
        gameOver = true;
        this.outcome = outcome;
        changeScreen(Config.GO_TO_END);
    }


    @Override
    public void updateGameUI(HashMap<Integer, int[]> playerNumbersAndPositions) {
        subscribedViews.forEach(tronView -> tronView.updateGameUI(playerNumbersAndPositions)
        );
    }

    public void deletePlayer(List<Integer> playerPositions) {
        subscribedViews.forEach(tronView -> tronView.deletePlayer(playerPositions));
    }

    private void updateTimer(int time) {
        subscribedViews.forEach(tronView -> tronView.updateTimer(time));
    }

    private void changeScreen(int screen) {
        // TODO how to handle different views clicking start Button?
        // TODO dont change screen via "handleInput" method and make views call changeScreen Functions on Controller?
        switch (screen) {
            case Config.GO_TO_LOBBY:
                subscribedViews.forEach(IGameView::showLobbyScreen);
                lobbyScreenTimer();
                break;
            case Config.GO_TO_GAME:
                subscribedViews.forEach(IGameView::showGameScreen);
                gameLoop();
                break;
            case Config.GO_TO_END:
                subscribedViews.forEach(tronView -> tronView.showEndScreen(outcome));
                break;
            case Config.GO_TO_START_SCREEN:
                subscribedViews.forEach(IGameView::showStartScreen);
        }
    }

    private void gameLoop() {
        tronModel.initGame(numberOfPlayers, listOfPlayers);
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if (gameOver) {
                        timer.cancel();
                    } else {
                        tronModel.updatePlayingField();
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 500, Config.DELAY);
    }

    private void lobbyScreenTimer() {
        if (timerStarted) return;
        timerStarted = true;
        // TODO make it so that ppl can join while timer is running
        final int[] time = {Config.COUNTDOWN_LENGTH};
        Timeline timer = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        event -> {
                            if (Config.NUMBER_OF_PLAYERS == numberOfPlayers) {
                                handleInput(Config.VIEW_ID, Config.GO_TO_GAME);
                            } else if (time[0] != 1) {
                                time[0]--;
                                updateTimer(time[0]);
                            } else {
                                handleInput(Config.VIEW_ID, Config.GO_TO_START_SCREEN);
                            }
                        }));
        timer.setCycleCount(Config.COUNTDOWN_LENGTH);
        timer.play();
    }


    @Override
    public void subscribe(IGameView gameView) {
        numberOfPlayers++;
        listOfPlayers.add(gameView.getId());
        subscribedViews.add(gameView);
    }

    @Override
    public void setGameModel(IGameModel tronModel) {
        this.tronModel = (TronModel) tronModel;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public void setId(int id) {

    }
}
