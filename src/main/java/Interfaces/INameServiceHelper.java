package Interfaces;

import java.net.InetAddress;

public interface INameServiceHelper {
    public final int port = 1234;

    //interfaceType 0 steht für Controller, 1 für View
    public int bind(int interfaceType);
    public InetAddress lookup(int objectId);
}
