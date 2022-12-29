package trongame.applicationStub.callee;

public interface ICallee {
    void call(int sourceId, String methodId, Object[] data);

    String getName();
}
