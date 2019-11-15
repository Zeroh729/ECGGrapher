package android.zeroh729.com.ecggrapher.ui.main.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.zeroh729.com.ecggrapher.R;
import android.zeroh729.com.ecggrapher.presenters.EmergencyContactPresenter;

import org.androidannotations.annotations.EFragment;

@EFragment
public class EmergencyContactFragment extends DialogFragment implements EmergencyContactPresenter.ContactScreen {
    EmergencyContactPresenter presenter;

    private View view;
    EditText et_name;
    Button btn_confirm_name;

    TextView tv_confirm_message;
    TextView tv_greetings_wname;
    EditText et_phonenum;
    Button btn_add;
    Button btn_ok;
    View view_add_phonenum;
    View view_confirm;
    View view_username;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        presenter = new EmergencyContactPresenter();
        presenter.setScreen(this);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.frag_emcontact, null);

        view_add_phonenum = view.findViewById(R.id.view_enter_num);
        view_confirm = view.findViewById(R.id.view_confirm);
        view_username = view.findViewById(R.id.view_username);
        tv_confirm_message = view.findViewById(R.id.tv_confirm_message);
        et_name = view.findViewById(R.id.et_name);
        btn_confirm_name = view.findViewById(R.id.btn_confirm_name);
        tv_greetings_wname  = view.findViewById(R.id.tv_greeting_wname);
        et_phonenum = view.findViewById(R.id.et_phonenum);
        btn_add = view.findViewById(R.id.btn_add);
        btn_ok = view.findViewById(R.id.btn_ok);

        btn_confirm_name.setOnClickListener(view -> {
            presenter.onAddUsername();
        });

        btn_add.setOnClickListener(view -> onClickAddEmergencyContact());

        btn_ok.setOnClickListener(view -> {
            this.dismiss();
        });

        builder.setView(view);
        builder.setCancelable(false);
        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return view;
    }

    @Override
    public void onClickAddEmergencyContact() {
        presenter.onAddEmergencyContact(et_phonenum.getText().toString());
    }

    @Override
    public void showSavedConfirmationScreen(String phonenum) {
        tv_confirm_message.setText("Emergency contact "  + phonenum + " saved.\nYou can change this later in the settings.");
        view_add_phonenum.setVisibility(View.GONE);
        view_confirm.setVisibility(View.VISIBLE);
    }

    @Override
    public String getUsername() {
        return et_name.getText().toString();
    }

    @Override
    public void showAddEmergencyForm(String username) {
        tv_greetings_wname.setText("Hi " + username + "!");
        view_username.setVisibility(View.GONE);
        view_add_phonenum.setVisibility(View.VISIBLE);
    }
}

