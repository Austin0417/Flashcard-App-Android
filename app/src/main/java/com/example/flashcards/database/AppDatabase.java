package com.example.flashcards.database;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

// Singleton design pattern for generating one handle to our database that will be shared
public class AppDatabase {
    private static FlashcardsDatabase db;

    public AppDatabase(Context context) {
        db = Room.databaseBuilder(context, FlashcardsDatabase.class, "Flashcards_Database").allowMainThreadQueries().build();
    }

    public static FlashcardsDatabase getInstance() {
        return db;
    }
}
