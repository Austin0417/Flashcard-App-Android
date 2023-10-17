package com.example.flashcards.cardset.interfaces;

import android.view.View;

import com.example.flashcards.entities.Card;

public interface CardListener {
    public void onCardLongClick(View v, Card card);
}
