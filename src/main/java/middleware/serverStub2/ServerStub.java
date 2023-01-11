package middleware.serverStub2;

import config.Config;
import middleware.nameServiceHelper.INameServiceHelper;
import middleware.serverStub.IServerStub;
import tronGame.applicationStub.callee.ICallee;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerStub implements IServerStub {
    private final INameServiceHelper nameServerHelper;
    private final Map<Integer, ICallee> calleeMap;

    public ServerStub(INameServiceHelper nameServerHelper) {
        this.nameServerHelper = nameServerHelper;
        this.calleeMap = new HashMap<>();
        ServerStubTCP serverStubTCP = new ServerStubTCP(calleeMap);
        ServerStubUDP serverStubUDP = new ServerStubUDP(calleeMap);
        Thread TCP = new Thread(serverStubTCP);
        Thread UDP = new Thread(serverStubUDP);
        TCP.start();
        UDP.start();
    }

    @Override
    public int register(int interfaceType, ICallee callee) {
        int objectId = nameServerHelper.bind(interfaceType);
        calleeMap.put(objectId, callee);
        return objectId;
    }

    public static class ServerStubTCP implements Runnable {
        private final Map<Integer, ICallee> calleeMap;
        private ServerSocket serverSocket = null;
        private final ExecutorService threadPool = Executors.newFixedThreadPool(10);

        public ServerStubTCP(Map<Integer, ICallee> calleeMap) {
            this.calleeMap = calleeMap;
        }

        public void receive() {
            openServerSocket();
            while (true) {
                Socket clientSocket;
                try {
                    clientSocket = this.serverSocket.accept();
                } catch (IOException e) {
                    throw new RuntimeException("Error accepting client connection", e);
                }
                // TODO pass source id from ip?
                this.threadPool.execute(new WorkerRunnableTCP(clientSocket, calleeMap));
            }
        }

        @Override
        public void run() {
            receive();
        }

        private void openServerSocket() {
            try {
                this.serverSocket = new ServerSocket(Config.TCP_PORT);
            } catch (IOException e) {
                throw new RuntimeException("Cannot open port " + Config.TCP_PORT, e);
            }
        }
    }

    public static class ServerStubUDP implements Runnable {
        private final Map<Integer, ICallee> calleeMap;
        private DatagramSocket serverSocket = null;
        private final ExecutorService threadPool = Executors.newFixedThreadPool(10);

        public ServerStubUDP(Map<Integer, ICallee> calleeMap) {
            this.calleeMap = calleeMap;
        }

        @Override
        public void run() {
            receive();
        }


        private void openServerSocket() {
            try {
                this.serverSocket = new DatagramSocket(Config.UDP_PORT);
            } catch (IOException e) {
                throw new RuntimeException("Cannot open port " + Config.UDP_PORT, e);
            }
        }

        public void receive() {
            openServerSocket();
            while (true) {
                DatagramSocket clientSocket = null;
                clientSocket = this.serverSocket;
                byte[] buffer = new byte[32_000];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                try {
                    clientSocket.receive(packet);
                } catch (IOException e) {
                    System.err.println("failed receiving data in UDP Server");
                    throw new RuntimeException(e);
                }
                this.threadPool.execute(new WorkerRunnableUDP(packet, calleeMap));
            }
        }
    }
}