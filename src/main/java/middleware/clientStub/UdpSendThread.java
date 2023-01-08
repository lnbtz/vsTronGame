package middleware.clientStub;

import config.Config;
import org.javatuples.Pair;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpSendThread implements Runnable{
    private int udpPort = Config.UDP_PORT;
    private DatagramSocket udpClientSocket;
    private SendQueue sendQueue;

    public UdpSendThread(SendQueue sendQueue){
        this.sendQueue = sendQueue;
        try {
            udpClientSocket = new DatagramSocket();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        while (true){
            Pair<InetAddress, byte[]> msg = sendQueue.dequeueUdp();
            InetAddress ip = msg.getValue0();
            byte[] data = msg.getValue1();

            DatagramPacket packet = new DatagramPacket(data, data.length, ip, udpPort);
            try {
                udpClientSocket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
