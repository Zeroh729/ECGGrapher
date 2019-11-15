package android.zeroh729.com.ecggrapher.presenters;

import android.zeroh729.com.ecggrapher.interactors.EmergencyContactSystem;
import android.zeroh729.com.ecggrapher.interactors.interfaces.SuccessCallback;
import android.zeroh729.com.ecggrapher.presenters.base.BasePresenter;

public class EmergencyContactPresenter implements BasePresenter{
    EmergencyContactSystem system = new EmergencyContactSystem();
    ContactScreen screen;

    @Override
    public void setup() {

    }

    @Override
    public void updateState() {

    }

    @Override
    public void setState(int state) {

    }

    public void setScreen(ContactScreen screen){
        this.screen = screen;
    }

    public void onAddEmergencyContact(String phonenum) {
        system.saveEmContact(phonenum);
        screen.showSavedConfirmationScreen(system.getEmContact());
    }

    public void onAddUsername() {
        String username = screen.getUsername();
        system.saveUsername(username);
        screen.showAddEmergencyForm(username);
    }

    public interface ContactSystem{
        void saveEmContact(String phonenum);
        void saveUsername(String username);
        void sendSMS(String msg, SuccessCallback onSuccess);
        String getEmContact();
        String getUsername();
    }

    public interface ContactScreen{
        void onClickAddEmergencyContact();
        void showSavedConfirmationScreen(String phonenum);
        String getUsername();
        void showAddEmergencyForm(String username);
    }
}
