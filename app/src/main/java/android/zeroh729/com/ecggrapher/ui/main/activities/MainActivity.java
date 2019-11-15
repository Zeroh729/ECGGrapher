package android.zeroh729.com.ecggrapher.ui.main.activities;


import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.zeroh729.com.ecggrapher.R;
import android.zeroh729.com.ecggrapher.data.local.Constants;
import android.zeroh729.com.ecggrapher.data.local.SharedPrefHelper;
import android.zeroh729.com.ecggrapher.data.model.ECGSeries;
import android.zeroh729.com.ecggrapher.interactors.BluetoothDataHandler;
import android.zeroh729.com.ecggrapher.interactors.BluetoothService;
import android.zeroh729.com.ecggrapher.interactors.BluetoothSystem;
import android.zeroh729.com.ecggrapher.interactors.EmergencyContactSystem;
import android.zeroh729.com.ecggrapher.interactors.MockBluetoothDataHandler;
import android.zeroh729.com.ecggrapher.interactors.interfaces.SimpleCallback;
import android.zeroh729.com.ecggrapher.interactors.interfaces.SuccessCallback;
import android.zeroh729.com.ecggrapher.presenters.ECGStoragePresenter;
import android.zeroh729.com.ecggrapher.presenters.MainPresenter;
import android.zeroh729.com.ecggrapher.ui.base.BaseActivity;
import android.zeroh729.com.ecggrapher.ui.main.adapters.BluetoothDevicesAdapter;
import android.zeroh729.com.ecggrapher.ui.main.fragments.SettingsDialogFragment;
import android.zeroh729.com.ecggrapher.ui.main.views.MyFadeFormatter;
import android.zeroh729.com.ecggrapher.utils._;

import com.androidplot.util.Redrawer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.XYPlot;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import ecganal.Callback.ECGAnalysisCallback;
import ecganal.ECGAnalyzer;
import ecganal.Model.ECGSummary;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements MainPresenter.MainScreen {
    @ViewById
    RelativeLayout parent_view;

    @ViewById
    XYPlot plot;

    @ViewById
    TextView tv_status;

    @ViewById
    View view_popup_warning;

    @Extra
    BluetoothDevice btDevice;

    private ECGSeries series;
    private Redrawer redrawer;

    MainPresenter presenter;

    @Override
    protected void onStart() {
        super.onStart();
        presenter = new MainPresenter(this, btDevice);
        series = new ECGSeries(plot);
        plot.addSeries(series, new MyFadeFormatter(Constants.COUNT_X - 100));
        plot.setRangeBoundaries(0, Constants.COUNT_Y, BoundaryMode.FIXED);
        plot.setDomainBoundaries(0, Constants.COUNT_X, BoundaryMode.FIXED);
        redrawer = new Redrawer(plot, 60, true);

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(btreceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        _.log("Receiver unregistered");
        unregisterReceiver(btreceiver);
        if(redrawer != null)
            redrawer.finish();
        presenter.setState(MainPresenter.STATE_FINISHED);
    }

    private final BroadcastReceiver btreceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction() != null
                    && intent.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {

                    int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                    if (state == BluetoothAdapter.STATE_OFF) {
                        presenter.setState(MainPresenter.STATE_DISCONNECTED);
                    }
                }
            }
    };

    @Click(R.id.view_popup_warning)
    void onClickWarning(){
        presenter.onClickWarning();
    }


    @Click(R.id.ib_settings)
    void onClickSettings(){
        presenter.onClickSettings();
    }

    @UiThread
    public void receiveData(int data) {
        presenter.receiveData(data);
    }

    @UiThread
    void displaySummary(ECGSummary summary){
        presenter.updateSummary(summary);
    }

    public void disconnected() {
        presenter.setState(MainPresenter.STATE_DISCONNECTED);
    }

    @Override
    public MainActivity getContext() {
        return this;
    }

    @Override
    public void displayDisconnectedView() {
        _.showToast("Disconnected from device");
        finish();
    }

    @Override
    public void displayGraphingView(String deviceName) {
        plot.setTitle("Connected to " + deviceName);
    }

    @Override
    public void displayLaggingView() {

    }

    @Override
    public void displayConnectingView() {

    }

    @Override
    public void setSummaryText(String s) {
        tv_status.setText(s);
    }

    @UiThread
    public void displayWarningIndicator(){
        view_popup_warning.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayWarningDialog(String message, SimpleCallback smsPressedCallback) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("AFib symptom detected")
                .setMessage(message)
                .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Send SMS to Emergency Contact", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        smsPressedCallback.onReturn();
                    }
                })
                .show();
    }

    @Override
    public void graphECGdata(double ddata) {
        series.addData(ddata);
    }

    @Override
    public void displaySettingsDialog() {
        SettingsDialogFragment frag = new SettingsDialogFragment();
        frag.setOnClickDisconnect(onClickDisconnect);
        frag.setOnClickGotosettings(onClickGotoSettings);
        frag.show(getFragmentManager(), "settings_dialog");
    }

    SimpleCallback onClickDisconnect = new SimpleCallback() {
        @Override
        public void onReturn() {
            presenter.setState(MainPresenter.STATE_DISCONNECTED);
        }
    };

    SimpleCallback onClickGotoSettings = new SimpleCallback() {
        @Override
        public void onReturn() {
            SettingsActivity_.intent(MainActivity.this).start();
        }
    };
}