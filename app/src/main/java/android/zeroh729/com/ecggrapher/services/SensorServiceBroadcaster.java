package android.zeroh729.com.ecggrapher.services;

import android.zeroh729.com.ecggrapher.data.model.ECG;

public interface SensorServiceBroadcaster {
    void startReadingSensor();
    void broadcastEcgData(ECG ecg);
}
