package middleware.ServerStub;

import trongame.applicationStub.callee.ICallee;
import Interfaces.INameServiceHelper;
import Interfaces.IServerStub;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerStubImpl extends Thread implements IServerStub {
    private final Pattern messagePattern;
    Map<Integer, ICallee> calleeMap;
    RecieveQueue recieveQueue;
    INameServiceHelper nameService;


    public ServerStubImpl(RecieveQueue recieveQueue, INameServiceHelper nameService){
        this.messagePattern = Pattern.compile("(.*?):(.*?):(.*)");
        this.calleeMap = new HashMap<>();
        this.recieveQueue = recieveQueue;
        this.nameService = nameService;
    }
    @Override
    public int register(int interfaceType, ICallee callee) {
        int objedctId = nameService.bind(interfaceType);
        calleeMap.put(objedctId, callee);
        return objedctId;
    }

    private String unmarshall(byte[] marshalledData){
        String unmarshalledData;
        try{
            ByteArrayInputStream bis = new ByteArrayInputStream(marshalledData);
            ObjectInputStream ois = new ObjectInputStream(bis);
            unmarshalledData = (String) ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return unmarshalledData;
    }

    public void run(){
        try{
            recieve();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void recieve() throws IOException {
        while (true){
            byte[] marshalledData = recieveQueue.dequeue();
            String unmarshalledData = unmarshall(marshalledData);

            Matcher matcher = messagePattern.matcher(unmarshalledData);
            matcher.matches();

            int objectId = Integer.parseInt(matcher.group(1));
            String methodName = matcher.group(2);
            String data = matcher.group(3);

            ICallee callee = calleeMap.get(objectId);
            callee.call(methodName, data);
        }
    }

}
