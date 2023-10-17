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

public class AddCardDialog extends DialogFragment {
    private EditText wordName;
    private EditText wordDef;

    // Boolean variable to know if we are showing this dialog to create a new card, or update an existing one
    private boolean isUpdating = false;

    public AddCardDialog() {}

    public AddCardDialog(boolean isUpdating) { this.isUpdating = isUpdating; }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getContext()).inflate(R.layout.add_card, null, false);
        wordName = v.findViewById(R.id.wordName);
        wordDef = v.findViewById(R.id.wordDef);
        return new AlertDialog.Builder(getContext())
                .setView(v)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!wordName.getText().toString().isEmpty() && !wordDef.getText().toString().isEmpty()) {
                            Bundle result = new Bundle();
                            result.putString("word_name", wordName.getText().toString());
                            result.putString("word_def", wordDef.getText().toString());

                            // Creating a card
                            if (!isUpdating) {
                                getParentFragmentManager().setFragmentResult("card_info", result);
                            // Updating a card
                            } else {
                                getParentFragmentManager().setFragmentResult("update_card", result);
                            }
                        } else {
                            Toast.makeText(getContext(), "Error adding card: one or more fields were empty!", Toast.LENGTH_LONG).show();
                            getDialog().dismiss();
                        }
                    }
                })
                .create();
    }
}
