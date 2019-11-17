package android.zeroh729.com.ecggrapher.presenters;

import android.bluetooth.BluetoothDevice;
import android.zeroh729.com.ecggrapher.ECGGrapher_;
import android.zeroh729.com.ecggrapher.data.local.Constants;
import android.zeroh729.com.ecggrapher.data.local.SharedPrefHelper;
import android.zeroh729.com.ecggrapher.data.model.ShortECGSummary;
import android.zeroh729.com.ecggrapher.interactors.BluetoothDataHandler;
import android.zeroh729.com.ecggrapher.interactors.BluetoothSystem;
import android.zeroh729.com.ecggrapher.interactors.EmergencyContactSystem;
import android.zeroh729.com.ecggrapher.interactors.MockBluetoothDataHandler;
import android.zeroh729.com.ecggrapher.interactors.interfaces.AbstractBluetoothDataHandler;
import android.zeroh729.com.ecggrapher.interactors.interfaces.SimpleCallback;
import android.zeroh729.com.ecggrapher.interactors.interfaces.SuccessCallback;
import android.zeroh729.com.ecggrapher.presenters.base.BasePresenter;
import android.zeroh729.com.ecggrapher.ui.base.BaseBluetoothActivity;
import android.zeroh729.com.ecggrapher.ui.main.activities.MainActivity;
import android.zeroh729.com.ecggrapher.utils._;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import ecganal.Callback.ECGAnalysisCallback;
import ecganal.ECGAnalyzer;
import ecganal.Model.ECGSummary;

@EBean
public class MainPresenter implements BasePresenter {
    public static final int STATE_CONNECTING = 0, STATE_CONNECTED = 1, STATE_LAGGING = 2, STATE_DISCONNECTED = 3, STATE_FINISHED = 4;
    private int state;

    BluetoothSystem btSystem;

    MainScreen screen;
    private EmergencyContactSystem emContactSystem;
    private SharedPrefHelper sharedPrefHelper = SharedPrefHelper.getInstance(ECGGrapher_.getInstance());

    public MainPresenter() {
        btSystem = BluetoothSystem.getInstance();
    }

    public void setup(MainScreen screen) {
        // By reaching MainActivity, BT device should already have been selected by User
        this.screen = screen;
        btSystem.setup(new SuccessCallback() {
            @Override
            public void onSuccess() {
                setState(STATE_CONNECTED);
                _.log("MainPresenter : " + btSystem.getDeviceName());
            }

            @Override
            public void onFail() {
                setState(STATE_DISCONNECTED);
            }
        });
    }

    @Override
    public void setup() {

    }

    @Override
    public void updateState() {
        switch(this.state){
            case STATE_CONNECTED:
                screen.displayGraphingView("Connected to " + btSystem.getDeviceName());
                break;
            case STATE_CONNECTING:
                screen.displayConnectingView("Connecting to " + btSystem.getDeviceName() + "...");
                break;
            case STATE_LAGGING:
                screen.displayLaggingView();
                break;
            case STATE_DISCONNECTED:
                screen.displayDisconnectedView();
                btSystem.connectionStop();
                break;
            case STATE_FINISHED:
//                btSystem.connectionStop();
                break;
        }
    }

    @Override
    public void setState(int state) {
        this.state = state;
        updateState();
    }

    public void updateSummary(ShortECGSummary summary) {
        screen.setSummaryText("BPM: " + summary.getBPM() + (!summary.getBPMNote().isEmpty() ? " - " + summary.getBPMNote() : ""));
    }

    public void onClickWarning() {
        String filename = sharedPrefHelper.getString(Constants.PREFS_MOST_RECENT_AFIB_FILENAME);
        String dialogMessage = "The most recent ecg record with RR intervals deviating from the standard is at " + filename + ".\nCheck up with your doctor for precautionary measures.";
        screen.displayWarningDialog(dialogMessage, () -> {
            emContactSystem = new EmergencyContactSystem();
            emContactSystem.sendSMS("This is an automatic message from WARDS app. " + emContactSystem.getUsername() + "'s heart may be in critical condition. Send help ASAP!", new SuccessCallback() {
                @Override
                public void onSuccess() {
                    _.showToast("SMS sent to "+emContactSystem.getEmContact()+"!");
                }

                @Override
                public void onFail() {
                    _.showToast("SMS failed to send.");
                }
            });
        });
    }

    public void receiveData(double data) {
        screen.graphECGdata(data);
    }

    public void onClickSettings() {
        screen.displaySettingsDialog();
    }

    public interface MainScreen{
        void finishActivity();

        BaseBluetoothActivity getContext();

        void displayDisconnectedView();

        void displayGraphingView(String deviceName);

        void displayLaggingView();

        void displayConnectingView(String statusMsg);

        void setSummaryText(String s);

        void displayWarningIndicator();

        void displayWarningDialog(String message, SimpleCallback smsPressedCallback);

        void graphECGdata(double ddata);

        void displaySettingsDialog();
    }
}
