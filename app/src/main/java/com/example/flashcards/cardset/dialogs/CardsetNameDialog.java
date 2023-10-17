package com.example.flashcards.cardset.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.flashcards.R;

public class CardsetNameDialog extends DialogFragment {
    private EditText cardsetName;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.cardset_name_dialog, null, false);
        cardsetName = dialogView.findViewById(R.id.cardsetNameInput);
        return new AlertDialog.Builder(getContext())
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (cardsetName.getText().toString().isEmpty()) {
                            Toast.makeText(getContext(), "Cardset name cannot be empty!", Toast.LENGTH_LONG).show();
                        } else {
                            // TODO Send the cardset name back to the CreateCardsetActivity
                            Bundle data = new Bundle();
                            data.putString("value", cardsetName.getText().toString());
                            getParentFragmentManager().setFragmentResult("cardset_name", data);
                            getDialog().dismiss();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle result = new Bundle();
                        result.putBoolean("should_exit", true);
                        getParentFragmentManager().setFragmentResult("cardset_name", result);
                    }
                })
                .setView(dialogView).create();
    }
}
