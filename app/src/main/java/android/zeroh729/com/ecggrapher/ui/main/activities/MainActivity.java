package android.zeroh729.com.ecggrapher.ui.main.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.zeroh729.com.ecggrapher.R;
import android.zeroh729.com.ecggrapher.data.local.BTState;
import android.zeroh729.com.ecggrapher.data.model.ECG;
import android.zeroh729.com.ecggrapher.interactors.BTSystem;
import android.zeroh729.com.ecggrapher.interactors.interfaces.DataCallback;
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
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import static android.zeroh729.com.ecggrapher.data.events.BTConstants.KEY_STATE;
import static android.zeroh729.com.ecggrapher.data.local.BTState.BT_OFF;
import static android.zeroh729.com.ecggrapher.data.local.BTState.BT_TURNING_OFF;
import static android.zeroh729.com.ecggrapher.data.local.BTState.BT_TURNING_ON;
import static android.zeroh729.com.ecggrapher.data.local.BTState.CONNECTED;


@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity{
    @ViewById
    GraphView graph_view;
    LineGraphSeries ecgSeries;

    @ViewById
    ImageButton ibtn_bluetooth, ibtn_history;

    @Bean
    OttoBus bus;

    @Bean
    BTSystem btSystem;

    double currentX = 0;
    double latestX = 0;
    int offsetIndex = 1;
    private static final int REQUEST_ENABLE_BT = 1111;
    private static final int REQUEST_CONNECT_DEVICE = 1112;

    private static final int Y_0_INDICATOR = -1000;
    private static final int MAX_DATAPOINTS = 1000;
    private static final double NEW_X_INTERVAL = 0.2d;
    private static final int DRAW_MILLIS = 200;
    private static final int DRAW_PQRS_MILLIS = 10;

    private boolean pqrsFlag = true;

    int counter = 0;

    private ArrayList<double[]> listPoints = new ArrayList();
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(listPoints.get(0)[1] == Y_0_INDICATOR){
                removeLowerXValues();
                double x = listPoints.get(0)[0];
                double y = 0;
                _.log("x: " + x + "\t\t\t\ty:" + y);
                ecgSeries.appendData(new DataPoint(x,y), true, MAX_DATAPOINTS);
                listPoints.remove(0);
                latestX = x;

                currentX += NEW_X_INTERVAL;
                listPoints.add(new double[]{currentX,Y_0_INDICATOR});
            }else if(pqrsFlag){
                pqrsFlag = false;
                counter = 0;

                final Handler handler1 = new Handler();
                final Runnable runnable1 = new Runnable() {
                    @Override
                    public void run() {
                        removeLowerXValues();
                        double x = listPoints.get(0)[0];
                        double y = listPoints.get(0)[1];
                        _.log("x: " + x + "\t\t\t\ty:" + y);
                        ecgSeries.appendData(new DataPoint(x,y), true, MAX_DATAPOINTS);
                        listPoints.remove(0);
                        latestX = x;

                        counter++;
                        offsetIndex--;
                        _.log("--offsetIndex: " + offsetIndex);

                        if(counter < 4) {
                            handler1.postDelayed(this, DRAW_PQRS_MILLIS);
                        }else{
                            pqrsFlag = true;
                        }
                    }
                };
                handler1.post(runnable1);
            }

            _.log("Drew point, size is now: " + listPoints.size());
            handler.postDelayed(this, DRAW_MILLIS);
        }
    };

    private void removeLowerXValues() {
        int index = -1;
        for(int i = 0; i < listPoints.size(); i++) {
            if (listPoints.get(i)[0] < latestX) {
                _.log("comparing: " + listPoints.get(i)[0] + " < " + latestX + " w/ y:" + listPoints.get(i)[1]);
                index = i;
            }else
                break;
        }

        if(index != -1){
            listPoints.subList(0, index+1).clear();
        }
    }

    @AfterViews
    void afterviews(){
        ecgSeries = new LineGraphSeries<>();

        graph_view.addSeries(ecgSeries);
        graph_view.getViewport().setScrollable(true);
        graph_view.getViewport().setScalableY(true);
        graph_view.getViewport().setYAxisBoundsManual(true);
        graph_view.getViewport().setMaxY(4);
        graph_view.getViewport().setMinY(-3);

        for(int i = 0; i < MAX_DATAPOINTS; i++){
            listPoints.add(new double[]{currentX,Y_0_INDICATOR});
            currentX += NEW_X_INTERVAL;
        }

        handler.post(runnable);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        btSystem.subscribe(new DataCallback<Bundle>() {
//            @Override
//            public void onUpdate(Bundle data) {
//                BTState state = BTState.valueOf(data.getString(KEY_STATE));
//
//                switch(state){
//                    case BT_TURNING_OFF:
//                        _.showToast("Turning bluetooth off...");
//                        break;
//                    case BT_TURNING_ON:
//                        _.showToast("Turning bluetooth on...");
//                        break;
//                    case BT_OFF:
//                        _.showToast("Bluetooth is turned off.");
//                        break;
//                    case BT_ON_DOING_NOTHING:
//                        _.showToast("Bluetooth turned on!");
//                        break;
//                    case LISTENING:
//                        _.showToast("Listening...");
//                        break;
//                    case CONNECTING:
//                        _.showToast("Connecting...");
//                        break;
//                    case CONNECTING_FAILED:
//                        _.showToast("Device connection failed.");
//                        break;
//                    case CONNECTED:
//                        _.showToast("Connection success! Device connected");
//                        break;
//                    case DISCONNECTED:
//                        _.showToast("Device disconnected");
//                        break;
//                }
//
//                ibtn_bluetooth.setEnabled(!(state == BT_TURNING_OFF || state == BT_TURNING_ON));
//
//                if(state == CONNECTED){
//                    ibtn_bluetooth.setImageResource(R.drawable.ic_bluetooth_blue_24dp);
//                }else if(state == BT_OFF){
//                    ibtn_bluetooth.setImageResource(R.drawable.ic_bluetooth_disabled_black_24dp);
//                }else{
//                    ibtn_bluetooth.setImageResource(R.drawable.ic_bluetooth_black_24dp);
//                }
//            }
//        });
//    }


    @Override
    protected void onStart() {
        super.onStart();
        MockSensorService_.intent(getApplication()).startReadingSensor().start();
    }

    @Override
    protected void onPause() {
        MockSensorService_.intent(getApplication()).stopReadingSensor().start();
        super.onPause();
    }

    @Click(R.id.ibtn_bluetooth)
    void onClickBluetoothBtn(){
        if(BTSystem.getBTState() == BT_OFF){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }else{
            connectToDevice();
        }
    }

    @OnActivityResult(REQUEST_CONNECT_DEVICE)
    void onConnectToDeviceIntent(int resultCode, Intent data){
        if(resultCode == Activity.RESULT_OK) {

        }
    }

    private void connectToDevice() {
        Intent intent = new Intent(getApplicationContext(), DeviceList.class);
        startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
    }

    @Subscribe
    public void subscribeToECGData(ECG ecg){
        //TODO: Change
        double xp = listPoints.get(0)[0] + ecg.getPdelta();
        double xq = listPoints.get(0)[0] + (ecg.getPdelta()*2d) + ecg.getQdelta();
        double xr = listPoints.get(0)[0] + (ecg.getPdelta()*2d) + (ecg.getQdelta()*2d) + ecg.getRdelta();
        double xs = listPoints.get(0)[0] + (ecg.getPdelta()*2d) + (ecg.getQdelta()*2d) + (ecg.getRdelta()*2d) + ecg.getSdelta();
        double lastx = listPoints.get(0)[0] + (ecg.getPdelta()*2d) + (ecg.getQdelta()*2d) + (ecg.getRdelta()*2d) + (ecg.getSdelta()*2);

        listPoints.add(offsetIndex++, new double[]{xp, ecg.getPv()});
        listPoints.add(offsetIndex++, new double[]{xq, ecg.getQv()});
        listPoints.add(offsetIndex++, new double[]{xr, ecg.getRv()});
        listPoints.add(offsetIndex++, new double[]{xs, ecg.getSv()});
        listPoints.add(offsetIndex, new double[]{lastx, Y_0_INDICATOR});

        _.log("offsetIndex: " + offsetIndex);
    }

}