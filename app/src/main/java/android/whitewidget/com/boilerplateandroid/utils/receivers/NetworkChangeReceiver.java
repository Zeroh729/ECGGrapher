package android.whitewidget.com.boilerplateandroid.utils.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.whitewidget.com.boilerplateandroid.data.events.NetworkEvent;
import android.whitewidget.com.boilerplateandroid.utils.OttoBus;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EReceiver;
import org.androidannotations.annotations.SystemService;

@EReceiver
public class NetworkChangeReceiver extends BroadcastReceiver {

    @SystemService
    ConnectivityManager connectivityManager;

    @Bean
    OttoBus bus;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        NetworkEvent event = new NetworkEvent();
        event.setConnected(isOnline(context));
        bus.post(event);
    }

    private boolean isOnline(Context context) {
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }
}