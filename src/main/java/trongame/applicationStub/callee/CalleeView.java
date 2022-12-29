package trongame.applicationStub.callee;

import com.google.gson.Gson;
import javafx.application.Platform;
import trongame.view.IGameView;
import trongame.view.ISubscriber;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class CalleeView implements ICallee {
    IGameView tronView;
    ISubscriber subscriber;
    Gson gson = new Gson();

    public CalleeView(IGameView tronView, ISubscriber subscriber) {
        this.tronView = tronView;
        this.subscriber = subscriber;
    }

    @Override
    public void call(int sourceId, String methodId, Object[] data) {
        for (Method method : tronView.getClass().getMethods()) {
            if (method.getName().equals(methodId)) {
                invokeMethod(data, method);
            }
        }
    }

    private void invokeMethod(Object[] data, Method method) {
        if (hasParameters(method)) {
            Type parameterType = method.getParameters()[0].getParameterizedType();
            Object parameter = gson.fromJson((String) data[2], parameterType);
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
    public String getName() {
        return "view";
    }
}
