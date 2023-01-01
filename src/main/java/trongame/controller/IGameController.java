package trongame.controller;

import trongame.model.IGameModel;

import java.util.List;
import java.util.Map;

public interface IGameController {
    void handleInput(int playerNumber, int input);

    void setGameModel(IGameModel tronModel);

    void updateGameUI(Map<Integer, int[]> playerNumberBikePositionDirection);

    void deletePlayer(List<Integer> playerPositions);

    void gameOver(String draw);

    int getId();

    void setId(int id);
}
