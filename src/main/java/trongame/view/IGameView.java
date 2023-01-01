package trongame.view;

import java.util.HashMap;
import java.util.List;

public interface IGameView {

    void updateTimer(int time);
    void updatePlayerCount(int playerCount);

    void updateGameUI(HashMap<Integer, int[]> playerNumbersAndPositions);

    void deletePlayer(List<Integer> positions);

    void showStartScreen();

    void showEndScreen(String outcome);

    void showGameScreen();

    void showLobbyScreen();
    int getId();

    void setId(int viewId);
}
