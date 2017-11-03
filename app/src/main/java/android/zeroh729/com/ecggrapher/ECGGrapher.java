package android.zeroh729.com.ecggrapher;

import android.app.Application;
import android.content.Context;

import org.androidannotations.annotations.EApplication;

@EApplication
public class ECGGrapher extends Application{
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public Context getContext(){
        return context;
    }
}
