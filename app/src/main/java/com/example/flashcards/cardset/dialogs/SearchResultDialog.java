package com.example.flashcards.cardset.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcards.R;
import com.example.flashcards.cardset.interfaces.DefinitionSelectionListener;
import com.example.flashcards.cardset.recycleradapter.SearchResultAdapter;

import java.util.List;

public class SearchResultDialog extends DialogFragment {
    private List<String> definitions;
    private DefinitionSelectionListener callback;
    private RecyclerView definitionSelection;
    private SearchResultAdapter adapter;
    private TextView headerText;
    private String searchedWord;
    private String currentSelectedDefinition;

    public SearchResultDialog(String searchedWord, List<String> definitions) {
        this.definitions = definitions;
        this.searchedWord = searchedWord;
    }
    public SearchResultDialog(String searchedWord, List<String> definitions, DefinitionSelectionListener callback) {
        this.searchedWord = searchedWord;
        this.definitions = definitions;
        this.callback = callback;
    }
    public void setSelectionListener(DefinitionSelectionListener callback) { this.callback = callback; }

    private void initializeRecyclerView() {
        adapter = new SearchResultAdapter(definitions);
        adapter.setSelectionListener(new DefinitionSelectionListener() {
            @Override
            public void onDefinitionSelected(String word, String definition) {
                if (definition != null) {
                    Log.i("NEW SELECTION", definition);
                } else {
                    Log.i("NEW SELECTION", "Unselecting previous selection...");
                }
                currentSelectedDefinition = definition;
            }
        });
        definitionSelection.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        definitionSelection.setAdapter(adapter);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.search_result, null, false);
        definitionSelection = v.findViewById(R.id.searchedDefinitions);
        headerText = v.findViewById(R.id.searchResultHeader);
        headerText.setText("Search result returned " + definitions.size() + " results: ");
        initializeRecyclerView();

        return new AlertDialog.Builder(getContext())
                .setTitle("Definition Selection")
                .setView(v)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onDefinitionSelected(searchedWord, currentSelectedDefinition);
                    }
                })
                .create();

    }
}
