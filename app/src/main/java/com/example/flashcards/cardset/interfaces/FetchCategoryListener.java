package com.example.flashcards.cardset.interfaces;

import com.example.flashcards.entities.Category;

import java.util.List;

public interface FetchCategoryListener {
    public void onFetchSuccess(List<Category> categories);

    public void onFetchFail();

}
