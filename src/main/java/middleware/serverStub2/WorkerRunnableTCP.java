package middleware.serverStub2;

import tronGame.applicationStub.callee.ICallee;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WorkerRunnableTCP implements Runnable {

    Pattern messagePattern = Pattern.compile("(.*?):(.*?):(.*)");
    private Socket clientSocket;
    Map<Integer, ICallee> calleeMap;

    public WorkerRunnableTCP(Socket clientSocket, Map<Integer, ICallee> calleeMap) {
        this.clientSocket = clientSocket;
        this.calleeMap = calleeMap;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = clientSocket.getInputStream();
            byte[] buffer = new byte[1024];
            inputStream.read(buffer);
            String unmarshalledData = unmarshall(buffer);

            Matcher matcher = messagePattern.matcher(unmarshalledData);
            matcher.matches();

            int objectId = Integer.parseInt(matcher.group(1));
            String methodName = matcher.group(2);
            String data = matcher.group(3);

            ICallee callee = calleeMap.get(objectId);
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
