package android.zeroh729.com.ecggrapher.interactors;

import android.zeroh729.com.ecggrapher.ECGGrapher_;
import android.zeroh729.com.ecggrapher.data.local.Constants;
import android.zeroh729.com.ecggrapher.data.local.SharedPrefHelper;
import android.zeroh729.com.ecggrapher.presenters.EmergencyContactPresenter;
import android.zeroh729.com.ecggrapher.utils._;

public class EmergencyContactSystem implements EmergencyContactPresenter.ContactSystem {
    @Override
    public void saveEmContact(String phonenum) {
        SharedPrefHelper.getInstance(ECGGrapher_.getInstance()).putString(Constants.PREFS_EMERGENCY_CONTACT, phonenum);
    }

    @Override
    public String getEmContact() {
        return SharedPrefHelper.getInstance(ECGGrapher_.getInstance()).getString(Constants.PREFS_EMERGENCY_CONTACT);
    }

}
