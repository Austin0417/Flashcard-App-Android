package com.example.flashcards.cardset.interfaces;

import com.example.flashcards.entities.Category;

public interface CategoryClickListener {
    public void onCategorySelected(Category category);
    public void onCategoryUnselected(Category category);
}
