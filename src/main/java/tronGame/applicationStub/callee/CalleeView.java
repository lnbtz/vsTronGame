package tronGame.applicationStub.callee;

import com.google.gson.Gson;
import javafx.application.Platform;
import tronGame.view.IGameView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class CalleeView implements ICallee, IGameView {
    IGameView tronView;
    Gson gson = new Gson();
    int id;

    public CalleeView(IGameView tronView) {
        this.tronView = tronView;
    }

    @Override
    public void call(String methodId, String data) {
        for (Method method : tronView.getClass().getMethods()) {
            if (method.getName().equals(methodId)) {
                invokeMethod(data, method);
            }
        }
    }

    private void invokeMethod(String data, Method method) {
        if (hasParameters(method)) {
            Type parameterType = method.getParameters()[0].getParameterizedType();
            Object parameter = gson.fromJson(data, parameterType);
            Platform.runLater(() -> {
                try {
                    method.invoke(tronView, parameter);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            Platform.runLater(() -> {
                try {
                    method.invoke(tronView);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }


    private static boolean hasParameters(Method method) {
        return method.getParameterTypes().length != 0;
    }

    @Override
    public void updateTimer(int time) {

    }

    @Override
    public void updatePlayerCount(int playerCount) {

    }

    @Override
    public void updateGameUI(Map<Integer, int[]> playerNumbersAndPositions) {

    }

    @Override
    public void deletePlayer(List<Integer> positions) {

    }

    @Override
    public void showStartScreen() {

    }

    @Override
    public void showEndScreen(String outcome) {

    }

    @Override
    public void showGameScreen() {

    }

    @Override
    public void showLobbyScreen() {

    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}
