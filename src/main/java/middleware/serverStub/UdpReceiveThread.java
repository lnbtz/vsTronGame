package middleware.serverStub;

import config.Config;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpReceiveThread implements Runnable{
    private byte[] buf = new byte[65_535];
    int udpPort = Config.UDP_PORT;
    DatagramSocket udpSocket;
    private RecieveQueue recieveQueue;

    public UdpReceiveThread(RecieveQueue recieveQueue){
        this.recieveQueue = recieveQueue;
        try {
            udpSocket = new DatagramSocket(udpPort);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        while (true){
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                udpSocket.receive(packet);

                recieveQueue.enqueue(packet.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}