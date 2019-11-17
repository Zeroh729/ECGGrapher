package android.zeroh729.com.ecggrapher.interactors;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.zeroh729.com.ecggrapher.data.local.Constants;
import android.zeroh729.com.ecggrapher.interactors.interfaces.AbstractBluetoothDataHandler;
import android.zeroh729.com.ecggrapher.utils._;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by da Ent on 1-11-2015.
 */
public class BluetoothService extends Service {
    private static int state;

    // message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_ERROR = 1;
    public static final int STATE_CONNECTION_LOST = 2;
    public static final int STATE_CONNECTING = 3; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 4;  // now onConnected to a remote device
    public static final int STATE_CONNECTION_FAILED = 5;
    public static final int STATE_STOP = 6;

    private AbstractBluetoothDataHandler btDataHandler;

    BluetoothDevice myDevice;

    ConnectThread connectThread;
    ConnectedThread connectedThread;

    public static final String ACTION_BT_SERVICE = "ACTION_BT_SERVICE";
    public static final String EXTRA_BT_SERVICE = "EXTRA_BT_SERVICE";
    public static final String EXTRA_BT_CHANGE_STATE = "EXTRA_BT_CHANGE_STATE";

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_BT_SERVICE);
        registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action == null) return;
            if(action.equals(ACTION_BT_SERVICE)){
                String extra = intent.getStringExtra(EXTRA_BT_SERVICE);
                if(extra.equals(EXTRA_BT_CHANGE_STATE)){
                    int newState = intent.getIntExtra(EXTRA_BT_CHANGE_STATE, -1);
                    setState(newState);
                }
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String deviceAddress = intent.getStringExtra("btDeviceAddress");

        if(deviceAddress.equals(Constants.DEMO_DEVICE_ADDRESS)){
            btDataHandler = new MockBluetoothDataHandler();
        }else{
            myDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(deviceAddress);
            btDataHandler = new BluetoothDataHandler();
            connect();
        }
        return START_STICKY;
    }

    public synchronized void connect() {
        Log.d(Constants.TAG, "Connecting to: " + myDevice.getName() + " - " + myDevice.getAddress());
        // Start the thread to connect with the given device

        setState(STATE_CONNECTING);
        connectThread = new ConnectThread(myDevice);
        connectThread.start();
    }

    public synchronized void stop() {
        btDataHandler.destroy();
        cancelConnectThread();
        cancelConnectedThread();
        stopSelf();
    }

    private void updateState(){
        _.log("State is now " + state);
        if(state != STATE_NONE && state != STATE_STOP)
            btDataHandler.obtainMessage(MESSAGE_STATE_CHANGE, state, -1).sendToTarget();

        switch(state){
            case STATE_NONE:
                break;

            case STATE_CONNECTING:
                break;

            case STATE_CONNECTED:
                break;

            case STATE_CONNECTION_LOST:
                Log.e(Constants.TAG, "Connection lost");
                stop();
                break;

            case STATE_CONNECTION_FAILED:
                Log.e(Constants.TAG, "Connection Failed");
                stop();
                break;

            case STATE_ERROR:
                Log.e(Constants.TAG, "Connection Error");
                stop();
                break;

            case STATE_STOP:
                Log.d(Constants.TAG, "Stopping bluetooth service");
                stop();
                break;
        }
    }

    private synchronized void setState(int state) {
        Log.d(Constants.TAG, "setState() " + BluetoothService.state + " -> " + state);
        BluetoothService.state = state;
        updateState();
    }

    public synchronized int getState() {
        return state;
    }


    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        Log.d(Constants.TAG, "connected to: " + device.getName());

        cancelConnectThread();
        // Start the thread to manage the connection and perform transmissions
        connectedThread = new ConnectedThread(socket);
        connectedThread.start();

        setState(STATE_CONNECTED);
    }

    private void cancelConnectThread() {
        // Cancel the thread that completed the connection
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }
    }

    private void cancelConnectedThread() {
        // Cancel any thread currently running a connection
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                UUID uuid = Constants.myUUID;
                tmp = device.createRfcommSocketToServiceRecord(uuid);
            } catch (IOException e) {
                Log.e(Constants.TAG, "Create RFcomm socket failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                Log.e(Constants.TAG, "Unable to connect", connectException);
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e(Constants.TAG, "Unable to close() socket during connection failure", closeException);
                }
                setState(STATE_CONNECTION_FAILED);
                return;
            }

            synchronized (BluetoothService.this) {
                connectThread = null;
            }

            // When connection with device starts, another thread runs for managing data
            connected(mmSocket, mmDevice);
        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(Constants.TAG, "Close() socket failed", e);
            }
        }
    }


    public class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(Constants.TAG, "Temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            Log.i(Constants.TAG, "Begin connectedThread");
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            StringBuilder readMessage = new StringBuilder();

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {

                    bytes = mmInStream.read(buffer);
                    String read = new String(buffer, 0, bytes);
                    readMessage.append(read);

                    if (read.contains("\n")) {

                        btDataHandler.obtainMessage(MESSAGE_READ, bytes, -1, readMessage.toString()).sendToTarget();
                        readMessage.setLength(0);
                    }

                } catch (IOException e) {

                    Log.e(Constants.TAG, "Connection Lost", e);
                    setState(STATE_CONNECTION_LOST);
                    break;
                }
            }
        }

        /* Call this from the main activity to shutdown the connection */
        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(Constants.TAG, "close() of connect socket failed", e);}
        }
    }

}
