package android.zeroh729.com.ecggrapher.presenters;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.zeroh729.com.ecggrapher.interactors.BluetoothSystem;
import android.zeroh729.com.ecggrapher.interactors.interfaces.DataCallback;
import android.zeroh729.com.ecggrapher.interactors.interfaces.SimpleCallback;
import android.zeroh729.com.ecggrapher.interactors.interfaces.SuccessCallback;
import android.zeroh729.com.ecggrapher.presenters.base.BasePresenter;

import java.util.Set;

public class DeviceListPresenter implements BasePresenter {
    public final static int
            STATE_SEARCH_DONE = 1,
            STATE_SEARCHING = 2,
            STATE_BT_ERROR = 3,
            STATE_BT_ON = 4,
            STATE_BT_TURNING_ON = 5,
            STATE_BT_TURNED_OFF = 6,
            STATE_BT_OFF = 7;

    private int state;
    private DeviceListScreen screen;
    private DeviceListSystem system;

    private BluetoothSystem btSystem;

    public DeviceListPresenter(DeviceListScreen screen) {
        this.screen = screen;
        this.screen.checkPermissionsForBluetooth();
        this.screen.showEmergencyContactView();

        btSystem = new BluetoothSystem();
        btSystem.setup(new SuccessCallback() {
            @Override
            public void onSuccess() {
                if(btSystem.isBTEnabled()){
                    setState(STATE_BT_ON);
                }else{
                    setState(STATE_BT_OFF);
                }
                for (BluetoothDevice device : btSystem.getCurrentlyPairedDevices()){
                    screen.addDeviceToList(device);
                }
            }

            @Override
            public void onFail() {
                setState(STATE_BT_ERROR);
            }
        });
    }

    @Override
    public void setup() {

    }

    public int getState(){
        return state;
    }

    @Override
    public void updateState() {
        switch (state){
            case STATE_BT_ERROR:
                screen.displayBluetoothSetupError();
                break;

            case STATE_BT_ON:
                screen.displaySearchDevicesBtnOn();
                break;

            case STATE_BT_TURNING_ON:
                btSystem.enableBluetooth(screen.getContext());
                screen.displayTurningOn();
                break;

            case STATE_BT_TURNED_OFF:
                screen.displayError("Bluetooth turned off");
                setState(STATE_BT_OFF);
                break;

            case STATE_BT_OFF:
                screen.displayBluetoothBtnOn();
                break;

            case STATE_SEARCHING:
                screen.displaySearchingMessage();
                btSystem.startSearching(btCallback);
                break;

            case STATE_SEARCH_DONE:
                screen.displaySearchDone();
                setState(STATE_BT_ON);
                break;
        }
    }

    private SuccessCallback btCallback = new SuccessCallback() {
        @Override
        public void onSuccess() {
            screen.displaySearchingMessage();
        }

        @Override
        public void onFail() {
            screen.displayError("Failed to connect.");
            setState(DeviceListPresenter.STATE_BT_ON);
        }
    };

    public boolean isBluetoothOn(){
        return btSystem.isBTEnabled();
    }

    @Override
    public void setState(int state) {
        this.state = state;
        updateState();
    }

    public void onClickSearchDevices() {
        setState(STATE_SEARCHING);
    }

    public void onClickEnableBluetooth() {
        setState(STATE_BT_TURNING_ON);
    }

    public void onClickConfirmConnect() {
        btSystem.cancelDiscovery();
        setState(STATE_BT_ON);
    }

    public void onFailedToEnableBluetooth() {
        screen.displayError("Failed to turn on Bluetooth.");
        setState(STATE_BT_OFF);
    }

    public void foundDevice(BluetoothDevice device) {
        device.fetchUuidsWithSdp();
        screen.addDeviceToList(device);
    }

    public void onSelectDevice(BluetoothDevice device) {
        String confirmationMsg = "Do you want to connect to: " + device.getName() + " - " + device.getAddress();
        screen.displayConnectConfirmation(confirmationMsg, new SimpleCallback(){
            @Override
            public void onReturn() {
                onClickConfirmConnect();
                screen.goToMainActivity(device);
            }
        });
    }

    public interface DeviceListScreen {
        Activity getContext();
        void checkPermissionsForBluetooth();
        void showEmergencyContactView();
        void addDeviceToList(BluetoothDevice device);

        void displayBluetoothBtnOn();
        void displaySearchDevicesBtnOn();
        void displaySearchingMessage();
        void displayTurningOn();
        void displaySearchDone();
        void displayConnectConfirmation(String confirmationMsg, SimpleCallback confirmCallback);
        void displayError(String error);

        void goToMainActivity(BluetoothDevice device);
        void finishActivity();

        void displayBluetoothSetupError();
    }

    public interface DeviceListSystem {

    }
}
