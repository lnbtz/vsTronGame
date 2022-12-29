package middleware.serverstub;

import com.google.gson.Gson;
import middleware.IMarshaller;
import trongame.applicationStub.callee.ICallee;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.List;

public class WorkerRunnableUDP implements Runnable {


    List<ICallee> calleeList;
    DatagramPacket datagramPacket;

    public WorkerRunnableUDP(DatagramPacket datagramPacket, List<ICallee> calleeList) {
        this.calleeList = calleeList;
        this.datagramPacket = datagramPacket;
    }

    @Override
    public void run() {
        byte[] data = datagramPacket.getData();


        InetAddress sourceAddress = datagramPacket.getAddress();


        String json = IMarshaller.unmarshal(data);
        Object[] dataFromJson = getDataFromJson(json);
        ICallee callee = getCallee(dataFromJson);
        callee.call(sourceAddress.hashCode(), (String) dataFromJson[1], dataFromJson);
        System.out.println(json);
    }

    private Object[] getDataFromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Object[].class);
    }

    private ICallee getCallee(Object[] objectArray) {
        switch ((String) objectArray[0]) {
            case "controller":
            case "publisher":
                return calleeList.stream().filter(iCallee -> iCallee.getName().equals("controller")).findFirst().orElseGet(null);
            case "subscriber":
            case "view":
                return calleeList.stream().filter(iCallee -> iCallee.getName().equals("view")).findFirst().orElseGet(null);
        }
        return null;
    }
}
