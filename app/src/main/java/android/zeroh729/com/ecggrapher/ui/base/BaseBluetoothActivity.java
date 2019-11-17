package android.zeroh729.com.ecggrapher.ui.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public abstract class BaseBluetoothActivity extends BaseActivity {
    public abstract void receiveBtStateUpdate(Intent intent);
    public abstract void addActionToFilter(IntentFilter filter);

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        addActionToFilter(filter);
        registerReceiver(btreceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(btreceiver);
    }

    private final BroadcastReceiver btreceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent == null) return;
            receiveBtStateUpdate(intent);
        }
    };

}
