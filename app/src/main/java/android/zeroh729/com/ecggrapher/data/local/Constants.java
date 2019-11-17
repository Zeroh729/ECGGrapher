package android.zeroh729.com.ecggrapher.data.local;

import android.os.Environment;

import java.util.UUID;

public interface Constants {
    int ECG_DATA_LIMIT = 1000;
    public static final int REQCODE_ENABlE_BT = 20221;

    int COUNT_Y = 5;
    int COUNT_X = 300;

    String PREFS_MOST_RECENT_AFIB_FILENAME = "PREFS_MOST_RECENT_AFIB_FILENAME";
    String PREFS_EMERGENCY_CONTACT = "PREFS_EMERGENCY_CONTACT";
    String PREFS_USERNAME = "PREFS_USERNAME";

    String SAVE_FILEDIR = Environment.getExternalStorageDirectory().getPath()+"/ECGData";

    String TAG = "WARDS";

    UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    String DEMO_DEVICE_NAME = "Demo Device";
    String DEMO_DEVICE_ADDRESS = "SIMULATED DATA FOR DEMO";

    // Key names received from the BluetoothChatService Handler
    String ACTION_BTCONN_SERVICE = "ACTION_BTCONN_SERVICE";
    String EXTRA_MSG_TYPE = "EXTRA_MSG_TYPE";
    int MSG_STATE_CHANGED = 0;
    int MSG_RECEIVED_DATA = 1;
    int MSG_UPDATE_SUMMARY = 2;
    int MSG_HEART_WARNING = 3;
    String EXTRA_BTCONN_STATE = "EXTRA_BTCONN_STATE";
    String EXTRA_BTCONN_RECEIVED_DATA = "EXTRA_BTCONN_RECEIVED_DATA";
    String EXTRA_BTCONN_UPDATE_SUMMARY = "EXTRA_BTCONN_UPDATE_SUMMARY";
    String EXTRA_BTCONN_HEART_WARNING = "EXTRA_BTCONN_HEART_WARNING";

}

