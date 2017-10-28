package android.whitewidget.com.boilerplateandroid.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.whitewidget.com.boilerplateandroid.R;
import android.widget.Button;
import android.widget.TextView;


public class DialogUtil {
    public static Dialog createDialog(Context context, String title, String message, String actionYtext, String actionNtext, DialogInterface.OnClickListener listenerY, DialogInterface.OnClickListener listenerN){
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(context);
        AlertDialog dialog =  dBuilder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(actionYtext, listenerY)
                .setNegativeButton(actionNtext, listenerN)
                .setCancelable(false)
                .create();
        return dialog;
    }

    public static Dialog createDialog(Context context, String title, String message, String actionYtext, DialogInterface.OnClickListener listenerY){
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(context);
        final AlertDialog dialog = dBuilder
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(actionYtext, listenerY)
                .setCancelable(false)
                .create();
        return dialog;
    }

    public static Dialog createDialog(Context context, String title, String message){
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(context);
        final AlertDialog dialog = dBuilder
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(false)
                .create();
        return dialog;
    }

    public static void showDialog(Dialog alertDialog){
        alertDialog.show();
        TextView textView = (TextView) alertDialog.getWindow().findViewById(android.R.id.message);
        TextView alertTitle = (TextView) alertDialog.getWindow().findViewById(R.id.alertTitle);
        Button button1 = (Button) alertDialog.getWindow().findViewById(android.R.id.button1);
        Button button2 = (Button) alertDialog.getWindow().findViewById(android.R.id.button2);
        FontUtil.getInstance().setTypeface(FontUtil.Type.NORMAL, textView, alertTitle, button1, button2);
    }
}
