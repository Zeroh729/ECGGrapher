package android.zeroh729.com.ecggrapher.data.local;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;

public class PermissionsHelper {

    final static int MY_PERMISSIONS_REQUEST = 20001;

    public static void checkPermissionsForBluetooth(Activity a) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SEND_SMS};
            ArrayList<String> permsToRequest = new ArrayList<>();


            for(String p : permissions){
                if(a.checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED){
                    permsToRequest.add(p);
                }
            }

            //            // Should we show an explanation?
            //            if (shouldShowRequestPermissionRationale(
            //                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //                // Explain to the user why we need to read the contacts
            //            }

            if(permsToRequest.size() > 0) {
                String[] permsArr = new String[permsToRequest.size()];
                a.requestPermissions(permsToRequest.toArray(permsArr),
                        MY_PERMISSIONS_REQUEST);
            }
            // MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE is an
            // app-defined int constant that should be quite unique
        }
    }
}
