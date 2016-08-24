package android.whitewidget.com.boilerplateandroid.utils;

import android.util.Log;
import android.whitewidget.com.boilerplateandroid.BoilerplateApplication_;
import android.widget.Toast;

public class _ {
    public static void showToast(String message){
        Toast.makeText(BoilerplateApplication_.getInstance().getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static void log(String message){
        Log.d("TEST", message);
    }

    public static void logError(String message){
        Log.e("TEST", message);
    }
}
