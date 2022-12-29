package trongame.applicationStub.caller;

import com.google.gson.Gson;
import config.Config;
import middleware.clientstub.IClientStub;
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

    Gson gson = new Gson();

    @Override
    public void handleInput(int playerNumber, int input) {
        Object[] arrayToSend = {"controller", "handleInput", gson.toJson(playerNumber), gson.toJson(input)};
        String data = gson.toJson(arrayToSend);
        clientStub.invoke(Config.CONTROLLER_ID, data, false);
    }

    @Override
    public void subscribe(int id, IGameView gameView) {
        Object[] arrayToSend = {"publisher", "subscribe", gson.toJson(id)};
        String data = gson.toJson(arrayToSend);
        clientStub.invoke(Config.CONTROLLER_ID, data, true);
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
}
