package android.zeroh729.com.ecggrapher.interactors.interfaces;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.zeroh729.com.ecggrapher.ECGGrapher_;
import android.zeroh729.com.ecggrapher.data.local.Constants;
import android.zeroh729.com.ecggrapher.data.local.SharedPrefHelper;
import android.zeroh729.com.ecggrapher.data.model.ShortECGSummary;
import android.zeroh729.com.ecggrapher.presenters.ECGStoragePresenter;
import android.zeroh729.com.ecggrapher.ui.base.BaseBluetoothActivity;
import android.zeroh729.com.ecggrapher.ui.main.activities.MainActivity;
import android.zeroh729.com.ecggrapher.utils._;

import java.lang.ref.WeakReference;

import ecganal.Callback.ECGAnalysisCallback;
import ecganal.ECGAnalyzer;
import ecganal.Model.ECGSummary;

public abstract class AbstractBluetoothDataHandler extends Handler {
    private ECGStoragePresenter ecgStoragePresenter;
    private ECGAnalyzer ecgAnalyzer;

    public abstract void handleMessage(Message msg);
    public abstract void destroy();

    public AbstractBluetoothDataHandler() {
        ecgStoragePresenter = new ECGStoragePresenter();
        ecgStoragePresenter.setup();
        ecgAnalyzer = new ECGAnalyzer();
    }

    protected void consumeData(int data) {
        if(data > 100 || !_.ISFILTERED) { //receiving threshold = 100
            double ddata = data/1024.00 * 5;
            updateUIDataPoint(ddata);
            int datacnt = ecgStoragePresenter.addDatapoint(data);

            if(datacnt >= Constants.ECG_DATA_LIMIT){
                final String filelines = ecgStoragePresenter.getFileLines();
                final String filename = ecgStoragePresenter.saveECGData();
                ecgAnalyzer.analyzeData(filelines, new ECGAnalysisCallback() {
                    @Override
                    public void onSuccess(ECGSummary ecgSummary) {
                        updateUISummary(ecgSummary);
                        String summaryFile = "Summary for " + filename + "\n"
                                + "bpm: " + ecgSummary.getBPM() + "\n";
                        if(ecgSummary.getDeviatingRRIcount() > 0){
                            summaryFile += "Deviating RRI count : " + ecgSummary.getDeviatingRRIcount() + "\n";
                            SharedPrefHelper.getInstance(ECGGrapher_.getInstance()).putString(Constants.PREFS_MOST_RECENT_AFIB_FILENAME, filename);
                            updateUIWarning();
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

    protected void updateUIDataPoint(double data){
        Intent intent = new Intent();
        intent.setAction(Constants.ACTION_BTCONN_SERVICE);
        intent.putExtra(Constants.EXTRA_MSG_TYPE, Constants.MSG_RECEIVED_DATA);
        intent.putExtra(Constants.EXTRA_BTCONN_RECEIVED_DATA, data);
        ECGGrapher_.getInstance().sendBroadcast(intent);
    }

    private void updateUIWarning() {
        Intent intent = new Intent();
        intent.setAction(Constants.ACTION_BTCONN_SERVICE);
        intent.putExtra(Constants.EXTRA_MSG_TYPE, Constants.MSG_HEART_WARNING);
        ECGGrapher_.getInstance().sendBroadcast(intent);
    }

    private void updateUISummary(ECGSummary ecgSummary) {
        String bpmNote  = "";
        if(ecgSummary.getBPM() < 60){
            bpmNote = "Sinus Bradycardia";
        }else if (ecgSummary.getBPM() >= 101 && ecgSummary.getBPM() <= 180){
            bpmNote = "Sinus Tachycardia";
        }
        ShortECGSummary ecg = new ShortECGSummary(ecgSummary.getBPM(),bpmNote);
        Intent intent = new Intent();
        intent.setAction(Constants.ACTION_BTCONN_SERVICE);
        intent.putExtra(Constants.EXTRA_MSG_TYPE, Constants.MSG_UPDATE_SUMMARY);
        intent.putExtra(Constants.EXTRA_BTCONN_UPDATE_SUMMARY, ecg);
        ECGGrapher_.getInstance().sendBroadcast(intent);
    }
}
