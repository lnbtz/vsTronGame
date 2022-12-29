package trongame.applicationStub.caller;

import com.google.gson.Gson;
import middleware.clientstub.IClientStub;
import trongame.view.IGameView;
import trongame.view.ISubscriber;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RemoteView implements IGameView, ISubscriber {
    IClientStub clientStub;

    private int id;

    public RemoteView(IClientStub clientStub, int id) {
        this.id = id;
        this.clientStub = clientStub;
    }

    Gson gson = new Gson();

    @Override
    public void showStartScreen() {
        Object[] arrayToSend = {"view", "showStartScreen"};
        String data = gson.toJson(arrayToSend);
        clientStub.invoke(id, data, true);
    }

    @Override
    public void showEndScreen(String outcome) {
        Object[] arrayToSend = {"view", "showEndScreen", gson.toJson(outcome)};
        String data = gson.toJson(arrayToSend);
        clientStub.invoke(id, data, true);
    }

    @Override
    public void showGameScreen() {
        Object[] arrayToSend = {"view", "showGameScreen"};
        String data = gson.toJson(arrayToSend);
        clientStub.invoke(id, data, true);
    }

    @Override
    public void showLobbyScreen() {
        Object[] arrayToSend = {"view", "showLobbyScreen"};
        String data = gson.toJson(arrayToSend);
        clientStub.invoke(id, data, true);
    }

    @Override
    public void updateTimer(int time) {
        Object[] arrayToSend = {"view", "updateTimer", gson.toJson(time)};
        String data = gson.toJson(arrayToSend);
        clientStub.invoke(id, data, false);
    }

    @Override
    public void updateGameUI(HashMap<Integer, int[]> playerNumbersAndPositions) {
        Object[] arrayToSend = {"view", "updateGameUI", gson.toJson(playerNumbersAndPositions)};
        String data = gson.toJson(arrayToSend);
        clientStub.invoke(id, data, false);
    }

    @Override
    public void deletePlayer(List<Integer> positions) {
        Object[] arrayToSend = {"view", "deletePlayer", gson.toJson(positions)};
        String data = gson.toJson(arrayToSend);
        clientStub.invoke(id, data, false);
    }
}
