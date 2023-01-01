package middleware.serverStub;

import config.Config;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpRecieveThread extends Thread {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    InputStream in;
    private final int tcpPort = Config.TCP_PORT;
    private RecieveQueue recieveQueue;

    public TcpRecieveThread(RecieveQueue recieveQueue) {
        this.recieveQueue = recieveQueue;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(tcpPort);
            while (true) {
                // accept blocked by queue?
                clientSocket = serverSocket.accept();
                in = clientSocket.getInputStream();
                byte[] marshalledData = in.readAllBytes();
                in.close();
                clientSocket.close();

                recieveQueue.enqueue(marshalledData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
