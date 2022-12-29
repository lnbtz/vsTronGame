package trongame.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ISubscriber {
    void updateTimer(int time);

    void updateGameUI(HashMap<Integer, int[]> playerNumbersAndPositions);

    void deletePlayer(List<Integer> positions);
}
