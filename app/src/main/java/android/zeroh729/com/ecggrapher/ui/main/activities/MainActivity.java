package android.zeroh729.com.ecggrapher.ui.main.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.zeroh729.com.ecggrapher.R;
import android.zeroh729.com.ecggrapher.data.model.ECG;
import android.zeroh729.com.ecggrapher.services.MockSensorService_;
import android.zeroh729.com.ecggrapher.ui.base.BaseActivity;
import android.zeroh729.com.ecggrapher.utils.OttoBus;
import android.zeroh729.com.ecggrapher.utils._;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;


@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity{
    @ViewById
    GraphView graph_view;
    LineGraphSeries ecgSeries;

    @Bean
    OttoBus bus;

    double index = 0;
    private static final int REQUEST_ENABLE_BT = 1111;

    @AfterViews
    void afterviews(){
        ecgSeries = new LineGraphSeries<>();

        graph_view.addSeries(ecgSeries);
        graph_view.getViewport().setScrollable(true);
        graph_view.getViewport().setXAxisBoundsManual(true);
        graph_view.getViewport().setMaxY(3);
        graph_view.getViewport().setMinY(-3);

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            _.showToast("Get a new phone that supports bluetooth.");
            finish();
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }else{
            //start
            startReceiving();
        }
    }

    @OnActivityResult(REQUEST_ENABLE_BT)
    void onBluetoothIntent(int resultCode, Intent intent){
        if(resultCode == RESULT_OK){
            startReceiving();
        }else{
            _.showToast("Try again! Turn on Bluetooth.");
        }
    }

    private void startReceiving() {
        MockSensorService_.intent(getApplication()).startReadingSensor().start();
    }

    @Subscribe
    public void subscribeToECGData(ECG ecg){
        _.log("read: " + ecg.getSignal());
        ecgSeries.appendData(new DataPoint(index, ecg.getSignal()), true, 1000);
        index += 0.01d;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        MockSensorService_.intent(getApplication()).stopReadingSensor().start();
        super.onPause();
    }

}