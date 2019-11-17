package android.zeroh729.com.ecggrapher.interactors;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.zeroh729.com.ecggrapher.ECGGrapher_;
import android.zeroh729.com.ecggrapher.data.local.Constants;
import android.zeroh729.com.ecggrapher.data.model.BluetoothDeviceItem;
import android.zeroh729.com.ecggrapher.interactors.interfaces.AbstractBluetoothDataHandler;
import android.zeroh729.com.ecggrapher.interactors.interfaces.SuccessCallback;
import android.zeroh729.com.ecggrapher.ui.base.BaseBluetoothActivity;
import android.zeroh729.com.ecggrapher.utils._;

import org.androidannotations.annotations.EBean;

import java.util.Set;

import static android.zeroh729.com.ecggrapher.interactors.BluetoothService.STATE_CONNECTED;

public class BluetoothSystem {
    private static BluetoothSystem INSTANCE;
    private static BluetoothAdapter btadapter;
    private BluetoothDeviceItem device;
    Intent bluetoothServiceIntent;
    public static final int REQCODE_ENABlE_BT = 20221;

    public static BluetoothSystem getInstance(){
        if(INSTANCE == null)
            INSTANCE = new BluetoothSystem();

        return INSTANCE;
    }

    public void setup(SuccessCallback callback) {
        btadapter = BluetoothAdapter.getDefaultAdapter();
        if (btadapter != null) {
            callback.onSuccess();
        } else {
            callback.onFail();
        }
    }

    public String getDeviceName(){
        if(device != null){
            return device.getDeviceName();
        }
        return "";
    }

    public void pairToDevice(BluetoothDeviceItem device){
        //TODO ADD DEBUG MODE
        this.device = device;
        bluetoothServiceIntent = new Intent(ECGGrapher_.getInstance(), BluetoothService.class);
        bluetoothServiceIntent.putExtra("btDeviceAddress", device.getDeviceAddress());
        ECGGrapher_.getInstance().startService(bluetoothServiceIntent);
    }

    public void connectionStop(){
        //TODO: how to know if service is running?
//        if(bluetoothServiceIntent != null) {
//            ECGGrapher_.getInstance().stopService(bluetoothServiceIntent);
//            bluetoothServiceIntent = null;
//        }
        Intent intent = new Intent();
        intent.setAction(BluetoothService.ACTION_BT_SERVICE);
        intent.putExtra(BluetoothService.EXTRA_BT_SERVICE,BluetoothService.EXTRA_BT_CHANGE_STATE);
        intent.putExtra(BluetoothService.EXTRA_BT_CHANGE_STATE,BluetoothService.STATE_STOP);
        ECGGrapher_.getInstance().sendBroadcast(intent);
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
