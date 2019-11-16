package android.zeroh729.com.ecggrapher.interactors;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.zeroh729.com.ecggrapher.ECGGrapher;
import android.zeroh729.com.ecggrapher.ECGGrapher_;
import android.zeroh729.com.ecggrapher.interactors.interfaces.AbstractBluetoothDataHandler;
import android.zeroh729.com.ecggrapher.interactors.interfaces.DataCallback;
import android.zeroh729.com.ecggrapher.interactors.interfaces.SimpleCallback;
import android.zeroh729.com.ecggrapher.interactors.interfaces.SuccessCallback;
import android.zeroh729.com.ecggrapher.ui.main.activities.MainActivity;
import android.zeroh729.com.ecggrapher.ui.main.adapters.BluetoothDevicesAdapter;
import android.zeroh729.com.ecggrapher.utils._;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.ItemClick;

import java.util.Set;

@EBean
public class BluetoothSystem {
    private BluetoothService bluetoothService;
    public BluetoothAdapter btadapter;
    public static final int REQCODE_ENABlE_BT = 20221;

    public void setup(SuccessCallback callback) {
        btadapter = BluetoothAdapter.getDefaultAdapter();

        if (btadapter != null) {
            callback.onSuccess();
        }else{
            callback.onFail();
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

    public BluetoothSystem pairToDevice(AbstractBluetoothDataHandler handler, BluetoothDevice device){
        bluetoothService = new BluetoothService(handler, device);
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
