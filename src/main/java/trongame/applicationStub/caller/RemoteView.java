package trongame.applicationStub.caller;

import com.google.gson.Gson;
import config.Config;
import middleware.ClientStub.IClientStub;
import trongame.view.IGameView;

import java.util.HashMap;
import java.util.List;

public class RemoteView implements IGameView {
    IClientStub clientStub;

    private int id;

    public RemoteView(IClientStub clientStub, int id) {
        this.id = id;
        this.clientStub = clientStub;
    }

    Gson gson = new Gson();

    @Override
    public void showStartScreen() {
        clientStub.invoke(id, "showStartScreen", "", Config.SEND_TCP);
    }

    @Override
    public void showEndScreen(String outcome) {
        String data = gson.toJson(outcome);
        clientStub.invoke(id, "showEndScreen", data, Config.SEND_TCP);
    }

    @Override
    public void showGameScreen() {
        clientStub.invoke(id, "showGameScreen", "", Config.SEND_TCP);
    }

    @Override
    public void showLobbyScreen() {
        clientStub.invoke(id, "showLobbyScreen", "", Config.SEND_TCP);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int viewId) {
         this.id = viewId;
    }

    @Override
    public void updateTimer(int time) {
        String data = gson.toJson(time);
        clientStub.invoke(id, "updateTimer", data, Config.SEND_TCP);
    }

    @Override
    public void updatePlayercount(int playercount) {
        String data = gson.toJson(playercount);
        clientStub.invoke(id, "updatePlayercount", data, Config.SEND_TCP);
    }

    @Override
    public void updateGameUI(HashMap<Integer, int[]> playerNumbersAndPositions) {
        String data = gson.toJson(playerNumbersAndPositions);
        clientStub.invoke(id, "updateGameUI", data, Config.SEND_UDP);
    }

    @Override
    public void deletePlayer(List<Integer> positions) {
        String data = gson.toJson(positions);
        clientStub.invoke(id, "deletePlayer", data, Config.SEND_UDP);
    }
}
