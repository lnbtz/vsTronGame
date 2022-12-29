package Interfaces;

import org.javatuples.Pair;

import java.net.InetAddress;

public interface IServerStub {
    public int register(int interfaceTyp, ICallee callee);
}
