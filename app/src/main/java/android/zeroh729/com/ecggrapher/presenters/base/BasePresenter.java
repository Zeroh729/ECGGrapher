package android.zeroh729.com.ecggrapher.presenters.base;


public interface BasePresenter {
    void setup();
    void updateState();
    void setState(int state);

    interface SystemInterface{

    }

    interface ScreenInterface{

    }
}
