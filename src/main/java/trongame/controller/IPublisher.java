package trongame.controller;

import trongame.view.IGameView;

public interface IPublisher {

    void subscribe(int id, IGameView gameView);
}
