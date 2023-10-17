package com.example.flashcards.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.flashcards.entities.Category;

import java.util.List;

@Dao

public interface CategoryDAO {
    @Insert(onConflict= OnConflictStrategy.REPLACE)
    public void insert(Category category);

    @Delete
    public void delete(Category category);

    @Query("SELECT * FROM Categories WHERE user_id=:user_id")
    public List<Category> getUserCategories(int user_id);

    @Query("SELECT * FROM Categories WHERE name=:name AND user_id=:user_id LIMIT 1")
    public Category getCategory(String name, int user_id);

    @Query("DELETE FROM Categories WHERE user_id=:user_id AND name=:category_name")
    public void deleteCategory(int user_id, String category_name);
}
