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
    int currentNumberOfPlayers = 0;
    List<Integer> listOfPlayers = new ArrayList<>();
    List<IGameView> subscribedViews = new ArrayList<>();
    TronModel tronModel;
    LobbyTimer timer;

    boolean gameOver = false;
    String outcome;

    @Override
    public void handleInput(int playerNumber, int input) {
        if (input == Config.GO_TO_LOBBY) changeScreen(Config.GO_TO_LOBBY, playerNumber);
        else if (input == Config.GO_TO_GAME) changeScreen(Config.GO_TO_GAME, 0);
        else {
            tronModel.handleSteeringEvent(playerNumber, input);
        }
    }


    public void gameOver(String outcome) {
        gameOver = true;
        this.outcome = outcome;
        changeScreen(Config.GO_TO_END, 0);
        timer = new LobbyTimer(this, false);
        timer.start();
        currentNumberOfPlayers = 0;
        listOfPlayers.clear();
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
        //Test
        /*
        tronModel.updatePlayingField();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
         */
        //endTest
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
        // TODO make it so that ppl can join while timer is running
        final int[] time = {Config.COUNTDOWN_LENGTH};
        Timeline fiveSecondsWonder = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        event -> {
                            if (time[0] != 1) {
                                time[0]--;
                                updateTimer(time[0]);
                            } else {
                                handleInput(Config.VIEW_ID, Config.GO_TO_GAME);
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
            timer = new LobbyTimer(this, true);
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
    public int getId() {
        return 0;
    }

    @Override
    public void setId(int id) {

    }

    private class LobbyTimer extends Thread {
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
                    Thread.sleep(Config.LOBBY_TIMEOUT);
                    gameController.lobbyScreenTimer();
                } catch (InterruptedException e) {
                    System.err.println("Maximum number of players reached");
                }
            } else {
                try {
                    Thread.sleep(Config.ENDGAME_TIMEOUT);
                    gameController.changeScreen(Config.GO_TO_START, 0);
                    subscribedViews.clear();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
