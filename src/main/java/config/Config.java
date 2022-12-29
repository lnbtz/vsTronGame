package config;

public class Config {
    public static final String IP_ADDRESS_NAME_SERVICE = "192.168.2.133";
    public static int VIEW_ID = 1;
    public static int CONTROLLER_ID = 0;
    public static final int TCP_PORT = 60000;
    public static final int TCP_PORT_NAME_SERVICE = 60001;
    public static final int UDP_PORT = 61000;
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;
    public static final int COLUMNS = 80;
    public static final int ROWS = 80;

    public static final int COUNTDOWN_LENGTH = 20;

    public static final int RED = 1;
    public static final int BLUE = 2;
    public static final int GREEN = 3;
    public static final int YELLOW = 4;
    public static final int BLACK = 5;
    public static final int PURPLE = 6;
    public static final int DELETE = 0;
    public static final int SQUARE_WIDTH = WIDTH / COLUMNS;
    public static final int SQUARE_HEIGHT = HEIGHT / ROWS;

    public static final int SEND_TCP = 1;
    public static final int SEND_UDP = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int UP = 3;
    public static final int DOWN = 4;
    public static final int GO_TO_LOBBY = 5;
    public static final int GO_TO_GAME = 6;
    public static final int GO_TO_END = 7;
    public static final int GO_TO_START_SCREEN = 8;
    public static final int DELAY = 150;
    public static final int NUMBER_OF_PLAYERS = 2;
}
