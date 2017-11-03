package android.zeroh729.com.ecggrapher.utils;

import java.text.SimpleDateFormat;

/**
 * Created by Admin on 8/8/16.
 */
public class DateFormatter {
    public static String formatEEEMdyyyy(long time){
        SimpleDateFormat format = new SimpleDateFormat("EEE, MMMM d, yyyy");

        return format.format(time);
    }

    public static String formathmma(long time){
        SimpleDateFormat format = new SimpleDateFormat("h:mm a");

        return format.format(time);
    }

    public static String formatMMMMddyyyy(long time){
        SimpleDateFormat format = new SimpleDateFormat("MMMM dd yyyy");

        return format.format(time);
    }

    public static String formatMMddyyyy(long time){
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

        return format.format(time);
    }

    public static String formatyyyyMMddHHmmss(long time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return format.format(time);
    }
}
