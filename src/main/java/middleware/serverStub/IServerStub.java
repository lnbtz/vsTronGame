package middleware.serverStub;

import trongame.applicationStub.callee.ICallee;

public interface IServerStub {
    public int register(int interfaceTyp, ICallee callee);
}
