package middleware.clientStub;

import middleware.nameServiceHelper.INameServiceHelper;
import org.javatuples.Pair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;

public class ClientStubImpl implements IClientStub {
    private INameServiceHelper nameServiceHelper;
    private SendQueue sendQueue;

    public ClientStubImpl(INameServiceHelper nameServiceHelper, SendQueue sendQueue) {
        this.nameServiceHelper = nameServiceHelper;
        this.sendQueue = sendQueue;
    }

    @Override
    public void invoke(int objectId, String methodName, String data, int sendMethod) {
        //lookup
        InetAddress hostAddress = nameServiceHelper.lookup(objectId);
        //check if host registered in Name Service
        if (hostAddress == null) {
            try{
                throw new HostNotRegisteredException("host not registered");
            } catch (HostNotRegisteredException e){
                e.printStackTrace();
            }
            return;
        } else {
            // message = <objectID>:<methodName>:data
            String message = objectId + ":" + methodName + ":" + data;
            //marshall
            byte[] marshalledData = marshall(message);
            //send
            send(hostAddress, marshalledData, sendMethod);
        }
    }

    private byte[] marshall(String data) {
        byte[] marshalled;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(data);
            marshalled = bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return marshalled;
    }


    // sendMethod=0 -> udp, 1 -> tcp
    private void send(InetAddress ip, byte[] data, int sendMethod) {
        if (sendMethod == 0) {
            sendQueue.udpEnqueue(new Pair<>(ip, data));
        } else {
            sendQueue.tcpEnqueue(new Pair<>(ip, data));
        }
    }

    class HostNotRegisteredException extends Exception
    {
        public HostNotRegisteredException(String message)
        {
            super(message);
        }
    }
}
