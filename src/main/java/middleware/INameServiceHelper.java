package middleware;

import java.net.InetAddress;

public interface INameServiceHelper {
    public int bind(int interfaceType);
    public InetAddress lookup(int objectId);
}
