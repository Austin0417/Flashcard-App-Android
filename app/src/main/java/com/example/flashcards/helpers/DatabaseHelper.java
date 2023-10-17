package com.example.flashcards.helpers;

import android.os.AsyncTask;

import com.example.flashcards.cardset.interfaces.InsertCardCallback;
import com.example.flashcards.daos.CardDAO;
import com.example.flashcards.daos.CardSetDAO;
import com.example.flashcards.daos.CategoryDAO;
import com.example.flashcards.database.AppDatabase;
import com.example.flashcards.database.FlashcardsDatabase;
import com.example.flashcards.entities.Card;
import com.example.flashcards.entities.CardSet;
import com.example.flashcards.entities.Category;
import com.example.flashcards.cardset.interfaces.CardsetCategoryListener;
import com.example.flashcards.cardset.interfaces.DeleteCardsetListener;
import com.example.flashcards.cardset.interfaces.FetchCategoryListener;
import com.example.flashcards.cardset.interfaces.MoveCategoryListener;

import java.util.List;

public class DatabaseHelper {
    private static FlashcardsDatabase db = AppDatabase.getInstance();


    public static void insertCardSet(CardSet cardSet, CreateCardSetListener listener) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                CardSetDAO dao = db.cardSetDAO();
                dao.insert(cardSet);
                listener.onCreateCardSet(cardSet);
            }
        });
    }

    public static void getTargetCardset(int user_id, String cardset_name) {

    }

    public static void updateCardset(String newName, int user_id, int cardset_id) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                FlashcardsDatabase db = AppDatabase.getInstance();
                CardSetDAO dao = db.cardSetDAO();
                dao.editCardset(newName, cardset_id, user_id);
            }
        });
    }

    public static void deleteCardset(CardSet cardSet) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                FlashcardsDatabase db = AppDatabase.getInstance();
                CardSetDAO dao = db.cardSetDAO();
                dao.delete(cardSet);
            }
        });
    }

    public static void deleteCardset(int cardset_id, DeleteCardsetListener callback) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                FlashcardsDatabase db = AppDatabase.getInstance();
                CardSetDAO dao = db.cardSetDAO();
                dao.deleteById(cardset_id);
                CardSet cardSet = dao.getById(cardset_id);
                if (cardSet != null) {
                    callback.onDeleteFailed();
                } else {
                    callback.onDeleteSuccess();
                }
            }
        });
    }

    public static void moveCardsetToCategory(int user_id, String cardsetName, String categoryName, MoveCategoryListener callback) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                FlashcardsDatabase db = AppDatabase.getInstance();
                CardSetDAO dao = db.cardSetDAO();
                dao.moveCardsetToCategory(user_id, cardsetName, categoryName);
                String updatedCategory = dao.getExistingCardsetCategory(user_id, cardsetName);
                if (updatedCategory != null && !updatedCategory.equals(categoryName)) {
                    callback.onMoveFailed();
                } else {
                    callback.onMoveSuccess();
                }
            }
        });
    }

    public static void insertCard(Card card) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                CardDAO dao = db.cardDAO();
                dao.insert(card);
            }
        });
    }

    public static void insertCard(Card card, InsertCardCallback callback) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                FlashcardsDatabase db = AppDatabase.getInstance();
                CardDAO dao = db.cardDAO();
                dao.insert(card);
                Card insertedCard = dao.getCard(card.user_id, card.cardset_id, card.cardName, card.cardDefinition);
                if (insertedCard != null) {
                    callback.onCreateCardSuccess();
                } else {
                    callback.onCreateCardFailed();
                }
            }
        });
    }

    public static void updateCard(String newName, String newDefinition, int user_id, int cardset_id, int card_id) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                FlashcardsDatabase db = AppDatabase.getInstance();
                CardDAO dao = db.cardDAO();
                dao.editCard(newName, newDefinition, user_id, cardset_id, card_id);
            }
        });
    }

    public static void deleteCard(Card card) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                FlashcardsDatabase db = AppDatabase.getInstance();
                CardDAO dao = db.cardDAO();
                dao.delete(card);
            }
        });
    }
    public static void fetchUserCategories(int user_id, FetchCategoryListener callback) {
                FlashcardsDatabase db = AppDatabase.getInstance();
                CategoryDAO dao = db.categoryDAO();
                List<Category> fetchedCategories = dao.getUserCategories(user_id);
                if (fetchedCategories != null) {
                    callback.onFetchSuccess(fetchedCategories);
                } else {
                    callback.onFetchFail();
                }
    }

    public static void insertCategory(Category category) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                FlashcardsDatabase db = AppDatabase.getInstance();
                CategoryDAO dao = db.categoryDAO();
                dao.insert(category);
            }
        });
    }

    public static void deleteCategory(int user_id, Category category) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                FlashcardsDatabase db = AppDatabase.getInstance();
                CategoryDAO categoryDAO = db.categoryDAO();
                CardSetDAO cardSetDAO = db.cardSetDAO();
                categoryDAO.deleteCategory(user_id, category.name);
                cardSetDAO.updateCategoriesOnDelete(user_id, category.name);
            }
        });
    }

    public static void fetchExistingCardsetCategory(int user_id, String cardset_name, CardsetCategoryListener callback) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                FlashcardsDatabase db = AppDatabase.getInstance();
                CardSetDAO dao = db.cardSetDAO();
                String category_name = dao.getExistingCardsetCategory(user_id, cardset_name);
                callback.onCategoryFetch(category_name);

            }
        });
    }

}
