package android.whitewidget.com.boilerplateandroid.utils;

import android.whitewidget.com.boilerplateandroid.BoilerplateApplication_;

public class ResourcesUtil {
    public static String getString(int resId){
        return BoilerplateApplication_.getInstance().getResources().getString(resId);
    }
}
