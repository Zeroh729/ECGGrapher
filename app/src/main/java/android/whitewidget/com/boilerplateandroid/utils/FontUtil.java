package android.whitewidget.com.boilerplateandroid.utils;
import java.lang.reflect.Field;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.whitewidget.com.boilerplateandroid.ECGGrapher_;
import android.widget.TextView;

import org.androidannotations.annotations.EBean;

import static android.R.attr.type;
import static org.androidannotations.annotations.EBean.Scope.Singleton;

@EBean(scope = Singleton)
public class FontUtil {
    public enum Type{
        NORMAL, MEDIUM, DEMIBOLD, ULTRALIGHT
    }

    private static HashMap<Type, Typeface> fontmap;
    private static FontUtil instance;

    public static FontUtil getInstance(){
        if(instance == null){
            Context context = ECGGrapher_.getInstance().getContext();
            instance = new FontUtil();
            fontmap = new HashMap<>();
            fontmap.put(Type.NORMAL, Typeface.createFromAsset(context.getAssets(), "fonts/avenirnext_regular.ttf"));
            fontmap.put(Type.MEDIUM, Typeface.createFromAsset(context.getAssets(), "fonts/avenirnext_medium.ttf"));
            fontmap.put(Type.DEMIBOLD, Typeface.createFromAsset(context.getAssets(), "fonts/avenirnext_demibold.ttf"));
            fontmap.put(Type.ULTRALIGHT, Typeface.createFromAsset(context.getAssets(), "fonts/avenirnext_ultralight.ttf"));
        }
        return instance;
    }

    public  Typeface getNormal(){
        return fontmap.get(Type.NORMAL);
    }

    public  Typeface getMedium(){
        return fontmap.get(Type.MEDIUM);
    }

    public  Typeface getDemibold(){
        return fontmap.get(Type.DEMIBOLD);
    }

    public  Typeface getUltralight(){
        return fontmap.get(Type.ULTRALIGHT);
    }

    public void setTypeface(Activity activity, Type type, int... resIds) {
        for (int resId: resIds) {
            try {
                ((TextView) activity.findViewById(resId)).setTypeface(fontmap.get(type), Typeface.BOLD);
            }catch (Exception e){
                _.logError("Class can't be casted to TextView");
            }
        }
    }
    public void setTypeface(Type type, TextView... views) {
        for (TextView view: views) {
            if(view != null)
                view.setTypeface(fontmap.get(type), Typeface.BOLD);
        }
    }
}
