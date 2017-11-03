package android.zeroh729.com.ecggrapher.utils;


import android.zeroh729.com.ecggrapher.ECGGrapher_;

public class ResourcesUtil {
    public static String getString(int resId){
        return ECGGrapher_.getInstance().getResources().getString(resId);
    }
}
