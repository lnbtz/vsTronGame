package tronGame.controller;

import tronGame.view.IGameView;

public interface IPublisher {

    void subscribe(IGameView gameView);
}
