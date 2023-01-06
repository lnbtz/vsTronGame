package middleware.serverStub;

import config.Config;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpReceiveThread implements Runnable {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    InputStream in;
    private final int tcpPort = Config.TCP_PORT;
    private RecieveQueue recieveQueue;

    public TcpReceiveThread(RecieveQueue recieveQueue) {
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
