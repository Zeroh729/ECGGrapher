package android.zeroh729.com.ecggrapher.ui.main.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.zeroh729.com.ecggrapher.R;
import android.zeroh729.com.ecggrapher.interactors.interfaces.SimpleCallback;
import android.zeroh729.com.ecggrapher.presenters.EmergencyContactPresenter;

import org.androidannotations.annotations.EFragment;

@EFragment
public class SettingsDialogFragment extends DialogFragment {
    private View view;

    SimpleCallback onClickGotosettings;
    SimpleCallback onClickDisconnect;

    ImageButton ib_close;
    Button btn_gotosettings;
    Button btn_disconnect;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.frag_settings_dialog, null);

        ib_close = view.findViewById(R.id.ib_close);
        btn_gotosettings = view.findViewById(R.id.btn_gotosettings);
        btn_disconnect = view.findViewById(R.id.btn_disconnect);

        btn_gotosettings.setOnClickListener(view -> {
            onClickGotosettings.onReturn();
        });

        btn_disconnect.setOnClickListener(view -> {
            onClickDisconnect.onReturn();
        });

        ib_close.setOnClickListener(v -> dismiss());

        builder.setView(view);
        builder.setCancelable(false);
        return builder.create();
    }

    public void setOnClickGotosettings(SimpleCallback onClickGotosettings) {
        this.onClickGotosettings = onClickGotosettings;
    }

    public void setOnClickDisconnect(SimpleCallback onClickDisconnect) {
        this.onClickDisconnect = onClickDisconnect;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return view;
    }
}
