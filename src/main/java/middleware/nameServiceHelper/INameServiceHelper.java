package middleware.nameServiceHelper;

import java.net.InetAddress;

public interface INameServiceHelper {
    public int bind(int interfaceType);
    public InetAddress lookup(int objectId);
}
