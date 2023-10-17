package com.example.flashcards.cardset.interfaces;

import java.util.concurrent.CountDownLatch;

public interface IFetchCardsets {
    public void onFetchSuccess(CountDownLatch latch);
    public void onFetchFailure(CountDownLatch latch);
}
