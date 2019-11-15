package android.zeroh729.com.ecggrapher.interactors.interfaces;

import android.os.Handler;
import android.os.Message;
import android.zeroh729.com.ecggrapher.ui.main.activities.MainActivity;

import java.lang.ref.WeakReference;

public abstract class AbstractBluetoothDataHandler extends Handler {
    protected final WeakReference<MainActivity> mActivity;

    public AbstractBluetoothDataHandler(MainActivity activity) {
        mActivity = new WeakReference<>(activity);
    }

    public abstract void handleMessage(Message msg);
}
