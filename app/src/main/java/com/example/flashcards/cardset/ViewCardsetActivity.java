package com.example.flashcards.cardset;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcards.R;
import com.example.flashcards.cardset.dialogs.CardsetNameDialog;
import com.example.flashcards.daos.CardDAO;
import com.example.flashcards.daos.CardSetDAO;
import com.example.flashcards.database.AppDatabase;
import com.example.flashcards.database.FlashcardsDatabase;
import com.example.flashcards.entities.Card;
import com.example.flashcards.entities.CardSet;
import com.example.flashcards.helpers.CardsetHelper;
import com.example.flashcards.helpers.DatabaseHelper;
import com.example.flashcards.cardset.interfaces.CardsetListener;
import com.example.flashcards.cardset.interfaces.IFetchCards;
import com.example.flashcards.cardset.interfaces.IFetchCardsets;
import com.example.flashcards.cardset.recycleradapter.ViewCardsetAdapter;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ViewCardsetActivity extends AppCompatActivity implements CardsetListener {
    private RecyclerView cardsets;
    private ViewCardsetAdapter adapter;
    private int user_id;
    private List<CardSet> userCardsets;
    private boolean fetchSuccess = false;
    private CardSet currentSelection;

    @Override
    public void onCardsetClicked(CardSet cardSet) {
        String cardsJson = CardsetHelper.CardsToJson(cardSet.getCards());
        Intent intent = new Intent(this, CreateCardsetActivity.class);
        intent.putExtra("user_id", user_id);
        intent.putExtra("cardset_id", cardSet.getCardsetId());
        intent.putExtra("cardset_name", cardSet.cardset_name);
        intent.putExtra("cards", cardsJson);
        Log.i("CARDS JSON", cardsJson);

        startActivity(intent);
    }

    @Override
    public void onCardsetLongClick(View anchor, CardSet cardSet) {
        currentSelection = cardSet;
        PopupMenu menu = new PopupMenu(this, anchor);
        menu.getMenuInflater().inflate(R.menu.cardset_menu, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.cardset_menu_edit:
                        promptNewCardsetName();
                        return true;
                    case R.id.cardset_menu_delete:
                        deleteCardset(cardSet);
                        return true;
                }
                return false;
            }
        });
        menu.show();
    }

    private void promptNewCardsetName() {
        CardsetNameDialog dialog = new CardsetNameDialog();
        dialog.show(getSupportFragmentManager(), "EDIT_CARDSET");
    }

    private void updateCardset(String newName) {
        DatabaseHelper.updateCardset(newName, currentSelection.user_id, currentSelection.cardset_id);
        currentSelection.cardset_name = newName;
        adapter.updateDataset(userCardsets);
    }

    private void deleteCardset(CardSet cardSet) {
        DatabaseHelper.deleteCardset(cardSet);
        userCardsets.remove(cardSet);
        adapter.updateDataset(userCardsets);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_cardsets);
        user_id = getIntent().getIntExtra("user_id", -1);
        try {
            checkUserId();
        } catch (IllegalStateException e) {
            Toast.makeText(this, "Something went wrong while attempting to view cardsets. Please try again", Toast.LENGTH_LONG).show();
            finish();
        }
        CountDownLatch latch = new CountDownLatch(1);
        fetchUserCardsets(latch, new IFetchCardsets() {
            @Override
            public void onFetchSuccess(CountDownLatch latch) {
                fetchSuccess = true;
                latch.countDown();
            }
            @Override
            public void onFetchFailure(CountDownLatch latch) {
                fetchSuccess = false;
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (fetchSuccess) {
            bindCardsToCardSet();
            setViewReferences();
            setEventListeners();
            adapter = new ViewCardsetAdapter(userCardsets);
            cardsets.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            cardsets.setAdapter(adapter);
            // Proceed with initializing the RecyclerView and its adapter
        }
    }

    private void setViewReferences() {
        cardsets = findViewById(R.id.cardset_list);
    }
    private void setEventListeners() {
        getSupportFragmentManager().setFragmentResultListener("cardset_name", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                String newName = result.getString("value");
                if (newName != null) {
                    updateCardset(newName);
                }
            }
        });
    }
    private void checkUserId() throws IllegalStateException {
        if (user_id < 0) {
            throw new IllegalStateException("Error: user_id was not sent correctly!");
        }
    }
    private void fetchUserCardsets(CountDownLatch latch, IFetchCardsets callback) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                FlashcardsDatabase db = AppDatabase.getInstance();
                CardSetDAO dao = db.cardSetDAO();
                userCardsets = dao.getUserCardSets(user_id);
                if (userCardsets == null || userCardsets.isEmpty()) {
                    callback.onFetchFailure(latch);
                } else {
                    callback.onFetchSuccess(latch);
                }
            }
        });
    }

    private void findCards(CardSet cardSet, IFetchCards callback) {
        int cardset_id = cardSet.getCardsetId();
        FlashcardsDatabase db = AppDatabase.getInstance();
        CardDAO dao = db.cardDAO();
        List<Card> cards = dao.getAllUserCards(user_id, cardset_id);
        callback.onFetchCards(cards);
    }

    private void bindCardsToCardSet() {
        for (CardSet cardSet: userCardsets) {
            final List<Card>[] cards = new List[1];
            findCards(cardSet, new IFetchCards() {
                @Override
                public void onFetchCards(List<Card> retrievedCards) {
                    cards[0] = retrievedCards;
                    if (cards[0] != null && !cards[0].isEmpty()) {
                        cardSet.setCards(cards[0]);
                    } else {
                        Log.i("FETCHING CARDS FOR CARDSET", "Failed to fetch cards for cardset");
                    }
                }
            });
        }
    }
}
