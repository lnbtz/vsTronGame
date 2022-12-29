package middleware.ClientStub;

public interface IClientStub {
    void invoke(int objectId, String methodName, String data, int sendMethod);
}
