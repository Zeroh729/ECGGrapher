package android.zeroh729.com.ecggrapher.interactors;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.zeroh729.com.ecggrapher.data.local.Constants;
import android.zeroh729.com.ecggrapher.interactors.interfaces.AbstractBluetoothDataHandler;
import android.zeroh729.com.ecggrapher.interactors.interfaces.SuccessCallback;
import android.zeroh729.com.ecggrapher.ui.base.BaseBluetoothActivity;
import android.zeroh729.com.ecggrapher.utils._;

import org.androidannotations.annotations.EBean;

import java.util.Set;

public class BluetoothSystem {
    private static BluetoothSystem INSTANCE;
    private static BluetoothService bluetoothService;
    private static BluetoothAdapter btadapter;

    public static final int REQCODE_ENABlE_BT = 20221;
    private static AbstractBluetoothDataHandler btDataHandler;
    private BaseBluetoothActivity activity;

    public static BluetoothSystem getInstance(){
        if(INSTANCE == null)
            INSTANCE = new BluetoothSystem();

        return INSTANCE;
    }

    public void setup(BaseBluetoothActivity activity, SuccessCallback callback) {
        this.activity = activity;
        if(bluetoothService != null && bluetoothService.getState() == Constants.STATE_CONNECTED) {
            if(btDataHandler!=null) {
                btDataHandler.setBaseBluetoothActivity(this.activity);
                callback.onSuccess();
            }else{
                callback.onFail();
            }
        }else{
            btadapter = BluetoothAdapter.getDefaultAdapter();
            if (btadapter != null) {
                callback.onSuccess();
            } else {
                callback.onFail();
            }
        }
    }

    public String getDeviceName(){
        if(bluetoothService != null){
            return bluetoothService.myDevice.getName();
        }
        if(_.ISDEBUG){
            return "Simulated Data";
        }
        return "";
    }

    public BluetoothSystem pairToDevice(BluetoothDevice device){
        //TODO ADD DEBUG MODE
        btDataHandler = new BluetoothDataHandler(activity);
        bluetoothService = new BluetoothService(btDataHandler, device);
        connectionStart();
        return this;
    }

    public void connectionStart(){
        if(bluetoothService != null)
            bluetoothService.connect();
    }

    public void connectionStop(){
        if(bluetoothService != null) {
            bluetoothService.stop();
            bluetoothService = null;
        }
    }

    public boolean isBTEnabled(){
        return btadapter.isEnabled();
    }

    public boolean cancelDiscovery(){
        return btadapter.cancelDiscovery();
    }

    public void enableBluetooth(Activity a) {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        a.startActivityForResult(enableBtIntent, REQCODE_ENABlE_BT);
    }

    public void startSearching(SuccessCallback callback) {
        if (btadapter.startDiscovery()) {
            callback.onSuccess();
        } else {
            callback.onFail();
        }
    }


    public Set<BluetoothDevice> getCurrentlyPairedDevices() {
        return btadapter.getBondedDevices();
    }
}
