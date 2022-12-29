package trongame.view.screens;

import javafx.scene.layout.Pane;
import trongame.controller.IGameController;
import trongame.controller.TronController;

public class EndScreen extends Pane {

    IGameController tronController;

    public EndScreen(IGameController tronController) {
        this.tronController = tronController;
    }

    public void renderOutcome(String outcome){
        if (outcome.equals("draw")) System.out.println("game was draw");
        else System.out.println("the winner is player " + outcome + "!");
    }
}
