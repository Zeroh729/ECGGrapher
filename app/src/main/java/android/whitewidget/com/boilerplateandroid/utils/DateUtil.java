package android.whitewidget.com.boilerplateandroid.utils;

import android.content.Context;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static Calendar toCalendar(Date date){
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal;
    }

    public static Calendar toCalendar(int year, int month, int day){
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal;
    }

    public static long daysBetween(Date startDate, Date endDate) {
        Calendar sDate = DateUtil.toCalendar(startDate);
        Calendar eDate = DateUtil.toCalendar(endDate);

        long daysBetween = 0;
        while (sDate.before(eDate)) {
            sDate.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }


    public static boolean isDateInRange(Context context, Calendar chosen, DatePicker datePicker){
        Calendar max = toCalendar(new Date(datePicker.getMaxDate()));
        Calendar min = toCalendar(new Date(datePicker.getMinDate()));

        chosen = toCalendar(chosen.getTime());

        if(chosen.before(min) || chosen.after(max)){
            Toast.makeText(context, "Please select valid date.", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
