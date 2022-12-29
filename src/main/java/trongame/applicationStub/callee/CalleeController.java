package trongame.applicationStub.callee;

import com.google.gson.Gson;
import middleware.clientstub.IClientStub;
import trongame.applicationStub.caller.RemoteView;
import trongame.controller.IGameController;
import trongame.controller.IPublisher;

public class CalleeController implements ICallee {

    IGameController gameController;
    IPublisher publisher;

    IClientStub clientStub;
    Gson gson = new Gson();

    public CalleeController(IGameController gameController, IPublisher publisher, IClientStub clientStub) {
        this.clientStub = clientStub;
        this.gameController = gameController;
        this.publisher = publisher;
    }

    @Override
    public void call(int sourceId, String methodId, Object[] data) {
        if (methodId.equals("subscribe")) {
            int id = gson.fromJson((String) data[2], Integer.class);
            publisher.subscribe(id, new RemoteView(clientStub, id));
        } else {
            int playerNumber = gson.fromJson((String) data[2], Integer.class);
            int input = gson.fromJson((String) data[3], Integer.class);
            gameController.handleInput(playerNumber, input);
        }
    }

    @Override
    public String getName() {
        return "controller";
    }
}
