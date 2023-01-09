package middleware.serverStub;

import tronGame.applicationStub.callee.ICallee;

public interface IServerStub {
    public int register(int interfaceTyp, ICallee callee);
}
