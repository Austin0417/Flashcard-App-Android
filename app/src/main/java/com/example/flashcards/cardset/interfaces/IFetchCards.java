package com.example.flashcards.cardset.interfaces;

import com.example.flashcards.entities.Card;

import java.util.List;

public interface IFetchCards {
    public void onFetchCards(List<Card> cards);
}
