package com.example.flashcards.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.flashcards.entities.CardSet;

import java.util.List;

@Dao
public interface CardSetDAO {
    @Insert
    public void insert(CardSet cardSet);

    @Delete
    public void delete(CardSet cardSet);

    @Query("SELECT * FROM Cardsets WHERE cardset_id=:cardset_id LIMIT 1")
    public CardSet getById(int cardset_id);

    @Query("SELECT * FROM Cardsets WHERE user_id=:user_id")
    public List<CardSet> getUserCardSets(int user_id);

    @Query("SELECT cardset_id FROM Cardsets WHERE user_id=:user_id AND cardset_name=:cardset_name")
    public int getCardSetId(int user_id, String cardset_name);

    @Query("UPDATE Cardsets SET cardset_name=:newName WHERE cardset_id=:cardset_id AND user_id=:user_id")
    public void editCardset(String newName, int cardset_id, int user_id);

    @Query("DELETE FROM Cardsets WHERE user_id=:user_id AND cardset_id=:cardset_id AND cardset_name=:name")
    public void deleteCardset(String name, int user_id, int cardset_id);

    @Query("SELECT * FROM Cardsets WHERE user_id=:user_id AND cardset_name=:cardset_name LIMIT 1")
    public CardSet getCardSet(int user_id, String cardset_name);

    @Query("UPDATE Cardsets SET category_name=:category_name WHERE user_id=:user_id AND cardset_name=:cardset_name")
    public void moveCardsetToCategory(int user_id, String cardset_name, String category_name);

    @Query("SELECT category_name FROM Cardsets WHERE user_id=:user_id AND cardset_name=:cardset_name")
    public String getExistingCardsetCategory(int user_id, String cardset_name);

    @Query("DELETE FROM Cardsets WHERE cardset_id=:cardset_id")
    public void deleteById(int cardset_id);

    @Query("UPDATE Cardsets SET category_name=NULL WHERE user_id=:user_id AND category_name=:category_name")
    public void updateCategoriesOnDelete(int user_id, String category_name);
}
