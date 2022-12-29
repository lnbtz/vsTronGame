package middleware.serverstub;

import com.google.gson.Gson;
import middleware.IMarshaller;
import trongame.applicationStub.callee.ICallee;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

public class WorkerRunnableTCP implements Runnable {
    List<ICallee> calleeList;
    private Socket clientSocket;

    public WorkerRunnableTCP(Socket clientSocket, List<ICallee> calleeList) {
        this.calleeList = calleeList;
        this.clientSocket = clientSocket;
    }


    @Override
    public void run() {
        try {
            InetAddress sourceAddress = clientSocket.getInetAddress();

            // Get the input stream for reading data from the server
            InputStream inputStream = clientSocket.getInputStream();
            byte[] buffer = new byte[1024];
            inputStream.read(buffer);

            String json = IMarshaller.unmarshal(buffer);
            Object[] dataFromJson = getDataFromJson(json);
            ICallee callee = getCallee(dataFromJson);
            // TODO
            callee.call(sourceAddress.hashCode(), (String) dataFromJson[1], dataFromJson);
            System.out.println(json);
            // Close the socket
            clientSocket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
