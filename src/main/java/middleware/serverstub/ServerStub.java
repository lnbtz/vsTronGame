package middleware.serverstub;

import config.Config;
import middleware.NameServerHelper;
import trongame.applicationStub.callee.ICallee;
import util.IReceiver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerStub implements IServerStub {
    ServerStubTCP serverStubTCP;
    ServerStubUDP serverStubUDP;
    NameServerHelper nameServerHelper;

    public ServerStub(NameServerHelper nameServerHelper) {
        this.nameServerHelper = nameServerHelper;
        this.serverStubTCP = new ServerStubTCP();
        this.serverStubUDP = new ServerStubUDP();
        Thread TCP = new Thread(serverStubTCP);
        Thread UDP = new Thread(serverStubUDP);
        TCP.start();
        UDP.start();
    }

    @Override
    public void register(ICallee callee, String interfaceName) {
        serverStubUDP.register(callee);
        serverStubTCP.register(callee);
        if (interfaceName.equals("controller")) Config.CONTROLLER_ID = nameServerHelper.bind(interfaceName);
        else if (interfaceName.equals("view")) Config.VIEW_ID = nameServerHelper.bind(interfaceName);
    }

    public static class ServerStubTCP implements IReceiver, Runnable {
        List<ICallee> calleeList = new ArrayList<>();
        protected ServerSocket serverSocket = null;
        protected boolean isStopped = false;
        protected Thread runningThread = null;
        ExecutorService threadPool = Executors.newFixedThreadPool(10);

        @Override
        public void receive() {
            synchronized (this) {
                this.runningThread = Thread.currentThread();
            }
            openServerSocket();
            while (!isStopped()) {
                Socket clientSocket;
                try {
                    clientSocket = this.serverSocket.accept();
                } catch (IOException e) {
                    if (isStopped()) {
                        break;
                    }
                    throw new RuntimeException("Error accepting client connection", e);
                }
                // TODO pass source id from ip?
                this.threadPool.execute(new WorkerRunnableTCP(clientSocket, calleeList));
            }
            this.threadPool.shutdown();
        }

        @Override
        public void run() {
            receive();
        }

        private synchronized boolean isStopped() {
            return this.isStopped;
        }

        public synchronized void stop() {
            this.isStopped = true;
            try {
                this.serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException("Error closing server", e);
            }
        }

        private void openServerSocket() {
            try {
                this.serverSocket = new ServerSocket(Config.TCP_PORT);
            } catch (IOException e) {
                throw new RuntimeException("Cannot open port " + Config.TCP_PORT, e);
            }
        }

        public void register(ICallee callee) {
            calleeList.add(callee);
        }
    }

    public static class ServerStubUDP implements IReceiver, Runnable {
        List<ICallee> calleeList = new ArrayList<>();
        protected DatagramSocket serverSocket = null;
        protected boolean isStopped = false;
        protected Thread runningThread = null;
        ExecutorService threadPool =
                Executors.newFixedThreadPool(10);

        @Override
        public void run() {
            receive();
        }


        private synchronized boolean isStopped() {
            return this.isStopped;
        }

        public synchronized void stop() {
            this.isStopped = true;
            this.serverSocket.close();
        }

        private void openServerSocket() {
            try {
                this.serverSocket = new DatagramSocket(Config.UDP_PORT);
            } catch (IOException e) {
                throw new RuntimeException("Cannot open port " + Config.UDP_PORT, e);
            }
        }

        @Override
        public void receive() {
            synchronized (this) {
                this.runningThread = Thread.currentThread();
            }
            openServerSocket();
            while (!isStopped()) {
                DatagramSocket clientSocket = null;
                clientSocket = this.serverSocket;
                byte[] buffer = new byte[32000];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                try {
                    clientSocket.receive(packet);
                } catch (IOException e) {
                    System.out.println("failed receiving data in UDP Server");
                    throw new RuntimeException(e);
                }
                this.threadPool.execute(new WorkerRunnableUDP(packet, calleeList));
            }
            this.threadPool.shutdown();
        }

        public void register(ICallee callee) {
            calleeList.add(callee);
        }
    }
}
