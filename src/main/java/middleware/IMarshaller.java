package middleware;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public interface IMarshaller {
    static byte[] marshal(String input) {
        return input.getBytes();
    }

    static String unmarshal(byte[] data) {
        int length = 0;
        while (data[length] != 0) {
            length++;
        }
        ByteBuffer buffer = ByteBuffer.wrap(data, 0, length);
        return StandardCharsets.UTF_8.decode(buffer).toString();
    }
}
