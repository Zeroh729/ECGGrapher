package android.zeroh729.com.ecggrapher.ui.main.activities;


import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.zeroh729.com.ecggrapher.R;
import android.zeroh729.com.ecggrapher.data.local.Constants;
import android.zeroh729.com.ecggrapher.data.model.ECGSeries;
import android.zeroh729.com.ecggrapher.data.model.ShortECGSummary;
import android.zeroh729.com.ecggrapher.interactors.BluetoothService;
import android.zeroh729.com.ecggrapher.interactors.interfaces.SimpleCallback;
import android.zeroh729.com.ecggrapher.presenters.MainPresenter;
import android.zeroh729.com.ecggrapher.ui.base.BaseBluetoothActivity;
import android.zeroh729.com.ecggrapher.ui.main.fragments.SettingsDialogFragment;
import android.zeroh729.com.ecggrapher.ui.main.views.MyFadeFormatter;
import android.zeroh729.com.ecggrapher.utils._;

import com.androidplot.util.Redrawer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.XYPlot;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseBluetoothActivity implements MainPresenter.MainScreen {
    @ViewById
    RelativeLayout parent_view;

    @ViewById
    XYPlot plot;

    @ViewById
    TextView tv_status;

    @ViewById
    View view_popup_warning;

    private ECGSeries series;
    private Redrawer redrawer;

    @Bean
    MainPresenter presenter;

    @Override
    protected void onStart() {
        super.onStart();
        presenter = new MainPresenter();
        presenter.setup(this);
        series = new ECGSeries(plot);
        plot.addSeries(series, new MyFadeFormatter(Constants.COUNT_X - 100));
        plot.setRangeBoundaries(0, Constants.COUNT_Y, BoundaryMode.FIXED);
        plot.setDomainBoundaries(0, Constants.COUNT_X, BoundaryMode.FIXED);
        redrawer = new Redrawer(plot, 60, true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(redrawer != null)
            redrawer.finish();
        presenter.setState(MainPresenter.STATE_FINISHED);
    }


    @Click(R.id.view_popup_warning)
    void onClickWarning(){
        presenter.onClickWarning();
    }


    @Click(R.id.ib_settings)
    void onClickSettings(){
        presenter.onClickSettings();
    }

    @Override
    public void receiveBtStateUpdate(Intent intent) {
        String action = intent.getAction();
        if(action == null) return;
        if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
            if (state == BluetoothAdapter.STATE_OFF) {
                onDisconnected();
            }
        } else if(action.equals(Constants.ACTION_BTCONN_SERVICE)){
            int msgType = intent.getIntExtra(Constants.EXTRA_MSG_TYPE, -1);
            if(msgType == Constants.MSG_STATE_CHANGED){
                int state = intent.getIntExtra(Constants.EXTRA_BTCONN_STATE, -1);
                switch (state) {
                    case BluetoothService.STATE_NONE:
                    case BluetoothService.STATE_ERROR:
                    case BluetoothService.STATE_CONNECTION_FAILED:
                    case BluetoothService.STATE_CONNECTION_LOST:
                        _.log("MESSAGE_STATE_CHANGE : err code " + state);
                        presenter.setState(MainPresenter.STATE_DISCONNECTED);
                        break;
                    case BluetoothService.STATE_CONNECTED:
                        _.log("MESSAGE_STATE_CHANGE : Connected");
                        presenter.setState(MainPresenter.STATE_CONNECTED);
                        break;
                }
            }else if(msgType == Constants.MSG_RECEIVED_DATA){
                double data = intent.getDoubleExtra(Constants.EXTRA_BTCONN_RECEIVED_DATA, -1);
                receiveData(data);
            }else if(msgType == Constants.MSG_HEART_WARNING){
                displayWarningIndicator();
            }else if(msgType == Constants.MSG_UPDATE_SUMMARY){
                ShortECGSummary ecgSummary = intent.getParcelableExtra(Constants.EXTRA_BTCONN_UPDATE_SUMMARY);
                displaySummary(ecgSummary);
            }
        }
    }

    @UiThread
    public void receiveData(double data) {
        presenter.receiveData(data);
    }

    @Override
    public void addActionToFilter(IntentFilter filter) {
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(Constants.ACTION_BTCONN_SERVICE);
    }

    @UiThread
    void displaySummary(ShortECGSummary summary){
        presenter.updateSummary(summary);
    }

    public void onDisconnected() {
        presenter.setState(MainPresenter.STATE_DISCONNECTED);
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public MainActivity getContext() {
        return this;
    }

    public void displayDisconnectedView() {
        _.showToast("Disconnected from device");
        finish();
    }

    @Override
    public void displayGraphingView(String statusMsg) {
        plot.setTitle(statusMsg);
    }

    @Override
    public void displayLaggingView() {

    }

    @Override
    public void displayConnectingView(String statusMsg) {
        plot.setTitle(statusMsg);
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

    @Override
    public void onBackPressed() {
        displaySettingsDialog();
    }
}