package android.whitewidget.com.boilerplateandroid.views.main.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.whitewidget.com.boilerplateandroid.R;
import android.whitewidget.com.boilerplateandroid.data.events.NetworkEvent;
import android.whitewidget.com.boilerplateandroid.utils.OttoBus;
import android.whitewidget.com.boilerplateandroid.views.base.BaseActivity;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;


@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {



    @Subscribe
    public void subscribeToNetworkEvent(NetworkEvent event){
        Toast.makeText(this, "Connected : " + event.isConnected(), Toast.LENGTH_SHORT).show();
    }
}
