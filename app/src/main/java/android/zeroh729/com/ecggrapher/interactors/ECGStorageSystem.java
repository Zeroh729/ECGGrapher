package android.zeroh729.com.ecggrapher.interactors;

import android.os.Environment;
import android.zeroh729.com.ecggrapher.presenters.ECGStoragePresenter;
import android.zeroh729.com.ecggrapher.utils._;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

@EBean
public class ECGStorageSystem implements ECGStoragePresenter.StorageSystem{

    @Override
    @Background
    public void saveStringToFile(String location, String filename, String filelines) {
        try {
            File dir = new File(location);
            if(!dir.exists())
                dir.mkdir();

            File myFile = new File(location+"/"+filename);
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.write(filelines);
            myOutWriter.close();
            fOut.close();
            _.log("Success! File stored: " + filename + " in location: " + location);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
