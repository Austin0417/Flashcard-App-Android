package com.example.flashcards.cardset.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.DialogFragment;

import com.example.flashcards.R;
import com.example.flashcards.cardset.interfaces.AddToCategoryListener;
import com.example.flashcards.entities.Category;

import java.util.List;

public class AddToCategoryDialog extends DialogFragment {
    private List<Category> categories;
    private Spinner selection;
    private Category selectedCategory = null;
    AddToCategoryListener listener;


    public AddToCategoryDialog(List<Category> categories) {
        this.categories = categories;
    }

    public void setAddToCategoryListener(AddToCategoryListener listener) { this.listener = listener; }

    private void initializeSpinner() {
        if (categories != null) {
            String[] spinnerOptions = new String[categories.size()];
            for (int i = 0; i < spinnerOptions.length; i++) {
                spinnerOptions[i] = categories.get(i).toString();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnerOptions);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            selection.setAdapter(adapter);
            selection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedCategory = categories.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    selectedCategory = null;
                }
            });
        } else {
            // Handle the case where there are no existing categories yet
            // Probably shouldn't allow the user to even open this dialog if there are no existing categories
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.move_to_category, null, false);
        selection = v.findViewById(R.id.categorySelection);
        initializeSpinner();

        return new AlertDialog.Builder(getContext())
                .setView(v)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onAddToCategory(selectedCategory);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
    }
}
