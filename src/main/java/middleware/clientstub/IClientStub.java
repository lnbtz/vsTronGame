package middleware.clientstub;

import java.io.IOException;

public interface IClientStub {
    void invoke(int objectId, String data, boolean TCP);

}
