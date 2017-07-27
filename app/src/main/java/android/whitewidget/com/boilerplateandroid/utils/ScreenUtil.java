package android.whitewidget.com.boilerplateandroid.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.whitewidget.com.boilerplateandroid.BoilerplateApplication;
import android.whitewidget.com.boilerplateandroid.BoilerplateApplication_;

public class ScreenUtil {
    private float dpWidth;

    public ScreenUtil(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float density = activity.getResources().getDisplayMetrics().density;
        dpWidth = outMetrics.widthPixels / density;
    }

    public float getWidth() {
        return dpWidth;
    }

    public static boolean isScreenLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static boolean isScreenPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public static int getScreenWidth() {
        WindowManager windowManager = (WindowManager) BoilerplateApplication_.getInstance().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.widthPixels;
    }

    public static int getScreenHeight() {
        WindowManager windowManager = (WindowManager) BoilerplateApplication_.getInstance().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.heightPixels;
    }
}