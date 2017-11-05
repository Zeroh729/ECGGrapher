package android.zeroh729.com.ecggrapher.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.zeroh729.com.ecggrapher.data.model.ECG;
import android.zeroh729.com.ecggrapher.utils.OttoBus;
import android.zeroh729.com.ecggrapher.utils._;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EIntentService;
import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.ServiceAction;

@EIntentService
public class MockSensorService extends IntentService implements SensorServiceBroadcaster{
    @Bean
    OttoBus bus;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            ECG ecg = new ECG();
            if(Math.random() > 0.5) {
                ecg.setSignal(Math.random());
            }else{
                ecg.setSignal(-Math.random());
            }
            broadcastEcgData(ecg);
            handler.postDelayed(this, 100);
        }
    };

    public MockSensorService() {
        super("MockSensorService");
    }

    @Override
    public void broadcastEcgData(ECG ecg) {
        _.log("Sending ecg");
        bus.post(ecg);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }


    @ServiceAction
    public void startReadingSensor() {
        handler.post(runnable);
    }

    @ServiceAction
    public void stopReadingSensor() {
        handler.removeCallbacks(runnable);
    }
}
