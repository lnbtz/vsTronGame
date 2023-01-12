package middleware.serverStub2;

import tronGame.applicationStub.callee.ICallee;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WorkerRunnableUDP implements Runnable {

    private final Pattern messagePattern = Pattern.compile("(.*?):(.*?):(.*)");
    private final DatagramPacket packet;
    private final Map<Integer, ICallee> calleeMap;

    public WorkerRunnableUDP(DatagramPacket packet, Map<Integer, ICallee> calleeMap) {
        this.packet = packet;
        this.calleeMap = calleeMap;
    }

    @Override
    public void run() {
        byte[] marshalledData = packet.getData();
        String unmarshalledData = unmarshall(marshalledData);


        Matcher matcher = messagePattern.matcher(unmarshalledData);
        matcher.matches();

        int objectId = Integer.parseInt(matcher.group(1));
        String methodName = matcher.group(2);
        String data = matcher.group(3);

        ICallee callee = calleeMap.get(objectId);
        try {
            callee.call(methodName, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String unmarshall(byte[] marshalledData) {
        String unmarshalledData;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(marshalledData);
            ObjectInputStream ois = new ObjectInputStream(bis);
            unmarshalledData = (String) ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return unmarshalledData;
    }
}
