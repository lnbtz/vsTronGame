package trongame;

import config.Config;
import javafx.stage.Stage;
import middleware.NameServerHelper;
import middleware.clientstub.ClientStub;
import middleware.clientstub.IClientStub;
import middleware.serverstub.ServerStub;
import trongame.applicationStub.callee.CalleeController;
import trongame.applicationStub.callee.CalleeView;
import trongame.applicationStub.callee.ICallee;
import trongame.applicationStub.caller.RemoteController;
import trongame.applicationStub.caller.RemoteView;
import trongame.controller.IGameController;
import trongame.controller.IPublisher;
import trongame.controller.TronController;
import trongame.model.IGameModel;
import trongame.model.TronModel;
import trongame.view.IGameView;
import trongame.view.ISubscriber;
import trongame.view.TronView;

import java.io.IOException;

public class TronGame {
    IGameController tronController;
    IPublisher publisher;
    IGameModel tronModel;
    IGameView tronView;

    ISubscriber subscriber;

    IGameController remoteController;

    ServerStub serverStub;
    IClientStub clientStub;
    ICallee callee;

    ICallee calleeController;
    ICallee calleeView;
    NameServerHelper nameServerHelper;

    public void startStandaloneGame(Stage stage) throws IOException {
        this.tronController = new TronController();
        this.publisher = (IPublisher) tronController;
        this.tronModel = new TronModel(tronController, publisher);
        this.tronView = new TronView(stage, tronController);
//        publisher.subscribe(tronView);
        tronController.setGameModel(tronModel);
    }

    public void startHostGame(Stage stage) throws IOException {

    }

    public void startClientGame(Stage stage) throws IOException {
        nameServerHelper = new NameServerHelper();
        clientStub = new ClientStub(nameServerHelper);
        serverStub = new ServerStub(nameServerHelper);

        remoteController = new RemoteController(clientStub);

        tronView = new TronView(stage,remoteController);
        subscriber = (ISubscriber) tronView;

        calleeView = new CalleeView(tronView,subscriber);

        serverStub.register(calleeView,"view");
        ((IPublisher) remoteController).subscribe(Config.VIEW_ID, null);
    }

    public void standAloneWithMiddleware(Stage stage) throws IOException {
        // middleware components
        nameServerHelper = new NameServerHelper();
        clientStub = new ClientStub(nameServerHelper);
        serverStub = new ServerStub(nameServerHelper);

        remoteController = new RemoteController(clientStub);

        tronView = new TronView(stage, remoteController);
        subscriber = (ISubscriber) tronView;

        tronController = new TronController();
        publisher = (IPublisher) tronController;

        tronModel = new TronModel(tronController, publisher);
        tronController.setGameModel(tronModel);

        calleeView = new CalleeView(tronView, subscriber);
        calleeController = new CalleeController(tronController, publisher,clientStub);

        serverStub.register(calleeController, "controller");
        serverStub.register(calleeView, "view");

        ((IPublisher) remoteController).subscribe(Config.VIEW_ID, null);
    }
}
