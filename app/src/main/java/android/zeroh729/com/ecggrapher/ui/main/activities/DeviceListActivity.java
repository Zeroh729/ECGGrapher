package android.zeroh729.com.ecggrapher.ui.main.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.zeroh729.com.ecggrapher.R;
import android.zeroh729.com.ecggrapher.data.local.Constants;
import android.zeroh729.com.ecggrapher.data.local.PermissionsHelper;
import android.zeroh729.com.ecggrapher.data.local.SharedPrefHelper;
import android.zeroh729.com.ecggrapher.interactors.BluetoothSystem;
import android.zeroh729.com.ecggrapher.interactors.interfaces.SimpleCallback;
import android.zeroh729.com.ecggrapher.presenters.DeviceListPresenter;
import android.zeroh729.com.ecggrapher.ui.main.adapters.BluetoothDevicesAdapter;
import android.zeroh729.com.ecggrapher.ui.main.fragments.EmergencyContactFragment;
import android.zeroh729.com.ecggrapher.utils._;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_devicelist)
public class DeviceListActivity extends AppCompatActivity implements DeviceListPresenter.DeviceListScreen {
    @ViewById
    RelativeLayout parent_view;

    @ViewById
    TextView tv_status;

    @ViewById
    Button btn_bt;

    @ViewById
    ListView lv_devices;

    @Bean
    BluetoothDevicesAdapter adapter;

    DeviceListPresenter presenter;

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(btreceiver, filter);
        presenter = new DeviceListPresenter(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(btreceiver);
    }

    @AfterViews
    void afterviews(){
        lv_devices.setAdapter(adapter);
    }

    @Click(R.id.btn_bt)
    void onClickBluetooth(){
        if(presenter.isBluetoothOn()){
            if(presenter.getState() == DeviceListPresenter.STATE_BT_ON)
                presenter.onClickSearchDevices();
        } else {
            presenter.onClickEnableBluetooth();
        }
    }

    @Click(R.id.ib_settings)
    void onClickSettings(){
        SettingsActivity_.intent(this).start();
    }

    @ItemClick
    void lv_devicesItemClicked(final BluetoothDevice device) {
        presenter.onSelectDevice(device);
    }

    private final BroadcastReceiver btreceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                presenter.foundDevice(device);

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                presenter.setState(DeviceListPresenter.STATE_SEARCH_DONE);

            } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)){
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        presenter.setState(DeviceListPresenter.STATE_BT_TURNED_OFF);
                        break;

//                    case BluetoothAdapter.STATE_TURNING_ON:
//                        presenter.setState(DeviceListPresenter.STATE_BT_TURNING_ON);
//                        break;

                    case BluetoothAdapter.STATE_ON:
                        presenter.setState(DeviceListPresenter.STATE_BT_ON);
                        break;
                }
            }
        }
    };

    @OnActivityResult(BluetoothSystem.REQCODE_ENABlE_BT)
    void onBluetoothResult(int resultCode){
        if (resultCode == RESULT_OK) {
            presenter.onClickSearchDevices();
        } else {
            presenter.onFailedToEnableBluetooth();
        }
    }

    @Override
    public Activity getContext() {
        return this;
    }

    @Override
    public void checkPermissionsForBluetooth() {
        PermissionsHelper.checkPermissionsForBluetooth(this);
    }

    public void showEmergencyContactView(){
        if(SharedPrefHelper.getInstance(this).getString(Constants.PREFS_EMERGENCY_CONTACT).equals("")){
            EmergencyContactFragment frag = new EmergencyContactFragment();
            frag.show(getFragmentManager(), "emcontact_dialog");
        }
    }
    @Override
    public void addDeviceToList(BluetoothDevice device) {
        if (adapter.getPosition(device) == -1) {
            adapter.add(device);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void displayBluetoothBtnOn() {
        btn_bt.setText("Turn on Bluetooth");
        btn_bt.setEnabled(true);
    }

    @Override
    public void displaySearchDevicesBtnOn() {
        btn_bt.setText("Search devices");
        btn_bt.setEnabled(true);
    }

    @Override
    public void displaySearchingMessage() {
        btn_bt.setText("Searching...");
        btn_bt.setEnabled(false);
        tv_status.setVisibility(View.GONE);
    }

    @Override
    public void displayTurningOn() {
        btn_bt.setText("Turning on ...");
        btn_bt.setEnabled(false);
    }

    @Override
    public void displaySearchDone() {
        if(adapter.getCount() == 0){
            tv_status.setText("No devices found");
            tv_status.setVisibility(View.VISIBLE);
        }else{
            tv_status.setVisibility(View.GONE);
        }
        btn_bt.setEnabled(true);
    }

    @Override
    public void displayConnectConfirmation(String confirmationMsg, SimpleCallback confirmCallback) {
        new AlertDialog.Builder(DeviceListActivity.this)
                .setCancelable(false)
                .setTitle("Connect")
                .setMessage(confirmationMsg)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        confirmCallback.onReturn();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        presenter.setState(DeviceListPresenter.STATE_BT_ON);
                    }
                }).show();

        btn_bt.setEnabled(true);
    }

    @Override
    public void displayError(String error) {
        _.showToast(error);
    }

    @Override
    public void goToMainActivity(BluetoothDevice device) {
        MainActivity_.intent(DeviceListActivity.this).extra("btDevice",device).start();
    }

    @Override
    public void finishActivity() {
        finish();
    }


}
