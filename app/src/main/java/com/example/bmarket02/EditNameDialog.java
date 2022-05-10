package com.example.bmarket02;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class EditNameDialog extends AppCompatDialogFragment {

    private EditText edit_name;
    private String name;
    private EditNameDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_edit_name_dialog, null);
        builder.setView(view).setTitle("Enter your name")
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                name = edit_name.getText().toString();
                listener.applyText(name);
            }
        });

        edit_name = view.findViewById(R.id.edit_name);
        edit_name.setText(ProfileActivity.name_txt.getText());
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (EditNameDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+ "Must implement EditNameDialogListener");
        }

    }

    public interface EditNameDialogListener {
        void applyText(String name);
    }
}
