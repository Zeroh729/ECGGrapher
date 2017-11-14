package android.zeroh729.com.ecggrapher.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.zeroh729.com.ecggrapher.data.events.BTDataReceiveEvent;
import android.zeroh729.com.ecggrapher.data.model.ECG;
import android.zeroh729.com.ecggrapher.utils.OttoBus;
import android.zeroh729.com.ecggrapher.utils._;

import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EIntentService;
import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.ServiceAction;

@EIntentService
public class MockSensorService extends IntentService implements SensorServiceBroadcaster{
    @Bean
    OttoBus bus;

    private static int graphUnit = 4000;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            double p, q, r, s, pdelta, qdelta, rdelta, sdelta;
            pdelta = 120d / graphUnit;   //120 milliseconds
            qdelta = 40d / graphUnit;
            rdelta = 50d / graphUnit;
            sdelta = 40d / graphUnit;
            p = (Math.abs(Math.random() * 3) + 1.25d)/4;
            q = -((Math.abs(Math.random() * 3) + 0.25d)/4);
            r = (Math.abs(Math.random() * 3) + 10.7d)/4;
            s = -((Math.abs(Math.random() * 3) + 2.05d)/4);
            ECG ecg = new ECG(p,q,r,s,pdelta,qdelta,rdelta,sdelta);
            broadcastEcgData(ecg);
            handler.postDelayed(this, 850);
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

    @Subscribe
    public void subscribeToBTData(BTDataReceiveEvent event){
        _.log("BTData passed to service w/ data: " + event.getData() + event.getMessage());
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
