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

public class EditPriceDialog extends AppCompatDialogFragment {

    private EditText edit_price;
    private String price;
    private EditPriceDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_edit_price_dialog, null);
        builder.setView(view).setTitle("Enter a price")
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                price = edit_price.getText().toString();
                listener.applyText(price);
            }
        });

        edit_price = view.findViewById(R.id.edit_price);
        edit_price.setText(ItemInfoActivity.currentItem.getItem_price());
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (EditPriceDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+ "Must implement EditPriceDialogListener");
        }

    }

    public interface EditPriceDialogListener {
        void applyText(String name);
    }
}
