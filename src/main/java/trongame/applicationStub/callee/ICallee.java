package trongame.applicationStub.callee;

import java.io.IOException;

public interface ICallee {
    void call(String methodName, String data) throws IOException;

    int getId();

    void setId(int getId);
}
