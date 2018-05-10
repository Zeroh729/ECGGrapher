package android.zeroh729.com.ecggrapher.interactors;

import android.os.Handler;
import android.os.Message;
import android.zeroh729.com.ecggrapher.data.local.Constants;
import android.zeroh729.com.ecggrapher.ui.main.activities.MainActivity;
import android.zeroh729.com.ecggrapher.utils._;

import java.lang.ref.WeakReference;

public class MockBluetoothDataHandler extends Handler {
    private final WeakReference<MainActivity> mActivity;
    private long delayMs = 12;
    private Thread thread;
    private boolean keepRunning;

    private int dummydataIndex = 0;
    private int[] dummydata = new int[]{293,293,273,257,251,248,243,243,246,245,208,83,235,540,540,539,283,25,266,226,229,238,242,246,247,250,257,262,269,277,285,296,309,316,320,320,317,307,293,276,261,244,233,230,226,225,229,228,229,231,233,233,237,236,236,235,235,237,231,235,232,229,226,226,225,224,226,228,229,228,227,228,231,231,232,231,231,236,236,235,236,237,234,239,241,240,244,243,241,246,246,243,246,258,250,265,255,243,245,246,246,246,248,247,225,93,204,541,540,540,411,2,245,238,226,242,242,245,254,255,259,261,266,275,284,289,298,311,312,314,311,303,292,278,262,248,232,222,221,221,220,222,223,227,229,229,229,234,230,230,228,226,227,225,225,225,228,229,229,235,234,233,232,232,231,235,233,234,237,237,241,241,240,241,243,243,243,242,243,244,246,247,250,248,247,247,248,253,252,260,268,259,270,255,244,244,244,244,243,242,244,232,119,147,401,541,540,540,2,144,250,207,225,231,235,240,241,245,252,255,260,267,275,286,296,303,307,304,300,291,281,267,253,241,230,226,225,228,229,229,227,231,231,233,231,232,232,231,229,231,227,229,226,226,226,223,223,227,229,224,222,224,224,229,230,233,234,234,235,237,236,237,234,240,237,237,239,240,242,243,239,248,258,252,264,257,240,240,239,236,239,242,240,237,139,150,383,540,539,541,1,82,247,215,226,234,237,240,246,251,254,260,266,274,280,290,303,311,314,310,304,293,281,265,253,234,224,218,222,221,222,225,226,225,228,229,232,232,235,236,235,236,232,234,233,228,230,233,231,232,232,237,235,232,239,238,239,239,240,243,243,249,245,244,246,246,248,257,265,268,285,270,254,250,248,244,243,241,247,237,115,191,500,540,540,485,1,175,252,218,234,237,245,251,257,261,268,272,276,285,295,304,315,324,326,323,319,307,286,269,250,236,222,219,223,224,225,228,228,233,236,237,237,241,242,245,245,244,241,240,242,247,245,245,245,240,242,244,246,245,244,245,246,244,245,247,247,245,243,245,247,246,247,248,249,249,252,253,253,252,252,256,258,267,263,277,266,248,248,249,246,244,244,247,235,105,173,469,539,540,504,3,189,248,219,241,242,250,256,259,263,269,276,288,295,306,313,324,330,333,333,332,319,302,285,268,249,235,229,226,225,223,227,230,231,235,239,238,236,237,239,240,240,238,234,237,234,234,233,234,235,236,235,234,239,238,240,242,242,244,243,242,243,240,243,249,244,248,251,249,249,248,248,248,244,246,245,249,259,269,266,282,268,254,253,252,247,250,252,254,241,111,177,466,542,540,502,1,175,248,214,229,229,234,233,239,245,249,254,259,269,279,286,292,295,303,300,296,291,280,264,250,230,223,213,212,215,218,217,220,226,229,230,228,229,229,229,230,229,229,224,225,223,226,224,224,226,225,232,231,227,231,231,230,233,236,238,233,233,235,235,235,228,233,236,237,236,244,251,256,257,246,236,235,233,231,232,237,238,205,105,250,540,540,539,196,2,191,240,228,244,255,252,252,252,258,269,274,279,288,292,296,304,306,302,302,295,285,264,245,227,206,199,192,194,198,199,202,205,207,208,211,209,211,215,219,219,216,214,213,216,217,217,222,224,224,228,227,231,234,230,235,234,247,253,252,269,264,245,236,237,232,234,241,240,239,166,131,337,541,541,541,14,49,255,231,235,242,246,250,256,259,263,271,278,285,290,307,317,326,328,330,323,315,295,278,263,245,231,227,228,224,222,228,227,231,235,237,239,241,238,242,242,241,240,237,240,236,236,241,238,238,239,238,239,239,240,241,240,243,245,248,249,250,249,250,251,248,249,251,248,246,246,246,244,245,257,257,263,262,246,248,246,245,248,248,247,235,116,166,447,540,540,527,2,166,257,222,238,241,249,253,258,258,269,273,277,285,292,305,312,318,327,324,318,310,296,282,262,245,236,229,225,224,228,228,229,234,235,237,237,237,240,241,238,238,238,239,239,236,239,239,234,239,239,238,238,239,242,241,244,243,241,240,242,243,244,245,245,248,248,247,248,246,250,247,248,251,248,251,251,251,253,250,248,249,250,261,261,274,255,252,256,254,251,251,252,242,130,159,412,540,540,541,3,140,251,209,228,231,233,238,241,250,253,257,265,274,284,297,307,315,320,319,316,310,296,276,265,247,235,225,220,217,219,218,223,226,229,231,232,233,234,231,230,233,229,228,231,230,230,231,228,231,229,230,232,232,233,233,235,235,233,233,235,234,231,234,235,233,232,233,236,232,233,233,238,235,233,235,242,242,240,241,241,241,239,244,247,259,251,271,252,246,247,249,251,250,249,246,179,117,298,539,539,539,80,54,262,222,232,242,248,253,255,258,260,266,269,274,283,292,304,313,316,319,320,314,299,281,267,253,236,225,223,222,220,218,222,226,229,232,235,238,235,237,237,239,239,233,236,237,236,237,238,235,234,234,237,238,239,235,239,239,239,239,239,237,241,240,245,242,241,242,244,246,247,244,248,247,248,250,250,252,263,263,269,274,262,251,254,251,252,253,255,251,198,111,286,540,541,540,172,40,266,231,235,242,248,255,259,265,269,276,282,289,297,305};

    public MockBluetoothDataHandler(final MainActivity activity) {
        mActivity = new WeakReference<>(activity);

        keepRunning = true;

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (keepRunning) {
//                        _.log("Received data!");
                        if(dummydataIndex >= dummydata.length-1){
                            dummydataIndex = 0;
                        }
                        activity.receiveData(dummydata[dummydataIndex++]);

                        Thread.sleep(delayMs);
                    }
                } catch (InterruptedException e) {
                    keepRunning = false;
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    @Override
    public void handleMessage(Message msg) {
//        final MainActivity activity = mActivity.get();
    }

}