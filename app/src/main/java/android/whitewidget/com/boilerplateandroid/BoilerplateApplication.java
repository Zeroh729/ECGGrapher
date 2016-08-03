package android.whitewidget.com.boilerplateandroid;

import android.app.Application;
import android.content.Context;

import org.androidannotations.annotations.EApplication;

@EApplication
public class BoilerplateApplication extends Application{
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
