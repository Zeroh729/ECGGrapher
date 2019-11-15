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
import android.zeroh729.com.ecggrapher.interactors.interfaces.SimpleCallback;
import android.zeroh729.com.ecggrapher.interactors.interfaces.SuccessCallback;
import android.zeroh729.com.ecggrapher.ui.main.activities.MainActivity;
import android.zeroh729.com.ecggrapher.ui.main.adapters.BluetoothDevicesAdapter;
import android.zeroh729.com.ecggrapher.utils._;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.ItemClick;

@EBean
public class BluetoothSystem {
    private BluetoothService bluetoothService;
    public BluetoothAdapter btadapter;
    public static final int REQCODE_ENABlE_BT = 20221;

    public void setup(Context context, final SimpleCallback failcallback) {
        btadapter = BluetoothAdapter.getDefaultAdapter();

        if (btadapter == null) {
            _.logError("Device has no bluetooth");
            new AlertDialog.Builder(context)
                    .setCancelable(false)
                    .setTitle("No Bluetooth")
                    .setMessage("Your device has no bluetooth")
                    .setPositiveButton("Close app", new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialog, int which) {
                            failcallback.onReturn();
                        }
                    }).show();
        }
    }

    public String getDeviceName(){
        if(bluetoothService != null){
            return bluetoothService.myDevice.getName();
        }
        return "";
    }

    public BluetoothSystem pairToDevice(AbstractBluetoothDataHandler handler, BluetoothDevice device){
        bluetoothService = new BluetoothService(handler, device);
        return this;
    }

    public void start(){
        if(bluetoothService != null)
            bluetoothService.connect();
    }

    public void stop(){
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


}
