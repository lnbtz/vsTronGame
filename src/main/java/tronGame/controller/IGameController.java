package tronGame.controller;

import tronGame.model.IGameModel;

import java.util.List;
import java.util.Map;

public interface IGameController {
    void handleInput(int playerNumber, int input);

    void updateViews(Map<Integer, int[]> playerNumberBikePositionDirection);

    void deletePlayerFromViews(List<Integer> playerPositions);

    void gameOver(String draw);

    void setId(int id);

    void setGameModel(IGameModel tronModel);
}
