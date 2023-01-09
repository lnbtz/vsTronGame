package tronGame.applicationStub.callee;

import com.google.gson.Gson;
import config.Config;
import middleware.clientStub.IClientStub;
import tronGame.applicationStub.caller.RemoteView;
import tronGame.controller.IGameController;
import tronGame.controller.IPublisher;


public class CalleeController implements ICallee {

    IGameController gameController;
    IPublisher publisher;

    IClientStub clientStub;
    Gson gson = new Gson();
    int id;

    public CalleeController(IGameController gameController, IPublisher publisher, IClientStub clientStub) {
        this.clientStub = clientStub;
        this.gameController = gameController;
        this.publisher = publisher;
    }

    @Override
    public void call(String methodId, String data) {
        if (methodId.equals("subscribe")) {
            int id = gson.fromJson(data, Integer.class);
            publisher.subscribe(new RemoteView(clientStub, id));
            gameController.handleInput(id, Config.GO_TO_LOBBY);
        } else {
            Object[] parameterArray = gson.fromJson(data, Object[].class);
            int playerNumber = ((Double) parameterArray[0]).intValue();
            int input = ((Double) parameterArray[1]).intValue();
            gameController.handleInput(playerNumber, input);
        }
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int getId) {
        this.id = getId;
    }

}
