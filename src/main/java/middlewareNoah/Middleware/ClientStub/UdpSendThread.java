package Middleware.ClientStub;

import Interfaces.IClientStub;
import org.javatuples.Pair;

import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class UdpSendThread extends Thread{
    int udpPort = 1235;
    DatagramSocket udpClientSocket;
    SendQueue sendQueue;

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
