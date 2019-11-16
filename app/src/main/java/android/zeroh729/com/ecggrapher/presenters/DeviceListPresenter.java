package android.zeroh729.com.ecggrapher.presenters;

import android.bluetooth.BluetoothDevice;
import android.zeroh729.com.ecggrapher.interactors.BluetoothSystem;
import android.zeroh729.com.ecggrapher.interactors.interfaces.SimpleCallback;
import android.zeroh729.com.ecggrapher.interactors.interfaces.SuccessCallback;
import android.zeroh729.com.ecggrapher.presenters.base.BasePresenter;
import android.zeroh729.com.ecggrapher.ui.base.BaseBluetoothActivity;
import android.zeroh729.com.ecggrapher.utils._;

import org.androidannotations.annotations.EBean;

@EBean
public class DeviceListPresenter implements BasePresenter {
    public final static int
            STATE_SEARCH_DONE = 1,
            STATE_SEARCHING = 2,
            STATE_BT_ERROR = 3,
            STATE_BT_ON = 4,
            STATE_BT_CONNECTING = 5,
            STATE_BT_TURNING_ON = 6,
            STATE_BT_TURNED_OFF = 7,
            STATE_BT_OFF = 8,
            STATE_BT_CONNECTED = 9,
            STATE_BT_CONNECT_FAIL = 10;

    private int state;
    private DeviceListScreen screen;
    private DeviceListSystem system;

    BluetoothSystem btSystem;

    public DeviceListPresenter(){
        btSystem = BluetoothSystem.getInstance();
    }

    public void setup(DeviceListScreen screen) {
        this.screen = screen;
        this.screen.checkPermissionsForBluetooth();
        this.screen.showEmergencyContactView();

        btSystem = new BluetoothSystem();
        btSystem.setup(screen.getContext(), new SuccessCallback() {
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
                screen.displayListedDevices(true);
                break;

            case STATE_BT_CONNECTING:
                screen.displayConnectingIndicator("Connecting to " + btSystem.getDeviceName() + "...");
                screen.displaySearchDevicesBtnOn();
                break;

            case STATE_BT_CONNECTED:
                _.log("Device List Presenter : " + btSystem.getDeviceName());
                screen.displayConnectedToDevice(true, "Successfully connected to " + btSystem.getDeviceName());
                screen.goToMainActivity();
                break;

            case STATE_BT_CONNECT_FAIL:
                screen.displayConnectedToDevice(false, "Failed to connect to " + btSystem.getDeviceName() + ". Try again.");
                break;

            case STATE_BT_TURNING_ON:
                btSystem.enableBluetooth(screen.getContext());
                screen.displayTurningOn();
                break;

            case STATE_BT_TURNED_OFF:
                screen.displayError("Bluetooth turned off");
                screen.displayListedDevices(false);
                screen.displayBluetoothBtnOn();
                break;

            case STATE_BT_OFF:
                screen.displayBluetoothBtnOn();
                screen.displayListedDevices(false);
                break;

            case STATE_SEARCHING:
                screen.displaySearchingMessage();
                btSystem.startSearching(btCallback);
                break;

            case STATE_SEARCH_DONE:
                screen.displaySearchDone();
                screen.displaySearchDevicesBtnOn();
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

    public void onClickConfirmConnect(BluetoothDevice device) {
        btSystem.cancelDiscovery();
        btSystem.pairToDevice(device);
        setState(STATE_BT_CONNECTING);
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
                onClickConfirmConnect(device);
            }
        });
    }

    public interface DeviceListScreen {
        BaseBluetoothActivity getContext();
        void checkPermissionsForBluetooth();
        void showEmergencyContactView();
        void addDeviceToList(BluetoothDevice device);

        void displayListedDevices(boolean isVisible);
        void displayBluetoothBtnOn();
        void displaySearchDevicesBtnOn();
        void displaySearchingMessage();
        void displayTurningOn();
        void displaySearchDone();
        void displayConnectConfirmation(String confirmationMsg, SimpleCallback confirmCallback);
        void displayConnectingIndicator(String statusMsg);
        void displayConnectedToDevice(boolean isSuccess, String deviceName);
        void displayBluetoothSetupError();
        void displayError(String error);
        void goToMainActivity();
        void finishActivity();

    }

    public interface DeviceListSystem {

    }
}
