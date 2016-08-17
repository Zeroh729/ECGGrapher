package android.whitewidget.com.boilerplateandroid.presenters;

import android.whitewidget.com.boilerplateandroid.interactors.DataListSystem;
import android.whitewidget.com.boilerplateandroid.presenters.base.BasePresenter;

public class DataListPresenter implements BasePresenter {
    private Screen screen;
    private System system;

    public DataListPresenter(Screen screen) {
        this.screen = screen;
        system = new DataListSystem();
    }

    @Override
    public void setup() {

    }

    @Override
    public void updateState() {

    }

    @Override
    public void setState(int state) {

    }

    public interface Screen {

    }

    public interface System {

    }
}
