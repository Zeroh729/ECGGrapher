package android.zeroh729.com.ecggrapher.presenters;

import android.zeroh729.com.ecggrapher.interactors.EmergencyContactSystem;
import android.zeroh729.com.ecggrapher.interactors.interfaces.SimpleCallback;
import android.zeroh729.com.ecggrapher.presenters.base.BasePresenter;

public class SettingsPresenter implements BasePresenter {
    public static final int STATE_EDIT = 0, STATE_VIEW_ONLY = 1;
    int state;
    SettingsScreen screen;
    EmergencyContactSystem emContactSystem;

    public SettingsPresenter(SettingsScreen screen) {
        this.screen = screen;
        emContactSystem = new EmergencyContactSystem();
        setState(STATE_VIEW_ONLY);
        setup();
    }

    @Override
    public void setup() {

    }

    public int getState(){
        return state;
    }

    @Override
    public void updateState() {
        switch (state){
            case STATE_EDIT:
                screen.displayFieldsEditable(true);
                screen.displaySaveButton();
                break;
            case STATE_VIEW_ONLY:
                screen.displayFieldsEditable(false);
                screen.displayEditButton();
                screen.displayUsername(emContactSystem.getUsername());
                screen.displayEmContact(emContactSystem.getEmContact());
                break;
        }
    }

    @Override
    public void setState(int state) {
        this.state = state;
        updateState();
    }

    public void onClickSave(){
        emContactSystem.saveEmContact(screen.getEmergencyContact());
        emContactSystem.saveUsername(screen.getUsername());
        setState(STATE_VIEW_ONLY);
        screen.displaySuccessSave();
    }

    public void onClickCancelEdit() {
        boolean hasChanges = !screen.getUsername().equals(emContactSystem.getUsername())
                || !screen.getEmergencyContact().equals(emContactSystem.getEmContact());
        if(hasChanges){
            screen.displayDiscardMessage(new SimpleCallback(){
                @Override
                public void onReturn() {
                    onClickSave();
                    setState(STATE_VIEW_ONLY);
                }
            });
        }else{
            screen.finishActivity();
        }
    }

    public interface SettingsScreen{

        void displayFieldsEditable(boolean isEditable);

        void displaySaveButton();

        void displayEditButton();

        void displayUsername(String username);

        void displayEmContact(String emContact);

        void displaySuccessSave();

        void finishActivity();

        void displayDiscardMessage(SimpleCallback saveChangesCallback);

        String getEmergencyContact();

        String getUsername();

    }

}
