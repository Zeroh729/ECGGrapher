package android.whitewidget.com.boilerplateandroid.utils;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.whitewidget.com.boilerplateandroid.BoilerplateApplication_;

import java.util.List;

public class PackageUtil {
    public static boolean isPackageExists(String targetPackage){
        List<ApplicationInfo> packages;
        PackageManager pm;

        pm = BoilerplateApplication_.getInstance().getPackageManager();
        packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if(packageInfo.packageName.equals(targetPackage))
                return true;
        }
        return false;
    }
}
