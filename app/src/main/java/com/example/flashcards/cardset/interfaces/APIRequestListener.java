package com.example.flashcards.cardset.interfaces;

public interface APIRequestListener {
    public void onRequestSuccess(String jsonData);
    public void onRequestFailed(String errorMessage);
}
