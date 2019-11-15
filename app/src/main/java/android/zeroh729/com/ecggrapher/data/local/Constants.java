package android.zeroh729.com.ecggrapher.data.local;

import java.util.UUID;

public interface Constants {
    int ECG_DATA_LIMIT = 1000;

    int COUNT_Y = 5;
    int COUNT_X = 300;

    String PREFS_MOST_RECENT_AFIB_FILENAME = "PREFS_MOST_RECENT_AFIB_FILENAME";
    String PREFS_EMERGENCY_CONTACT = "PREFS_EMERGENCY_CONTACT";

    String TAG = "WARDS";
    int REQUEST_ENABLE_BT = 1;

    // message types sent from the BluetoothChatService Handler
    int MESSAGE_STATE_CHANGE = 1;
    int MESSAGE_READ = 2;
    int MESSAGE_WRITE = 3;
    int MESSAGE_SNACKBAR = 4;

    // Constants that indicate the current connection state
    int STATE_NONE = 0;       // we're doing nothing
    int STATE_ERROR = 1;
    int STATE_CONNECTING = 2; // now initiating an outgoing connection
    int STATE_CONNECTED = 3;  // now connected to a remote device

    UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    // Key names received from the BluetoothChatService Handler
    String EXTRA_DEVICE  = "EXTRA_DEVICE";
    String SNACKBAR = "toast";

}

