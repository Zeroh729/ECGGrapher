package android.zeroh729.com.ecggrapher.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ShortECGSummary implements Parcelable{
    int BPM;
    String BPMNote;

    public ShortECGSummary(int BPM, String BPMNote){
        this.BPM = BPM;
        this.BPMNote = BPMNote;
    }

    protected ShortECGSummary(Parcel in) {
        BPM = in.readInt();
        BPMNote = in.readString();
    }

    public static final Creator<ShortECGSummary> CREATOR = new Creator<ShortECGSummary>() {
        @Override
        public ShortECGSummary createFromParcel(Parcel in) {
            return new ShortECGSummary(in);
        }

        @Override
        public ShortECGSummary[] newArray(int size) {
            return new ShortECGSummary[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(BPM);
        parcel.writeString(BPMNote);
    }

    public int getBPM() {
        return BPM;
    }

    public void setBPM(int BPM) {
        this.BPM = BPM;
    }

    public String getBPMNote() {
        return BPMNote;
    }

    public void setBPMNote(String BPMNote) {
        this.BPMNote = BPMNote;
    }
}
