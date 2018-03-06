package android.zeroh729.com.ecggrapher.presenters;

import android.os.Environment;
import android.zeroh729.com.ecggrapher.interactors.ECGStorageSystem;
import android.zeroh729.com.ecggrapher.presenters.base.BasePresenter;
import android.zeroh729.com.ecggrapher.utils.DateFormatter;
import android.zeroh729.com.ecggrapher.utils._;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class ECGStoragePresenter implements BasePresenter {
    private Date startDate;
    private long startTime;
//    private ArrayList<String> filelines;
    private String filelines;
    private int filelinesCnt;
    private ECGStorageSystem storageSystem;
    /*TODO: Current implementation of file segmentation is every x filelines, it should be after every n mintues*/

    @Override
    public void setup() {
        init();
        storageSystem = new ECGStorageSystem();
    }

    private void init() {
        startDate = new Date();
        startTime = System.currentTimeMillis();
        filelinesCnt = 0;
        filelines = "";
        _.log("ECGStoragePresenter Initializing - Start time is " + startTime);
    }

    public void addDatapoint(int data){
        long elapsedTime = System.currentTimeMillis() - startTime;
        filelines += (elapsedTime + "," + data + "\n");
        filelinesCnt++;
        if(filelinesCnt >= 1000){
            String fileLocation = Environment.getExternalStorageDirectory().getPath()+"/ECGData";
            String filename = DateFormatter.formatyyyyMMddHHmmss(startDate.getTime())+".txt";
            _.log("ECGStoragePresenter Preparing to store file in " + fileLocation
                    + "\nFilename: " + filename
                    + "\nData:" + filelines.substring(0,300)
                    + "\n...");

            storageSystem.saveStringToFile(fileLocation, filename, filelines);
            init();
        }
    }

    @Override
    public void updateState() {

    }

    @Override
    public void setState(int state) {

    }

    public interface StorageSystem{
        void saveStringToFile(String location, String filename, String filelines);
    }
}
