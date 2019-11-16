package android.zeroh729.com.ecggrapher.interactors.interfaces;

import android.os.Handler;
import android.os.Message;
import android.zeroh729.com.ecggrapher.ui.base.BaseBluetoothActivity;
import android.zeroh729.com.ecggrapher.ui.main.activities.MainActivity;

import java.lang.ref.WeakReference;

public abstract class AbstractBluetoothDataHandler extends Handler {
    protected WeakReference<BaseBluetoothActivity> mActivity;

    public AbstractBluetoothDataHandler(BaseBluetoothActivity activity) {
        mActivity = new WeakReference<>(activity);
    }

    public void setBaseBluetoothActivity(BaseBluetoothActivity activity){
        mActivity = new WeakReference<>(activity);
    }

    public abstract void handleMessage(Message msg);
}
