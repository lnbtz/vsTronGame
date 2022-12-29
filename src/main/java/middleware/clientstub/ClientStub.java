package middleware.clientstub;

import config.Config;
import middleware.IMarshaller;
import middleware.NameServerHelper;
import util.ISender;

import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class ClientStub implements IClientStub, ISender {
    public ClientStub(NameServerHelper nameServerHelper) {
        this.nameServerHelper = nameServerHelper;
    }

    NameServerHelper nameServerHelper;

    @Override
    public void invoke(int objectId, String json, boolean TCP) {
        // lookup ip for objectId at Name Service
        String ipAddress = nameServerHelper.lookup(objectId);
        // send data via TCP or UDP
        int port = TCP ? Config.TCP_PORT : Config.UDP_PORT;
        // marshal to data to send
        byte[] dataToSend = IMarshaller.marshal(json);
        // send data
        send(ipAddress, port, dataToSend, TCP);
    }

    @Override
    public void send(String ip, int port, byte[] data, boolean TCP) {
        try {
            if (TCP) {
                sendTCP(ip, port, data);
            } else {
                sendUDP(ip, port, data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendUDP(String ip, int port, byte[] dataToSend) throws IOException {
        InetAddress inetAddress = InetAddress.getByName(ip);
        DatagramSocket socket = new DatagramSocket();
        DatagramPacket datagramPacket = new DatagramPacket(dataToSend, dataToSend.length, inetAddress, port);
        socket.send(datagramPacket);
        socket.close();
    }

    private static void sendTCP(String ip, int port, byte[] dataToSend) throws IOException {
        Socket socket = new Socket(ip, port);
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(dataToSend);
        outputStream.close();
        socket.close();
    }
}
