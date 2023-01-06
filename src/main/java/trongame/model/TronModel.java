package trongame.model;

import config.Config;
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

    Map<Integer, Integer> validDirectionMap;

    Map<Integer, int[]> playerNumberBikePositionDirection;

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
            validDirectionMap.put(listOfPlayers.get(i), currentDirection(playerPos.get(i)));
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
                updatePlayer(playerNumber, playerPositionAndDirection);
                // make move on the board
                makeMoveOnTheBoard(playerNumber, playerPositionAndDirection[0], playerPositionAndDirection[1]);
            });
        }
    }

    private void updatePlayer(Integer playerNumber, int[] playerPositionAndDirection) {
        int y = playerPositionAndDirection[0];
        int x = playerPositionAndDirection[1];
        int currentDirection = currentDirection(playerPositionAndDirection);
        switch (currentDirection) {
            case Config.LEFT:
                playerNumberBikePositionDirection.get(playerNumber)[1] = x - 1;
                validDirectionMap.replace(playerNumber, Config.LEFT);
                break;
            case Config.RIGHT:
                playerNumberBikePositionDirection.get(playerNumber)[1] = x + 1;
                validDirectionMap.replace(playerNumber, Config.RIGHT);
                break;
            case Config.UP:
                playerNumberBikePositionDirection.get(playerNumber)[0] = y - 1;
                validDirectionMap.replace(playerNumber, Config.UP);
                break;
            case Config.DOWN:
                playerNumberBikePositionDirection.get(playerNumber)[0] = y + 1;
                validDirectionMap.replace(playerNumber, Config.DOWN);
                break;
        }
    }

    private boolean foundWinner() {
        return playerNumberBikePositionDirection.size() == 0;
    }

    private boolean draw() {
        return playerNumberBikePositionDirection.isEmpty();
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
            checkPlayerCollision(newPositions, toBeRemoved, index, playerNumber, playerPositionAndDirection);
        });
        // check for frontal collision
        checkFrontalCollision(newPositions, toBeRemoved);
        // delete and remove crashed bikes
        deleteCrashedBikes(toBeRemoved);
    }

    private void checkPlayerCollision(int[][] newPositions, List<Integer> toBeRemoved, AtomicInteger index, Integer playerNumber, int[] playerPositionAndDirection) {
        int y = playerPositionAndDirection[0];
        int x = playerPositionAndDirection[1];
        int currentDirection = currentDirection(playerPositionAndDirection);
        switch (currentDirection) {
            case Config.LEFT:
                removeOutOfBoundsOrCollisionLeftRight(toBeRemoved, playerNumber, x - 1, y);
                newPositions[index.get()] = new int[]{playerNumber, y, x};
                break;
            case Config.RIGHT:
                removeOutOfBoundsOrCollisionLeftRight(toBeRemoved, playerNumber, x + 1, y);
                newPositions[index.get()] = new int[]{playerNumber, y, x};
                break;
            case Config.UP:
                removeOutOfBoundsOrCollisionUpDown(toBeRemoved, playerNumber, x, y - 1);
                newPositions[index.get()] = new int[]{playerNumber, y, x};
                break;
            case Config.DOWN:
                removeOutOfBoundsOrCollisionUpDown(toBeRemoved, playerNumber, x, y + 1);
                newPositions[index.get()] = new int[]{playerNumber, y, x};
                break;
        }
        index.getAndIncrement();
    }

    private void removeOutOfBoundsOrCollisionUpDown(List<Integer> toBeRemoved, Integer playerNumber, int x, int y) {
        if (y > Config.ROWS - 1 || y < 0 || gameBoard[y][x] != 0) {
            toBeRemoved.add(playerNumber);
        }
    }

    private void removeOutOfBoundsOrCollisionLeftRight(List<Integer> toBeRemoved, Integer playerNumber, int x, int y) {
        if (x > Config.COLUMNS - 1 || x < 0 || gameBoard[y][x] != 0) {
            toBeRemoved.add(playerNumber);
        }
    }

    private void deleteCrashedBikes(List<Integer> toBeRemoved) {
        for (Integer playerNumber :
                toBeRemoved) {
            numberOfPlayers--;
            deletePlayer(playerNumber);
            playerNumberBikePositionDirection.remove(playerNumber);
        }
    }

    private static void checkFrontalCollision(int[][] newPositions, List<Integer> toBeRemoved) {
        for (int i = 0; i < newPositions.length; i++) {
            for (int j = i + 1; j < newPositions.length - 1; j++) {
                if (newPositions[i][1] == newPositions[j][1] && currentDirection(newPositions[i]) == currentDirection(newPositions[j])) {
                    toBeRemoved.add(newPositions[i][0]);
                    toBeRemoved.add(newPositions[j][0]);
                }
            }
        }
    }

    private static int currentDirection(int[] playerPositionAndDirection) {
        return playerPositionAndDirection[2];
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
