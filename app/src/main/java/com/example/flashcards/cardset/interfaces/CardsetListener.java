package com.example.flashcards.cardset.interfaces;

import android.view.View;

import com.example.flashcards.entities.CardSet;

public interface CardsetListener {
    public void onCardsetClicked(CardSet cardSet);
    public void onCardsetLongClick(View anchor, CardSet cardSet);
}
