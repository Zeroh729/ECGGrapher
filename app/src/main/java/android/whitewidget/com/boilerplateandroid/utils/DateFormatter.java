package android.whitewidget.com.boilerplateandroid.utils;

import java.text.SimpleDateFormat;

/**
 * Created by Admin on 8/8/16.
 */
public class DateFormatter {
    public static String formatMMMMddyyyy(long time){
        SimpleDateFormat format = new SimpleDateFormat("MMMM dd yyyy");

        return format.format(time);
    }

    public static String formatMMddyyyy(long time){
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

        return format.format(time);
    }
}
