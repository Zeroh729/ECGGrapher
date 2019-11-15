package android.zeroh729.com.ecggrapher.ui.main.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.zeroh729.com.ecggrapher.R;
import android.zeroh729.com.ecggrapher.interactors.interfaces.SimpleCallback;
import android.zeroh729.com.ecggrapher.presenters.SettingsPresenter;
import android.zeroh729.com.ecggrapher.ui.base.BaseActivity;
import android.zeroh729.com.ecggrapher.utils._;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_settings)
public class SettingsActivity extends BaseActivity implements SettingsPresenter.SettingsScreen {
    @ViewById
    EditText et_username;

    @ViewById
    EditText et_emcontact;

    @ViewById
    Button btn_save_edit;

    @ViewById
    Toolbar toolbar;

    SettingsPresenter presenter;

    @AfterViews
    void afterview(){
        presenter = new SettingsPresenter(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Settings");
    }


    @Override
    public void displayFieldsEditable(boolean isEditable) {
        et_emcontact.setFocusable(isEditable);
        et_emcontact.setFocusableInTouchMode(isEditable);
        et_emcontact.setClickable(isEditable);

        et_username.setFocusable(isEditable);
        et_username.setFocusableInTouchMode(isEditable);
        et_username.setClickable(isEditable);
        if(isEditable){
            et_username.requestFocus();
        }
    }

    @Override
    public void displaySaveButton() {
        btn_save_edit.setText("Save");
    }

    @Override
    public void displayEditButton() {
        btn_save_edit.setText("Edit details");
    }

    @Override
    public void displayUsername(String username) {
        et_username.setText(username);
    }

    @Override
    public void displayEmContact(String emContact) {
        et_emcontact.setText(emContact);
    }

    @Override
    public void displaySuccessSave() {
        _.showToast("Successfully saved");
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void displayDiscardMessage(SimpleCallback saveChangesCallback) {
        new AlertDialog.Builder(SettingsActivity.this)
                .setCancelable(false)
                .setTitle("Save changes?")
                .setMessage("You haven't saved your changes.")
                .setPositiveButton("Save changes", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        saveChangesCallback.onReturn();
                        dialog.dismiss();
                        finishActivity();
                    }
                })
                .setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finishActivity();
                    }
                }).show();
    }

    @Override
    public String getEmergencyContact() {
        return et_emcontact.getText().toString();
    }

    @Override
    public String getUsername() {
        return et_username.getText().toString();
    }


    @Click(R.id.btn_save_edit)
    void onClickSaveEdit(){
        if (presenter.getState() == SettingsPresenter.STATE_EDIT){
            presenter.onClickSave();
            presenter.setState(SettingsPresenter.STATE_VIEW_ONLY);
        }else if (presenter.getState() == SettingsPresenter.STATE_VIEW_ONLY){
            presenter.setState(SettingsPresenter.STATE_EDIT);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                presenter.onClickCancelEdit();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
