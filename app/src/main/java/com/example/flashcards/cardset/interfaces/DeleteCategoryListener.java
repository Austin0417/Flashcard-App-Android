package com.example.flashcards.cardset.interfaces;

import com.example.flashcards.entities.Category;

import java.util.List;

public interface DeleteCategoryListener {
    public void onDeleteCategory(List<Category> categoriesToRemove);
}
