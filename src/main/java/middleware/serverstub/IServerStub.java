package middleware.serverstub;

import trongame.applicationStub.callee.ICallee;

public interface IServerStub {
    void register(ICallee callee, String interfaceName);
}
