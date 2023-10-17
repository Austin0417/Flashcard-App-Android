package com.example.flashcards.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.flashcards.daos.CardDAO;
import com.example.flashcards.daos.CardSetDAO;
import com.example.flashcards.daos.CategoryDAO;
import com.example.flashcards.daos.UserDAO;
import com.example.flashcards.entities.Card;
import com.example.flashcards.entities.CardSet;
import com.example.flashcards.entities.Category;
import com.example.flashcards.entities.User;
import com.example.flashcards.helpers.Converter;

@Database(entities={User.class, CardSet.class, Card.class, Category.class}, version = 1)
@TypeConverters(Converter.class)
public abstract class FlashcardsDatabase  extends RoomDatabase {
    public abstract UserDAO userDAO();
    public abstract CardSetDAO cardSetDAO();
    public abstract CardDAO cardDAO();
    public abstract CategoryDAO categoryDAO();
}
