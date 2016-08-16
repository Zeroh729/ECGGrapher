package android.whitewidget.com.boilerplateandroid.ui.main.activities;

import android.support.v7.widget.RecyclerView;
import android.whitewidget.com.boilerplateandroid.R;
import android.whitewidget.com.boilerplateandroid.data.events.NetworkEvent;
import android.whitewidget.com.boilerplateandroid.ui.base.BaseActivity;
import android.whitewidget.com.boilerplateandroid.ui.main.adapters.BasicRowRecyclerViewAdapter;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;


@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @AfterViews
    public void afterViews(){

    }

    @Subscribe
    public void subscribeToNetworkEvent(NetworkEvent event){
        Toast.makeText(this, "Connected : " + event.isConnected(), Toast.LENGTH_SHORT).show();
    }
}
