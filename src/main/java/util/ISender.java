package util;

import java.io.IOException;

public interface ISender {
    void send(String ip, int port, byte[] data, boolean TCP) throws IOException;
}
