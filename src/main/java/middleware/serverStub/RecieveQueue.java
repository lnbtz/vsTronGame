package middleware.serverStub;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RecieveQueue {
    public BlockingQueue<byte[]> messages;

    public RecieveQueue(){
        this.messages = new LinkedBlockingQueue<>(2000);
    }

    public byte[] dequeue() {
        byte[] msg = null;
        try {
            msg = messages.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return msg;
    }

    public void enqueue(byte[] element) {
        try {
            messages.put(element);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
