package android.zeroh729.com.ecggrapher.utils;

import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by Admin on 8/8/16.
 */
public class Validation {
    public static boolean isLegalPassword(String pass) {
        if (!pass.matches(".*[A-Z].*")) return false;
        if (!pass.matches(".*\\d.*")) return false;
        return true;
    }

    public static boolean isLegalAge(int year, int month, int day){
        Calendar age = Calendar.getInstance();
        age.set(year, month, day);
        return isLegalAge(age);
    }

    public static boolean isLegalAge(Calendar age){
        final Calendar max = Calendar.getInstance();
        max.set(Calendar.YEAR, max.get(Calendar.YEAR) - 18);

        int chosenDay = age.get(Calendar.DAY_OF_MONTH);
        int chosenMonth = age.get(Calendar.MONTH);
        int chosenYear = age.get(Calendar.YEAR);

        final Calendar chosen = Calendar.getInstance();
        chosen.set(Calendar.DAY_OF_MONTH, chosenDay);
        chosen.set(Calendar.MONTH, chosenMonth);
        chosen.set(Calendar.YEAR, chosenYear);
        chosen.set(Calendar.HOUR, 0);
        chosen.set(Calendar.MINUTE, 0);
        chosen.set(Calendar.SECOND, 0);
        chosen.set(Calendar.MILLISECOND, 0);

        if(chosen.after(max)){
            return false;
        }

        return true;
    }

    public static boolean hasBlankEditTexts(EditText... views){
        for(EditText et : views){
            if(et.getText().toString().trim().length() == 0){
                return true;
            }
        }
        return false;
    }
}
