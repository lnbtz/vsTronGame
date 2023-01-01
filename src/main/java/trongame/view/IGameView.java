package trongame.view;

import java.util.List;
import java.util.Map;

public interface IGameView {

    void updateTimer(int time);
    void updatePlayerCount(int playerCount);

    void updateGameUI(Map<Integer, int[]> playerNumbersAndPositions);

    void deletePlayer(List<Integer> positions);

    void showStartScreen();

    void showEndScreen(String outcome);

    void showGameScreen();

    void showLobbyScreen();
    int getId();

    void setId(int viewId);
}
