package tronGame.view;

import java.util.List;
import java.util.Map;

public interface IGameView {

    void updateTimer(int time);
    void updatePlayerCount(int playerCount);

    void updateGameUI(Map<Integer, int[]> playerNumbersAndPositions);

    void deletePlayer(List<Integer> positions);

    void showStartScreen();

    void showLobbyScreen();

    void showGameScreen();

    void showEndScreen(String outcome);
    int getId();

    void setId(int viewId);
}
