package android.zeroh729.com.ecggrapher.utils;

import android.util.Log;
import android.zeroh729.com.ecggrapher.ECGGrapher_;
import android.widget.Toast;
import android.zeroh729.com.ecggrapher.data.local.Constants;

public class _ {
    public static boolean ISDEBUG = false;
    public static boolean ISFILTERED = false;

    public static void showToast(String message){
        Toast.makeText(ECGGrapher_.getInstance().getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static void log(String message){
        Log.d(Constants.TAG, message);
    }

    public static void logError(String message){
        Log.e(Constants.TAG, message);
    }
}
