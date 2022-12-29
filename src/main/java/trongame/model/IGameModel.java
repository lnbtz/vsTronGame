package trongame.model;

import java.util.List;

public interface IGameModel {
    void loadConfig();

    void updatePlayingField();

    void initGame(int numberOfPlayers, List<Integer> listOfPlayers);

    void handleSteeringEvent(int playerNumber, int newDirection);
}
