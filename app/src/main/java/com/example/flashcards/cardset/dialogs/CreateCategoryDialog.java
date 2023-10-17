package com.example.flashcards.cardset.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.example.flashcards.R;
import com.example.flashcards.cardset.interfaces.AddCategoryListener;

public class CreateCategoryDialog extends DialogFragment {
    AddCategoryListener listener;
    private EditText categoryName;

    public void setCategoryListener(AddCategoryListener listener) { this.listener = listener; }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        View v = LayoutInflater.from(getContext()).inflate(R.layout.add_category_dialog, null, false);
        categoryName = v.findViewById(R.id.categoryName);
        return new AlertDialog.Builder(getContext())
                .setView(v)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (categoryName.getText().toString() != null && !categoryName.getText().toString().isEmpty()) {
                            listener.onCreateCategory(categoryName.getText().toString());
                        }
                        getDialog().dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onCreateCategory(null);
                    }
                })
                .create();
    }
}
