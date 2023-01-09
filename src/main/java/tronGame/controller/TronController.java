package tronGame.controller;

import config.Config;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;
import tronGame.model.IGameModel;
import tronGame.model.TronModel;
import tronGame.view.IGameView;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class TronController implements IGameController, IPublisher {
    private int currentNumberOfPlayers = 0;
    private List<Integer> listOfPlayers = new ArrayList<>();
    private List<IGameView> subscribedViews = new ArrayList<>();
    private IGameModel tronModel;
    private Thread timer;

    private boolean gameOver = false;
    private String outcome;

    @Override
    public void handleInput(int playerNumber, int input) {
        if (input == Config.GO_TO_LOBBY) changeScreen(Config.GO_TO_LOBBY, playerNumber);
        else if (input == Config.GO_TO_GAME) changeScreen(Config.GO_TO_GAME, 0);
        else {
            tronModel.handleSteeringEvent(playerNumber, input);
        }
    }

    @Override
    public void gameOver(String outcome) {
        gameOver = true;
        this.outcome = outcome;
        changeScreen(Config.GO_TO_END, 0);
        timer = new Thread(new LobbyTimer(this, false));
        timer.start();
        currentNumberOfPlayers = 0;
        listOfPlayers.clear();
    }


    @Override
    public void updateViews(Map<Integer, int[]> playerNumbersAndPositions) {
        subscribedViews.forEach(tronView -> tronView.updateGameUI(playerNumbersAndPositions)
        );
    }

    @Override
    public void deletePlayerFromViews(List<Integer> playerPositions) {
        subscribedViews.forEach(tronView -> tronView.deletePlayer(playerPositions));
    }

    private void updateTimer(int time) {
        subscribedViews.forEach(tronView -> tronView.updateTimer(time));
    }

    private void updatePlayerCount() {
        subscribedViews.forEach(tronView -> tronView.updatePlayerCount(currentNumberOfPlayers));
    }

    private void changeScreen(int screen, int playerId) {
        switch (screen) {
            case Config.GO_TO_LOBBY:
                for (IGameView gameView : subscribedViews) {
                    if (gameView.getId() == playerId) {
                        gameView.showLobbyScreen();
                    }
                }
                break;
            case Config.GO_TO_GAME:
                subscribedViews.forEach(IGameView::showGameScreen);
                gameLoop();
                break;
            case Config.GO_TO_END:
                subscribedViews.forEach(tronView -> tronView.showEndScreen(outcome));
                break;
            case Config.GO_TO_START:
                subscribedViews.forEach(IGameView::showStartScreen);
                break;
        }
    }

    private void gameLoop() {
        tronModel.initGame(currentNumberOfPlayers, listOfPlayers);
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if (gameOver) {
                        gameOver = false;
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
        final int[] time = {Config.COUNTDOWN_LENGTH};
        Timeline fiveSecondsWonder = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        event -> {
                            if (time[0] != 1) {
                                time[0]--;
                                updateTimer(time[0]);
                            } else {
                                handleInput(Config.viewId, Config.GO_TO_GAME);
                            }
                        }));
        fiveSecondsWonder.setCycleCount(Config.COUNTDOWN_LENGTH);
        fiveSecondsWonder.play();
    }


    @Override
    public void subscribe(IGameView gameView) {
        currentNumberOfPlayers++;
        listOfPlayers.add(gameView.getId());
        subscribedViews.add(gameView);
        updatePlayerCount();

        if (currentNumberOfPlayers == 1) {
            timer = new Thread(new LobbyTimer(this, true));
            timer.start();
        } else if (currentNumberOfPlayers == Config.NUMBER_OF_PLAYERS) {
            timer.interrupt();
            lobbyScreenTimer();
        }
    }

    @Override
    public void setGameModel(IGameModel tronModel) {
        this.tronModel = (TronModel) tronModel;
    }

    @Override
    public void setId(int id) {

    }

    private class LobbyTimer implements Runnable {
        boolean lobbyTimer;
        TronController gameController;

        public LobbyTimer(TronController gameController, boolean lobbyTimer) {
            this.lobbyTimer = lobbyTimer;
            this.gameController = gameController;
        }

        @Override
        public void run() {
            if (lobbyTimer) {
                try {
                    TimeUnit.SECONDS.sleep(Config.LOBBY_TIMEOUT);
                    gameController.lobbyScreenTimer();
                } catch (InterruptedException e) {
                    System.err.println("Maximum number of players reached");
                }
            } else {
                try {
                    TimeUnit.SECONDS.sleep(Config.ENDGAME_TIMEOUT);
                    gameController.changeScreen(Config.GO_TO_START, 0);
                    subscribedViews.clear();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
