package middleware.nameService;

public interface INameService {
    public int bind(int interfaceType, String ip);
    public String lookup(int objectId);
}
