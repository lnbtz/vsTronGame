package Interfaces;

import java.io.IOException;

public interface ICallee {
    public void call(String methodName, String data) throws IOException;

    interface IClientStub {
    }
}
