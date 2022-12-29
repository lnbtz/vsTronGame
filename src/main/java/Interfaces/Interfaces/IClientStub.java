package Interfaces;

import org.javatuples.Pair;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;

public interface IClientStub {
    public void invoke(int objectId, String methodName, String data, int sendMethod) throws IOException;
}
