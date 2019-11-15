package android.zeroh729.com.ecggrapher.presenters;

import android.zeroh729.com.ecggrapher.presenters.base.BasePresenter;

public class DeviceListPresenter implements BasePresenter {
    private DeviceListScreen screen;
    private DeviceListSystem system;

    public DeviceListPresenter(DeviceListScreen screen) {
        this.screen = screen;
//        system = new DeviceListSystem();
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

    public interface DeviceListScreen {

    }

    public interface DeviceListSystem {

    }
}
