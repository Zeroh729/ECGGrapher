package android.zeroh729.com.ecggrapher.interactors;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.zeroh729.com.ecggrapher.data.local.Constants;
import android.zeroh729.com.ecggrapher.ui.main.activities.MainActivity;
import android.zeroh729.com.ecggrapher.utils._;

import java.lang.ref.WeakReference;

public class myHandler extends Handler {
    private final WeakReference<MainActivity> mActivity;

    public myHandler(MainActivity activity) {
        mActivity = new WeakReference<>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        final MainActivity activity = mActivity.get();
        _.log("Received data!");
        switch (msg.what) {
            case Constants.MESSAGE_STATE_CHANGE:
                switch (msg.arg1) {
                    case Constants.STATE_NONE:
                        //SHow button "Go to connect screen"
                        _.log("Disconnected");
                        activity.disconnected();
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
//                            for(String message2: messages){
                                if(!messages[messages.length-1].trim().isEmpty()) {
                                    int data = Integer.parseInt(messages[messages.length-1].trim());
                                    activity.receiveData(data);
                                }
//                            }
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