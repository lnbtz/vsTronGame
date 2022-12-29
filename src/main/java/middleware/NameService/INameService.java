package middleware.NameService;

public interface INameService {
    public final int port = 1234;

    //interfaceType 0 steht für Controller, 1 für View
    public int bind(int interfaceType, String ip);
    public String lookup(int objectId);
}
