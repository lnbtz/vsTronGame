package middleware.ClientStub;

import config.Config;
import org.javatuples.Pair;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class TcpSendThread extends Thread{
    int tcpPort = Config.TCP_PORT;
    SendQueue sendQueue;

    public TcpSendThread(SendQueue sendQueue){
        this.sendQueue = sendQueue;
    }

    @Override
    public void run(){
        while (true){
            Pair<InetAddress, byte[]> msg = sendQueue.dequeueTcp();
            InetAddress ip = msg.getValue0();
            byte[] data = msg.getValue1();

            try{
                Socket clientSocket = new Socket(ip, tcpPort);
                OutputStream out = clientSocket.getOutputStream();
                out.write(data);
                out.flush();
                out.close();
                clientSocket.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }



    }
}
