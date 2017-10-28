package android.whitewidget.com.boilerplateandroid.utils;

import android.provider.Settings;
import android.whitewidget.com.boilerplateandroid.BoilerplateApplication_;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtil {
    public static String md5Hash(String textToBeHashed){
        try {
            MessageDigest md = null;
            md = MessageDigest.getInstance("MD5");
            md.update(textToBeHashed.getBytes());

            byte byteData[] = md.digest();

            //convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

            //convert the byte to hex format method 2
//            StringBuffer hexString = new StringBuffer();
//            for (int i=0;i<byteData.length;i++) {
//                String hex=Integer.toHexString(0xff & byteData[i]);
//                if(hex.length()==1) hexString.append('0');
//                hexString.append(hex);
//            }
//            System.out.println("Digest(in hex format):: " + hexString.toString());

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getSourceId(){
        return Settings.Secure.getString(BoilerplateApplication_.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
