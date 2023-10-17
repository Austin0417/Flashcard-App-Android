package com.example.flashcards.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.flashcards.entities.Card;

import java.util.List;

@Dao
public interface CardDAO {
    @Insert
    public void insert(Card card);

    @Delete
    public void delete(Card card);

    @Query("SELECT * FROM Cards WHERE cardset_id=:cardset_id")
    public List<Card> getCardsFromCardSet(int cardset_id);

    @Query("SELECT * FROM Cards WHERE user_id=:user_id")
    public List<Card> getCardsFromUserId(int user_id);

    @Query("SELECT * FROM Cards WHERE user_id=:user_id AND cardset_id=:cardset_id")
    public List<Card> getAllUserCards(int user_id, int cardset_id);

    @Query("UPDATE Cards SET cardName=:newName, cardDefinition=:newDefinition WHERE user_id=:user_id AND cardset_id=:cardset_id AND card_id =:card_id")
    public void editCard(String newName, String newDefinition, int user_id, int cardset_id, int card_id);

    @Query("SELECT * FROM Cards WHERE user_id=:user_id AND cardset_id=:cardset_id AND cardName=:cardName AND cardDefinition=:cardDefinition LIMIT 1")
    public Card getCard(int user_id, int cardset_id, String cardName, String cardDefinition);

}
