package trongame.model;

import config.Config;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import trongame.controller.IGameController;
import trongame.controller.IPublisher;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class TronModel implements IGameModel {
    private int numberOfPlayers;
    private final int[][] gameBoard = new int[Config.ROWS][Config.COLUMNS];
    List<Integer> listOfPlayers;
    IGameController gameController;
    IPublisher publisher;

    HashMap<Integer, Integer> validDirectionMap;

    HashMap<Integer, int[]> playerNumberBikePositionDirection;

    public TronModel(IGameController gameController, IPublisher publisher) {
        this.gameController = gameController;
        this.publisher = publisher;
    }

    @Override
    public void initGame(int numberOfPlayers, List<Integer> listOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        this.listOfPlayers = listOfPlayers;
        playerNumberBikePositionDirection = new HashMap<>();
        validDirectionMap = new HashMap<>();
        clearBoard();
        initPlayers();
        setStartingPositions();
    }

    @Override
    public void handleSteeringEvent(int playerNumber, int newDirection) {
        if (isValidDirection(playerNumber, newDirection)) {
            playerNumberBikePositionDirection.get(playerNumber)[2] = newDirection;
        }
    }

    @Override
    public void updatePlayingField() {
        updatePlayerPositions();
        updateGameScreen();
    }

    private void initPlayers() {
        setFairStartingPos();
    }

    private void setFairStartingPos() {
        // x,y,direction
        List<int[]> playerPos = new ArrayList<>();
        playerPos.add(new int[]{gameBoard[0].length - ((Double) (gameBoard[0].length * 0.8)).intValue(), gameBoard.length - ((Double) (gameBoard.length * 0.8)).intValue(), Config.DOWN});
        playerPos.add(new int[]{gameBoard[0].length - ((Double) (gameBoard[0].length * 0.2)).intValue(), gameBoard.length - ((Double) (gameBoard.length * 0.2)).intValue(), Config.UP});
        playerPos.add(new int[]{gameBoard[0].length - ((Double) (gameBoard[0].length * 0.8)).intValue(), gameBoard.length - ((Double) (gameBoard.length * 0.2)).intValue(), Config.UP});
        playerPos.add(new int[]{gameBoard[0].length - ((Double) (gameBoard[0].length * 0.2)).intValue(), gameBoard.length - ((Double) (gameBoard.length * 0.8)).intValue(), Config.DOWN});
        playerPos.add(new int[]{gameBoard[0].length - ((Double) (gameBoard[0].length * 0.5)).intValue(), gameBoard.length - ((Double) (gameBoard.length * 0.2)).intValue(), Config.UP});
        playerPos.add(new int[]{gameBoard[0].length - ((Double) (gameBoard[0].length * 0.5)).intValue(), gameBoard.length - ((Double) (gameBoard.length * 0.8)).intValue(), Config.DOWN});

        for (int i = 0; i < numberOfPlayers; i++) {
            playerNumberBikePositionDirection.put(listOfPlayers.get(i), playerPos.get(i));
            validDirectionMap.put(listOfPlayers.get(i), playerPos.get(i)[2]);
        }
    }

    private void setStartingPositions() {
        for (Map.Entry<Integer, int[]> entry : playerNumberBikePositionDirection.entrySet()) {
            gameBoard[entry.getValue()[0]][entry.getValue()[1]] = entry.getKey();
        }
        updateGameScreen();
    }

    private void updateGameScreen() {
        gameController.updateGameUI(playerNumberBikePositionDirection);
    }

    private void deletePlayer(int playerNumber) {
        List<Integer> playerPositions = new ArrayList<>();
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[0].length; j++) {
                if (gameBoard[i][j] == playerNumber) {
                    playerPositions.add(i);
                    playerPositions.add(j);
                    gameBoard[i][j] = 0;
                }
            }
        }
        gameController.deletePlayer(playerPositions);
    }

    private void updatePlayerPositions() {
        checkCollisions();
        if (draw()) {
            gameController.gameOver("draw");
        } else if (foundWinner()) {
            gameController.gameOver(playerNumberBikePositionDirection.keySet().toArray()[0].toString());
        } else {
            playerNumberBikePositionDirection.forEach((playerNumber, playerPositionAndDirection) -> {
                // update playerPosition
                switch (playerPositionAndDirection[2]) {
                    case Config.LEFT:
                        playerNumberBikePositionDirection.get(playerNumber)[1] = playerPositionAndDirection[1] - 1;
                        validDirectionMap.replace(playerNumber, Config.LEFT);
                        break;
                    case Config.RIGHT:
                        playerNumberBikePositionDirection.get(playerNumber)[1] = playerPositionAndDirection[1] + 1;
                        validDirectionMap.replace(playerNumber, Config.RIGHT);
                        break;
                    case Config.UP:
                        playerNumberBikePositionDirection.get(playerNumber)[0] = playerPositionAndDirection[0] - 1;
                        validDirectionMap.replace(playerNumber, Config.UP);
                        break;
                    case Config.DOWN:
                        playerNumberBikePositionDirection.get(playerNumber)[0] = playerPositionAndDirection[0] + 1;
                        validDirectionMap.replace(playerNumber, Config.DOWN);
                        break;
                }
                // make move on the board
                makeMoveOnTheBoard(playerNumber, playerPositionAndDirection[0], playerPositionAndDirection[1]);
            });
        }
    }

    private boolean foundWinner() {
        return playerNumberBikePositionDirection.size() == 1;
    }

    private boolean draw() {
        return playerNumberBikePositionDirection.size() == 0;
    }

    private boolean isValidDirection(int playerNumber, int newDirection) {
        int currentDirection = validDirectionMap.get(playerNumber);
        if (currentDirection == Config.UP && newDirection == Config.DOWN) return false;
        else if (currentDirection == Config.DOWN && newDirection == Config.UP) return false;
        else if (currentDirection == Config.RIGHT && newDirection == Config.LEFT) return false;
        else return currentDirection != Config.LEFT || newDirection != Config.RIGHT;
    }

    private void checkCollisions() {
        int[][] newPositions = new int[numberOfPlayers][];
        List<Integer> toBeRemoved = new ArrayList<>();
        AtomicInteger index = new AtomicInteger();
        // check for border or path collision
        playerNumberBikePositionDirection.forEach((playerNumber, playerPositionAndDirection) -> {
            switch (playerPositionAndDirection[2]) {
                case Config.LEFT:
                    if (playerPositionAndDirection[1] - 1 < 0 || gameBoard[playerPositionAndDirection[0]][playerPositionAndDirection[1] - 1] != 0) {
                        toBeRemoved.add(playerNumber);
                    }
                    newPositions[index.get()] = new int[]{playerNumber, playerPositionAndDirection[0], playerPositionAndDirection[1]};
                    break;
                case Config.RIGHT:
                    if (playerPositionAndDirection[1] + 1 > Config.COLUMNS - 1 || gameBoard[playerPositionAndDirection[0]][playerPositionAndDirection[1] + 1] != 0) {
                        toBeRemoved.add(playerNumber);
                    }
                    newPositions[index.get()] = new int[]{playerNumber, playerPositionAndDirection[0], playerPositionAndDirection[1]};
                    break;
                case Config.UP:
                    if (playerPositionAndDirection[0] - 1 < 0 || gameBoard[playerPositionAndDirection[0] - 1][playerPositionAndDirection[1]] != 0) {
                        toBeRemoved.add(playerNumber);
                    }
                    newPositions[index.get()] = new int[]{playerNumber, playerPositionAndDirection[0], playerPositionAndDirection[1]};
                    break;
                case Config.DOWN:
                    if (playerPositionAndDirection[0] + 1 > Config.ROWS - 1 || gameBoard[playerPositionAndDirection[0] + 1][playerPositionAndDirection[1]] != 0) {
                        toBeRemoved.add(playerNumber);
                    }
                    newPositions[index.get()] = new int[]{playerNumber, playerPositionAndDirection[0], playerPositionAndDirection[1]};
                    break;
            }
            index.getAndIncrement();
        });
        // check for frontal collision
        for (int i = 0; i < newPositions.length; i++) {
            for (int j = i + 1; j < newPositions.length - 1; j++) {
                if (newPositions[i][1] == newPositions[j][1] && newPositions[i][2] == newPositions[j][2]) {
                    toBeRemoved.add(newPositions[i][0]);
                    toBeRemoved.add(newPositions[j][0]);
                }
            }
        }
        // delete and remove crashed bikes
        for (Integer playerNumber :
                toBeRemoved) {
            numberOfPlayers--;
            deletePlayer(playerNumber);
            playerNumberBikePositionDirection.remove(playerNumber);
        }
    }

    private void makeMoveOnTheBoard(int playerNumber, int y, int x) {
        gameBoard[y][x] = playerNumber;
    }

    private void clearBoard() {
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[0].length; j++) {
                gameBoard[i][j] = 0;
            }
        }
    }

    @Override
    public void loadConfig() {

    }
}
