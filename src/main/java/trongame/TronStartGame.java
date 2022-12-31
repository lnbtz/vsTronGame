package trongame;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class TronStartGame extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        TronGame game = new TronGame();
        game.standAloneWithMiddleware(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}