package android.whitewidget.com.boilerplateandroid.utils;


import android.whitewidget.com.boilerplateandroid.ECGGrapher_;

public class ResourcesUtil {
    public static String getString(int resId){
        return ECGGrapher_.getInstance().getResources().getString(resId);
    }
}
