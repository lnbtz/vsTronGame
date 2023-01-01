package middleware.clientStub;

import org.javatuples.Pair;

import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SendQueue {
    private BlockingQueue<Pair<InetAddress, byte[]>> tcpMessages;
    private BlockingQueue<Pair<InetAddress, byte[]>> udpMessages;

    public SendQueue(){
        this.tcpMessages = new LinkedBlockingQueue<>(20);
        this.udpMessages = new LinkedBlockingQueue<>(200);
    }

    public Pair<InetAddress, byte[]> dequeueTcp() {
        Pair<InetAddress, byte[]> msg = null;
        try {
            msg = tcpMessages.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return msg;
    }

    public Pair<InetAddress, byte[]> dequeueUdp() {
        Pair<InetAddress, byte[]> msg = null;
        try {
            msg = udpMessages.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return msg;
    }

    public void tcpEnqueue(Pair<InetAddress, byte[]> element) {
        try {
            tcpMessages.put(element);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void udpEnqueue(Pair<InetAddress, byte[]> element) {
        try {
            udpMessages.put(element);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
