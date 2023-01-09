package tronGame.model;

import java.util.List;

public interface IGameModel {

    void updatePlayingField();

    void initGame(int numberOfPlayers, List<Integer> listOfPlayers);

    void handleSteeringEvent(int playerNumber, int newDirection);
}
