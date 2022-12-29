package Middleware;

import Interfaces.INameService;
import Interfaces.INameServiceHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;

public class NameServiceHelper implements INameServiceHelper {
    private final HashMap<Integer, InetAddress> cache;
    private final int nameServerPort;
    private final String nameServerAdress;

    public NameServiceHelper(int nameServerPort, String nameServerAdress){
        this.cache = new HashMap<>();
        this.nameServerPort = nameServerPort;
        this.nameServerAdress = nameServerAdress;
    }
    @Override
    public int bind(int interfaceType) {
        int objectId = 0;
        try {
            Socket clientSocket = new Socket(nameServerAdress, nameServerPort);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            out.println("POST:" + interfaceType);

            objectId = Integer.parseInt(in.readLine());

            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        return objectId;
    }

    //returns null if host not registered
    @Override
    public InetAddress lookup(int objectId) {
        //cache checken
        InetAddress hostAdress = cache.get(objectId);
        if(hostAdress != null) return hostAdress;

        try {
            Socket clientSocket = new Socket(nameServerAdress, nameServerPort);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            out.println("GET:" + objectId);
            String nsLookup = in.readLine();

            //ergebnis cachen, prüfen ob ergebniss leer
            if(!nsLookup.equals("null")){
                hostAdress = InetAddress.getByName(nsLookup);
                cache.put(objectId, hostAdress);
            }

            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        //returns null if host not registered
        return hostAdress;
    }
}
