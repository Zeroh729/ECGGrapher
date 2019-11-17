package android.zeroh729.com.ecggrapher.interactors;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.zeroh729.com.ecggrapher.ECGGrapher_;
import android.zeroh729.com.ecggrapher.data.local.Constants;
import android.zeroh729.com.ecggrapher.data.local.SharedPrefHelper;
import android.zeroh729.com.ecggrapher.data.model.ShortECGSummary;
import android.zeroh729.com.ecggrapher.interactors.interfaces.AbstractBluetoothDataHandler;
import android.zeroh729.com.ecggrapher.interactors.interfaces.DataCallback;
import android.zeroh729.com.ecggrapher.presenters.ECGStoragePresenter;
import android.zeroh729.com.ecggrapher.ui.base.BaseBluetoothActivity;
import android.zeroh729.com.ecggrapher.ui.main.activities.MainActivity;
import android.zeroh729.com.ecggrapher.utils._;

import com.androidplot.xy.AdvancedLineAndPointRenderer;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.logging.LogRecord;

import ecganal.Callback.ECGAnalysisCallback;
import ecganal.ECGAnalyzer;
import ecganal.Model.ECGSummary;

import static android.zeroh729.com.ecggrapher.interactors.BluetoothService.MESSAGE_READ;
import static android.zeroh729.com.ecggrapher.interactors.BluetoothService.MESSAGE_STATE_CHANGE;

public class BluetoothDataHandler extends AbstractBluetoothDataHandler {

    @Override
    public void handleMessage(Message msg) {
        _.log("Received data! " + msg.what);
        broadcast(msg);
    }

    @Override
    public void destroy() {

    }

    private void broadcast(Message msg){
        _.log("Broadcasting " + msg.what);
        switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
                Intent intent = new Intent();
                intent.setAction(Constants.ACTION_BTCONN_SERVICE);
                intent.putExtra(Constants.EXTRA_MSG_TYPE, Constants.MSG_STATE_CHANGED);
                intent.putExtra(Constants.EXTRA_BTCONN_STATE, msg.arg1);
                ECGGrapher_.getInstance().sendBroadcast(intent);
                break;
            case MESSAGE_READ:
                try {
                    String message = ((String) msg.obj);
                    if(!message.trim().isEmpty() && !message.contains("!")){
                        if(message.contains("\n")){
                            String[] messages = message.split("\n");
                            if(!messages[messages.length-1].trim().isEmpty()) {
                                int data = Integer.parseInt(messages[messages.length-1].trim());
                                consumeData(data);
                            }
                        }else{
                            int data = Integer.parseInt(message.trim());
                            consumeData(data);
                        }
                    }
                }catch (Exception e) {
                    _.logError(e.getMessage());
                }
                break;
        }
    }
}