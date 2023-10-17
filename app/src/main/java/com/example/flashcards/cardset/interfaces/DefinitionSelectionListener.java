package com.example.flashcards.cardset.interfaces;

import androidx.annotation.Nullable;

public interface DefinitionSelectionListener {
    public void onDefinitionSelected(@Nullable String word, String definition);
}
