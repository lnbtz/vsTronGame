package trongame.applicationStub.caller;

import com.google.gson.Gson;
import config.Config;
import middleware.ClientStub.IClientStub;
import trongame.controller.IGameController;
import trongame.controller.IPublisher;
import trongame.model.IGameModel;
import trongame.view.IGameView;

import java.util.HashMap;
import java.util.List;

public class RemoteController implements IGameController, IPublisher {

    public RemoteController(IClientStub clientStub) {
        this.clientStub = clientStub;
    }

    IClientStub clientStub;

    int id;
    Gson gson = new Gson();

    @Override
    public void handleInput(int playerNumber, int input) {
        Object[] arrayToSend = {playerNumber, input};
        String data = gson.toJson(arrayToSend);
        clientStub.invoke(Config.CONTROLLER_ID, "handleInput", data, Config.SEND_UDP);
    }

    @Override
    public void subscribe(IGameView iGameView) {
        String data = gson.toJson(iGameView.getId());
        clientStub.invoke(Config.CONTROLLER_ID, "subscribe", data, Config.SEND_TCP);
    }


    @Override
    public void setGameModel(IGameModel tronModel) {

    }

    @Override
    public void updateGameUI(HashMap<Integer, int[]> playerNumberBikePositionDirection) {

    }

    @Override
    public void deletePlayer(List<Integer> playerPositions) {

    }

    @Override
    public void gameOver(String draw) {

    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}
