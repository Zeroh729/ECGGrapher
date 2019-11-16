package android.zeroh729.com.ecggrapher.interactors;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.zeroh729.com.ecggrapher.data.local.Constants;
import android.zeroh729.com.ecggrapher.interactors.interfaces.AbstractBluetoothDataHandler;
import android.zeroh729.com.ecggrapher.ui.base.BaseBluetoothActivity;
import android.zeroh729.com.ecggrapher.ui.main.activities.MainActivity;
import android.zeroh729.com.ecggrapher.utils._;

import com.androidplot.xy.AdvancedLineAndPointRenderer;

import java.lang.ref.WeakReference;
import java.util.logging.LogRecord;

public class BluetoothDataHandler extends AbstractBluetoothDataHandler {
    public BluetoothDataHandler(BaseBluetoothActivity activity) {
        super(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        final BaseBluetoothActivity activity = mActivity.get();
        _.log("Received data! " + msg.what);
        switch (msg.what) {
            case Constants.MESSAGE_SNACKBAR:
                activity.displayDisconnectedView();
                break;
            case Constants.MESSAGE_STATE_CHANGE:
                switch (msg.arg1) {
                    case Constants.STATE_NONE:
                    case Constants.STATE_ERROR:
                    case Constants.STATE_CONNECTION_LOST:
                        _.log("MESSAGE_STATE_CHANGE : err - " + msg.arg1);
                        activity.disconnected();
                        break;
                    case Constants.STATE_CONNECTED:
                        _.log("MESSAGE_STATE_CHANGE : Connected");
                        activity.connected();
                        break;
                }
                break;
            case Constants.MESSAGE_READ:
                //QQ - Graph the message
                try {
                    String message = ((String) msg.obj);
                    if(!message.trim().isEmpty() && !message.contains("!")){
                        if(message.contains("\n")){
                            String[] messages = message.split("\n");
                            if(!messages[messages.length-1].trim().isEmpty()) {
                                int data = Integer.parseInt(messages[messages.length-1].trim());
                                activity.receiveData(data);
                            }
                        }else{
                            int data = Integer.parseInt(message.trim());
                            activity.receiveData(data);
                        }
                    }
                }catch (Exception e) {
                    _.logError(e.getMessage());
                }
                break;
        }
    }

}