package trongame;

import config.Config;
import javafx.stage.Stage;
import middleware.ClientStub.ClientStubImpl;
import middleware.ClientStub.SendQueue;
import middleware.ClientStub.TcpSendThread;
import middleware.ClientStub.UdpSendThread;
import middleware.NameService.NameServiceImpl;
import middleware.NameServiceHelper;
import middleware.ServerStub.RecieveQueue;
import middleware.ServerStub.ServerStubImpl;
import middleware.ServerStub.TcpRecieveThread;
import middleware.ServerStub.UdpRecieveThread;
import trongame.applicationStub.callee.CalleeController;
import trongame.applicationStub.callee.CalleeView;
import trongame.applicationStub.caller.RemoteController;
import trongame.controller.IPublisher;
import trongame.controller.TronController;
import trongame.model.TronModel;
import trongame.view.IGameView;
import trongame.view.TronView;

import java.io.IOException;

public class TronGame {
//    IGameController tronController;
//    IPublisher publisher;
//    IGameModel tronModel;
//    IGameView tronView;
//
//    ISubscriber subscriber;
//
//    IGameController remoteController;
//
//    ServerStub serverStub;
//    IClientStub clientStub;
//    ICallee callee;
//
//    ICallee calleeController;
//    ICallee calleeView;
//    NameServerHelper nameServerHelper;

    public void startStandaloneGame(Stage stage) throws IOException {
//        this.tronController = new TronController();
//        this.publisher = (IPublisher) tronController;
//        this.tronModel = new TronModel(tronController, publisher);
//        this.tronView = new TronView(stage, tronController);
////        publisher.subscribe(tronView);
//        tronController.setGameModel(tronModel);
    }

    public void startHostGame(Stage stage) throws IOException {

    }

    public void startClientGame(Stage stage) throws IOException {
        // start middleware
        NameServiceImpl nameServiceImpl = new NameServiceImpl();
        nameServiceImpl.start();
        NameServiceHelper nameServiceHelper = new NameServiceHelper();


        //Queues Startup
        SendQueue sendQueue = new SendQueue();
        RecieveQueue recieveQueue = new RecieveQueue();


        //Send/Recieve Threads
        TcpSendThread tcpSendThread = new TcpSendThread(sendQueue);
        tcpSendThread.start();
        TcpRecieveThread tcpRecieveThread = new TcpRecieveThread(recieveQueue);
        tcpRecieveThread.start();

        //Send/Receive Threads
        UdpSendThread udpSendThread = new UdpSendThread(sendQueue);
        udpSendThread.start();
        UdpRecieveThread udpRecieveThread = new UdpRecieveThread(recieveQueue);
        udpRecieveThread.start();

        //Stubs
        ClientStubImpl clientStub = new ClientStubImpl(nameServiceHelper, sendQueue);
        ServerStubImpl serverStub = new ServerStubImpl(recieveQueue, nameServiceHelper);
        serverStub.start();


        // controller
        TronController tronController = new TronController();
        TronModel tronModel = new TronModel(tronController, tronController);
        tronController.setGameModel(tronModel);


        RemoteController remoteController = new RemoteController(clientStub);
        remoteController.setId(Config.CONTROLLER_ID);
        TronView tronView = new TronView(stage, remoteController);


        CalleeView calleeView = new CalleeView(tronView);
        int viewId = serverStub.register(1, calleeView);
        calleeView.setId(viewId);
        Config.VIEW_ID = viewId;

        ((IPublisher) remoteController).subscribe(calleeView);
    }

    public void standAloneWithMiddleware(Stage stage) throws IOException {
        // start middleware
        NameServiceImpl nameServiceImpl = new NameServiceImpl();
        nameServiceImpl.start();
        NameServiceHelper nameServiceHelper = new NameServiceHelper();


        //Queues Startup
        SendQueue sendQueue = new SendQueue();
        RecieveQueue recieveQueue = new RecieveQueue();


        //Send/Recieve Threads
        TcpSendThread tcpSendThread = new TcpSendThread(sendQueue);
        tcpSendThread.start();
        TcpRecieveThread tcpRecieveThread = new TcpRecieveThread(recieveQueue);
        tcpRecieveThread.start();

        //Send/Receive Threads
        UdpSendThread udpSendThread = new UdpSendThread(sendQueue);
        udpSendThread.start();
        UdpRecieveThread udpRecieveThread = new UdpRecieveThread(recieveQueue);
        udpRecieveThread.start();

        //Stubs
        ClientStubImpl clientStub = new ClientStubImpl(nameServiceHelper, sendQueue);
        ServerStubImpl serverStub = new ServerStubImpl(recieveQueue, nameServiceHelper);
        serverStub.start();


        // controller
        TronController tronController = new TronController();
        TronModel tronModel = new TronModel(tronController, tronController);
        tronController.setGameModel(tronModel);

        CalleeController calleeController = new CalleeController(tronController, tronController, clientStub);
        int controllerId = serverStub.register(0, calleeController);
        calleeController.setId(controllerId);


        RemoteController remoteController = new RemoteController(clientStub);
        remoteController.setId(Config.CONTROLLER_ID);
        TronView tronView = new TronView(stage, remoteController);


        CalleeView calleeView = new CalleeView(tronView);
        int viewId = serverStub.register(1, calleeView);
        calleeView.setId(viewId);
        tronView.setId(viewId);

        //((IPublisher) remoteController).subscribe(calleeView);
    }
}
