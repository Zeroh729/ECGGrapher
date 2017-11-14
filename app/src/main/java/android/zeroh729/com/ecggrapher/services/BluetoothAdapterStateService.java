package android.zeroh729.com.ecggrapher.services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.zeroh729.com.ecggrapher.interactors.BTSystem;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EService;

@EService
public class BluetoothAdapterStateService extends Service {
    @Bean
    BTSystem btSystem;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mbluetoothStateReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mbluetoothStateReceiver);
    }

    private final BroadcastReceiver mbluetoothStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        btSystem.stateEnabled(false);
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        btSystem.stateSettingToEnable(false);
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        btSystem.stateSettingToEnable(true);
                        break;
                    case BluetoothAdapter.STATE_ON:
                        btSystem.stateEnabled(true);
                        break;
                }
            }
        }
    };
}
