package android.zeroh729.com.ecggrapher.ui.main.activities;


import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.zeroh729.com.ecggrapher.ECGGrapher_;
import android.zeroh729.com.ecggrapher.R;
import android.zeroh729.com.ecggrapher.data.local.Constants;
import android.zeroh729.com.ecggrapher.data.model.ECGSeries;
import android.zeroh729.com.ecggrapher.interactors.BluetoothDataHandler;
import android.zeroh729.com.ecggrapher.interactors.BluetoothService;
import android.zeroh729.com.ecggrapher.interactors.BluetoothSystem;
import android.zeroh729.com.ecggrapher.interactors.MockBluetoothDataHandler;
import android.zeroh729.com.ecggrapher.interactors.interfaces.SimpleCallback;
import android.zeroh729.com.ecggrapher.interactors.interfaces.SuccessCallback;
import android.zeroh729.com.ecggrapher.presenters.ECGStoragePresenter;
import android.zeroh729.com.ecggrapher.ui.base.BaseActivity;
import android.zeroh729.com.ecggrapher.ui.main.adapters.BluetoothDevicesAdapter;
import android.zeroh729.com.ecggrapher.ui.main.views.MyFadeFormatter;
import android.zeroh729.com.ecggrapher.utils._;

import com.androidplot.util.Redrawer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.XYPlot;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    @ViewById
    RelativeLayout parent_view;

    @ViewById
    XYPlot plot;

    @ViewById
    ImageButton ib_bt;

    @ViewById
    TextView tv_status;

    @ViewById
    ListView lv_devices;

    @Bean
    BluetoothSystem btSystem;

    @Bean
    BluetoothDevicesAdapter adapter;

    private ECGSeries series;
    private Redrawer redrawer;
    private BluetoothDataHandler handler;
    private BluetoothService bluetoothService;
    private ECGStoragePresenter ecgStoragePresenter;

    @AfterViews
    void afterviews(){
        checkPermissionsForBluetooth();

        series = new ECGSeries(plot);
        ecgStoragePresenter = new ECGStoragePresenter();
        ecgStoragePresenter.setup();
        plot.addSeries(series, new MyFadeFormatter(Constants.COUNT_X - 100));
        plot.setRangeBoundaries(0, Constants.COUNT_Y, BoundaryMode.FIXED);
        plot.setDomainBoundaries(0, Constants.COUNT_X, BoundaryMode.FIXED);
        btSystem.setup(this, new SimpleCallback() {
            @Override
            public void onReturn() {
                finish();
            }
        });

        redrawer = new Redrawer(plot, 60, true);
        lv_devices.setAdapter(adapter);

        if(_.ISDEBUG){
            lv_devices.setVisibility(View.GONE);
            ib_bt.setVisibility(View.GONE);
            tv_status.setVisibility(View.GONE);
//            handler = new MockBluetoothDataHandler(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        _.log("Registering receiver");
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(btreceiver, filter);

        if(bluetoothService != null){
            bluetoothService.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        _.log("Receiver unregistered");
        unregisterReceiver(btreceiver);
        if(redrawer != null)
        redrawer.finish();

        if (bluetoothService != null) {
            bluetoothService.stop();
        }
    }

    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver btreceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                device.fetchUuidsWithSdp();

                if (adapter.getPosition(device) == -1) {
                    // -1 is returned when the item is not in the adapter
                    adapter.add(device);
                    adapter.notifyDataSetChanged();
                }

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                tv_status.setText("None");
            } else if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Snackbar.make(parent_view, "Bluetooth turned off", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Turn on", new View.OnClickListener() {
                                    @Override public void onClick(View v) {
                                        btSystem.enableBluetooth(MainActivity.this);
                                    }
                                }).show();
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        tv_status.setText("Turning On...");
                        break;
                    case BluetoothAdapter.STATE_ON:
//                        reconnect();
                }
            }
        }
    };


    @ItemClick
    void lv_devicesItemClicked(final BluetoothDevice device) {
        _.log("Tapped on Item "+ device.getName() + " " + device.getAddress());
//        final BluetoothDevice device = bluetoothDevicesAdapter.getItem(position);

        new AlertDialog.Builder(MainActivity.this)
                .setCancelable(false)
                .setTitle("Connect")
                .setMessage("Do you want to connect to: " + device.getName() + " - " + device.getAddress())
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        btSystem.cancelDiscovery();
                        handler = new BluetoothDataHandler(MainActivity.this);
                        bluetoothService = new BluetoothService(handler, device);
                        bluetoothService.connect();
                        lv_devices.setVisibility(View.GONE);
                        ib_bt.setVisibility(View.GONE);
                        tv_status.setVisibility(View.GONE);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        tv_status.setText("Cancelled connection");
                        _.log("Cancelled ");
                    }
                }).show();
    }

    @OnActivityResult(BluetoothSystem.REQCODE_ENABlE_BT)
    void onBluetoothResult(int resultCode){
        if (resultCode == RESULT_OK) {
            btSystem.startSearching(btCallback);
        } else {
            Snackbar.make(parent_view, "Failed to enable bluetooth", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Try Again", new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            tv_status.setText("Enabling bluetooth");
                            btSystem.enableBluetooth(MainActivity.this);
                        }
                    }).show();
        }
    }

    private SuccessCallback btCallback = new SuccessCallback() {
        @Override
        public void onSuccess() {
            tv_status.setText("Loading...");
        }

        @Override
        public void onFail() {
            tv_status.setText("Error");
            Snackbar.make(parent_view, "Failed to start searching", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Try Again", new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            btSystem.startSearching(btCallback);
                        }
                    }).show();
        }
    };

    @Click(R.id.ib_bt)
    void onClickBluetooth(){
        if (btSystem.isBTEnabled()) {
            btSystem.startSearching(btCallback);
        } else {
            tv_status.setText("Enabling bluetooth");
            btSystem.enableBluetooth(this);
        }
    }

    public void receiveData(int data) {
//        _.log("Plotting :  " + data);
        if(data > 100) {
            double ddata = data/1024.00 * 5;
            series.addData(ddata);
            ecgStoragePresenter.addDatapoint(ddata);
        }
    }

    public void disconnected() {
        bluetoothService.stop();
//        lv_devices.setVisibility(View.VISIBLE);
//        ib_bt.setVisibility(View.VISIBLE);
//        tv_status.setVisibility(View.VISIBLE);
    }

    final int MY_PERMISSIONS_REQUEST = 20001;

    private void checkPermissionsForBluetooth() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE};
            ArrayList<String> permsToRequest = new ArrayList<>();


            for(String p : permissions){
                if(checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED){
                    permsToRequest.add(p);
                }
            }

                //            // Should we show an explanation?
                //            if (shouldShowRequestPermissionRationale(
                //                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //                // Explain to the user why we need to read the contacts
                //            }

            if(permsToRequest.size() > 0) {
                String[] permsArr = new String[permsToRequest.size()];
                requestPermissions(permsToRequest.toArray(permsArr),
                        MY_PERMISSIONS_REQUEST);
            }
                // MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE is an
                // app-defined int constant that should be quite unique

                return;
        }
    }
}