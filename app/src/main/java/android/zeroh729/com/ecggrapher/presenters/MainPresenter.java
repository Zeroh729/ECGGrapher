package android.zeroh729.com.ecggrapher.presenters;

import android.bluetooth.BluetoothDevice;
import android.zeroh729.com.ecggrapher.ECGGrapher_;
import android.zeroh729.com.ecggrapher.data.local.Constants;
import android.zeroh729.com.ecggrapher.data.local.SharedPrefHelper;
import android.zeroh729.com.ecggrapher.interactors.BluetoothDataHandler;
import android.zeroh729.com.ecggrapher.interactors.BluetoothSystem;
import android.zeroh729.com.ecggrapher.interactors.EmergencyContactSystem;
import android.zeroh729.com.ecggrapher.interactors.MockBluetoothDataHandler;
import android.zeroh729.com.ecggrapher.interactors.interfaces.AbstractBluetoothDataHandler;
import android.zeroh729.com.ecggrapher.interactors.interfaces.SimpleCallback;
import android.zeroh729.com.ecggrapher.interactors.interfaces.SuccessCallback;
import android.zeroh729.com.ecggrapher.presenters.base.BasePresenter;
import android.zeroh729.com.ecggrapher.ui.main.activities.MainActivity;
import android.zeroh729.com.ecggrapher.utils._;

import ecganal.Callback.ECGAnalysisCallback;
import ecganal.ECGAnalyzer;
import ecganal.Model.ECGSummary;

public class MainPresenter implements BasePresenter {
    public static final int STATE_CONNECTING = 0, STATE_CONNECTED = 1, STATE_LAGGING = 2, STATE_DISCONNECTED = 3, STATE_FINISHED = 4;
    private int state;

    BluetoothSystem btSystem;
    MainScreen screen;
    private AbstractBluetoothDataHandler handler;
    private ECGStoragePresenter ecgStoragePresenter;
    private EmergencyContactSystem emContactSystem;
    private SharedPrefHelper sharedPrefHelper = SharedPrefHelper.getInstance(ECGGrapher_.getInstance());

    public MainPresenter(MainScreen screen, BluetoothDevice device) {
        // By reaching MainActivity, BT device should already have been selected by User
        this.screen = screen;
        btSystem = new BluetoothSystem();
        btSystem.setup(screen.getContext(), new SimpleCallback() {
            @Override
            public void onReturn() {
                displayFailedToConnect();
            }
        });
        if(_.ISDEBUG){
            handler = new MockBluetoothDataHandler(screen.getContext());
        }else{
            handler = new BluetoothDataHandler(screen.getContext());
            btSystem.pairToDevice(handler, device);
            btSystem.start();
        }

        ecgStoragePresenter = new ECGStoragePresenter();
        ecgStoragePresenter.setup();
    }

    private void displayFailedToConnect(){
        screen.finish();
    }

    @Override
    public void setup() {

    }

    @Override
    public void updateState() {
        switch(this.state){
            case STATE_CONNECTED:
                screen.displayGraphingView();
                break;
            case STATE_CONNECTING:
                screen.displayConnectingView();
                break;
            case STATE_LAGGING:
                screen.displayLaggingView();
                break;
            case STATE_DISCONNECTED:
                screen.displayDisconnectedView();
                if(!_.ISDEBUG)
                    btSystem.stop();
                break;
            case STATE_FINISHED:
                if(!_.ISDEBUG)
                    btSystem.stop();
                break;
        }
    }

    @Override
    public void setState(int state) {
        this.state = state;
        updateState();
    }

    public void updateSummary(ECGSummary summary) {
        String bpmNote  = "";
        if(summary.getBPM() < 60){
            bpmNote = "Sinus Bradycardia";
        }else if (summary.getBPM() >= 101 && summary.getBPM() <= 180){
            bpmNote = "Sinus Tachycardia";
        }
        screen.setSummaryText("BPM: " + summary.getBPM() + (!bpmNote.isEmpty() ? " - " + bpmNote : ""));
    }

    public void onClickWarning() {
        String filename = sharedPrefHelper.getString(Constants.PREFS_MOST_RECENT_AFIB_FILENAME);
        String dialogMessage = "The most recent ecg record with RR intervals deviating from the standard is at " + filename + ".\nCheck up with your doctor for precautionary measures.";
        screen.displayWarningDialog(dialogMessage, () -> {
            emContactSystem = new EmergencyContactSystem();
            emContactSystem.sendSMS("I need your help! My heart is not feeling well.", new SuccessCallback() {
                @Override
                public void onSuccess() {
                    _.showToast("SMS sent!");
                }

                @Override
                public void onFail() {
                    _.showToast("SMS failed to send.");
                }
            });
        });
    }

    public void receiveData(int data) {
        if(data > 100) { //receiving threshold = 100
            double ddata = data/1024.00 * 5;
            screen.graphECGdata(ddata);
            int datacnt = ecgStoragePresenter.addDatapoint(data);

            if(datacnt >= Constants.ECG_DATA_LIMIT){
                final String filelines = ecgStoragePresenter.getFileLines();
                final String filename = ecgStoragePresenter.saveECGData();
                ECGAnalyzer.getInstance().analyzeData(filelines, new ECGAnalysisCallback() {
                    @Override
                    public void onSuccess(ECGSummary ecgSummary) {
                        updateSummary(ecgSummary);
                        String summaryFile = "Summary for " + filename + "\n"
                                + "bpm: " + ecgSummary.getBPM() + "\n";
                        if(ecgSummary.getDeviatingRRIcount() > 0){
                            summaryFile += "Deviating RRI count : " + ecgSummary.getDeviatingRRIcount() + "\n";
                            sharedPrefHelper.putString(Constants.PREFS_MOST_RECENT_AFIB_FILENAME, filename);
                            screen.displayWarningIndicator();
                        }
                        ecgStoragePresenter.saveECGSummary(filename, summaryFile);
                    }

                    @Override
                    public void onFail(String s) {

                    }
                });
            }

        }
    }

    public interface MainScreen{
        void finish();

        MainActivity getContext();

        void displayDisconnectedView();

        void displayGraphingView();

        void displayLaggingView();

        void displayConnectingView();

        void setSummaryText(String s);

        void displayWarningIndicator();

        void displayWarningDialog(String message, SimpleCallback smsPressedCallback);

        void graphECGdata(double ddata);
    }
}
