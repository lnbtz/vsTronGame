package middleware.nameService;

import config.Config;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class NameServiceImpl implements INameService,Runnable {
    Map<Integer, String> hosts;
    int serverPort;
    int idCounter;

    public NameServiceImpl(){
        this.serverPort = Config.TCP_PORT_NAME_SERVICE;
        this.hosts = new HashMap<>();
        this.idCounter = 1;
    }


    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(this.serverPort);

            BufferedReader in;
            PrintWriter out;

            while (true) {
                Socket clientSocket;
                clientSocket = serverSocket.accept();

                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                String request = in.readLine();
                String[] parameters = request.split(":");

                switch (parameters[0]){
                    case "POST":
                        //Exaple request POST:<interfaceType>
                        String ip = clientSocket.getInetAddress().toString().replace("/","");
                        int givenId = bind(Integer.parseInt(parameters[1]), ip);
                        out.println(givenId);
                        break;
                    case "GET":
                        //Example request GET:<id>
                        String hostAdress = lookup(Integer.parseInt(parameters[1]));
                        //ToDo was passiert wenn host noch nicht registriert
                        // -> returnt null, String ist "null"
                        out.println(hostAdress);
                        break;
                }
                out.close();
                in.close();
                clientSocket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    @Override
    public int bind(int interfaceType, String ip) {
        int giveId;
        if(interfaceType==0){
            hosts.put(0, ip);
            giveId = 0;
        } else {
            hosts.put(idCounter, ip);
            giveId = idCounter;
            idCounter ++;
        }
        return giveId;
    }

    @Override
    public String lookup(int objectId) {
        return hosts.get(objectId);
    }

}
