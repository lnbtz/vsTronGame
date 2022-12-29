package middleware;

import config.Config;
import util.IReceiver;
import util.ISender;

import java.util.HashMap;
import java.util.Map;


public class NameServerHelper implements ISender, IReceiver {

    Map<Integer, String> objectIdIpAddressCache = new HashMap<>();

    public NameServerHelper() {
        objectIdIpAddressCache.put(2, "192.168.2.104");
    }

    public int bind(String interfaceName) {
        // TODO do the bind at the actual name server
        if (interfaceName.equals("controller")) {
            objectIdIpAddressCache.put(0, Config.MY_IP_ADDRESS);
            return 0;
        } else {
            objectIdIpAddressCache.put(1, Config.MY_IP_ADDRESS);
            return 1;
        }
    }


    public String lookup(int objectId) {
        if (objectIdIpAddressCache.containsKey(objectId)) return objectIdIpAddressCache.get(objectId);
            // TODO do actual lookup at name server and save it local map
            // TODO maybe do heartbeats to check if map is still accurate
            // return actual lookup
        else return "lookup at server and add to cache";
    }

    @Override
    public void receive() {

    }

    @Override
    public void send(String ip, int port, byte[] data, boolean TCP) {
        // TODO basic sending logic with hardcoded nameserver IP
    }
}
