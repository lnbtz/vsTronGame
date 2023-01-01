package trongame.view.screens;

import config.Config;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import trongame.controller.IGameController;

public class EndScreen extends Pane {

    IGameController tronController;
    Label winnerLabel = new Label();
    public EndScreen(IGameController tronController) {
        this.tronController = tronController;
    }

    public void renderOutcome(String outcome){
        winnerLabel.setFont(new Font(50));
        winnerLabel.setTextFill(Color.GREEN);
        winnerLabel.setAlignment(Pos.CENTER);
        if (outcome.equals("draw")) {
            winnerLabel.setText("game was draw");
        } else {
            winnerLabel.setText("the winner is player " + outcome + "!");
        }
        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.setLayoutY(30);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPrefWidth(Config.WIDTH);
        vBox.getChildren().add(winnerLabel);
        getChildren().add(vBox);
    }

    public void resetScreen(){
        winnerLabel.setText("");
    }
}
