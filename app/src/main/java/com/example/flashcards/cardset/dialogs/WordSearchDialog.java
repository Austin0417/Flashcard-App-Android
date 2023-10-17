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
import com.example.flashcards.cardset.interfaces.WordSearchListener;

public class WordSearchDialog extends DialogFragment {

    private WordSearchListener callback;
    private EditText wordSearch;


    public void setWordSearchListener(WordSearchListener callback) {
        this.callback = callback;
    }

    public WordSearchDialog() {}
    public WordSearchDialog(WordSearchListener callback) { this.callback = callback;}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        View v = LayoutInflater.from(getContext()).inflate(R.layout.word_search_dialog, null, false);
        wordSearch = v.findViewById(R.id.wordSearchInput);
        return new AlertDialog.Builder(getContext())
                .setTitle("Online Word Search")
                .setView(v)
                .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onWordSearched(wordSearch.getText().toString());
                    }
                })
                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create();
    }
}
