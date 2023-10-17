package com.example.flashcards.cardset;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcards.R;
import com.example.flashcards.cardset.dialogs.CardsetNameDialog;
import com.example.flashcards.cardset.dialogs.DeleteCategoryDialog;
import com.example.flashcards.cardset.dialogs.SearchResultDialog;
import com.example.flashcards.cardset.dialogs.WordSearchDialog;
import com.example.flashcards.cardset.interfaces.APIRequestListener;
import com.example.flashcards.cardset.interfaces.AddCategoryListener;
import com.example.flashcards.cardset.interfaces.DefinitionSelectionListener;
import com.example.flashcards.cardset.interfaces.DeleteCategoryListener;
import com.example.flashcards.cardset.interfaces.InsertCardCallback;
import com.example.flashcards.cardset.interfaces.MoveCategoryListener;
import com.example.flashcards.cardset.interfaces.WordSearchListener;
import com.example.flashcards.daos.CardSetDAO;
import com.example.flashcards.database.AppDatabase;
import com.example.flashcards.database.FlashcardsDatabase;
import com.example.flashcards.entities.CardSet;
import com.example.flashcards.entities.Category;
import com.example.flashcards.helpers.APIRequest;
import com.example.flashcards.helpers.CardsetHelper;
import com.example.flashcards.helpers.CreateCardSetListener;
import com.example.flashcards.helpers.DatabaseHelper;
import com.example.flashcards.cardset.dialogs.AddCardDialog;
import com.example.flashcards.cardset.dialogs.AddToCategoryDialog;
import com.example.flashcards.cardset.dialogs.CreateCategoryDialog;
import com.example.flashcards.cardset.interfaces.AddToCategoryListener;
import com.example.flashcards.cardset.interfaces.CardListener;
import com.example.flashcards.cardset.interfaces.CardsetCategoryListener;
import com.example.flashcards.cardset.interfaces.DeleteCardsetListener;
import com.example.flashcards.cardset.interfaces.FetchCategoryListener;
import com.example.flashcards.cardset.recycleradapter.CreateCardsetAdapter;
import com.example.flashcards.entities.Card;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CreateCardsetActivity extends AppCompatActivity implements CardListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private int user_id;
    private String cardsetName;
    private int cardSet_id;
    private boolean isExistingCardset = false;

    private RecyclerView flashcards;
    private ImageButton addFlashcardBtn;
    private ImageView optionsBtn;
    private Spinner categorySelections;

    private ArrayAdapter<String> categoryAdapter;
    private CreateCardsetAdapter cardsetRecyclerAdapter;
    private List<Card> cards = new ArrayList<Card>();
    private List<Category> userCategories;

    // Variable to hold currentSelection of a long clicked Card
    private Card currentSelection;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode) {
            case 3:
                if (permissions != null && grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(CreateCardsetActivity.this, "Internet access permissions granted. You can search online for words now", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(CreateCardsetActivity.this, "Permission was denied, you must allow enable internet permissions to use this feature", Toast.LENGTH_LONG).show();
                }
        }
    }



    private void setViewReferences() {
        flashcards = findViewById(R.id.flashcardsList);
        addFlashcardBtn = findViewById(R.id.addFlashCardBtn);
        optionsBtn = findViewById(R.id.optionsBtn);
        cardsetRecyclerAdapter = new CreateCardsetAdapter(cards);
        categorySelections = findViewById(R.id.categories);
        populateSpinner();

        flashcards.setAdapter(cardsetRecyclerAdapter);
        flashcards.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    }

    private void setViewHandlers() {
        addFlashcardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO find out a cool way to allow users to add flashcards (word name and definition)
                // TODO For now, just use a DialogFragment
                promptCardInfo(false);
            }
        });
        flashcards.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                CreateCardsetAdapter.currentViewIndex = layoutManager.findFirstVisibleItemPosition();
                Log.i("VIEW INDEX", "" + CreateCardsetAdapter.currentViewIndex);
            }
        });
        optionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsMenu(optionsBtn);
            }
        });
    }

    // Setting fragment listeners for child DialogFragments
    private void setFragmentListeners() {
        FragmentManager manager = getSupportFragmentManager();
        manager.setFragmentResultListener("cardset_name", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                // Creating a new CardSet
                if (requestKey.equals("cardset_name")) {
                    cardsetName = result.getString("value");
                    boolean shouldExit = result.getBoolean("should_exit", false);
                    if (shouldExit) {
                        CreateCardsetActivity.this.finish();
                    }
                    if (cardsetName == null) {
                        // If the user didn't enter a cardset name, return to the dashboard activity
                        Toast.makeText(CreateCardsetActivity.this, "Cardset name cannot be empty!", Toast.LENGTH_LONG).show();
                        CreateCardsetActivity.this.finish();
                    } else {
                        // Proceed with inserting the CardSet with the user's id
                        DatabaseHelper.insertCardSet(new CardSet(cardsetName, user_id), new CreateCardSetListener() {
                            @Override
                            public void onCreateCardSet(CardSet cardSet) {
                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        FlashcardsDatabase db = AppDatabase.getInstance();
                                        CardSetDAO dao = db.cardSetDAO();
                                        cardSet_id = dao.getCardSetId(cardSet.getUserId(), cardSet.getCardsetName());
                                    }
                                });
                            }
                        });
                    }
                }
            }
        });
        manager.setFragmentResultListener("card_info", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                // Creating a new Card
                if (requestKey.equals("card_info")) {
                    String cardName = result.getString("word_name");
                    String cardDefinition = result.getString("word_def");

                    if (cardName != null && cardDefinition != null) {
                        Card newCard = new Card(cardName, cardDefinition, user_id, cardSet_id);
                        DatabaseHelper.insertCard(newCard);
                        cards.add(newCard);
                        cardsetRecyclerAdapter.updateDataset(cards);
                    }
                }
            }
        });;
        manager.setFragmentResultListener("update_card", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                String wordName = result.getString("word_name");
                String wordDefinition = result.getString("word_def");
                if (wordName != null && wordDefinition != null) {
                    updateCard(currentSelection, wordName, wordDefinition);
                }
            }
        });
    }

    // Fetches user created categories for cardsets
    private void fetchCategories() {
        DatabaseHelper.fetchUserCategories(user_id, new FetchCategoryListener() {
            @Override
            public void onFetchSuccess(List<Category> categories) {
                userCategories = categories;
            }
            @Override
            public void onFetchFail() {
                userCategories = null;
                Log.i("FETCH CATEGORIES", "No existing categories in database");
            }
        });
    }

    private void promptSetName() {
        CardsetNameDialog dialog = new CardsetNameDialog();
        dialog.show(getSupportFragmentManager(), "CARDSET_NAME");
    }

    private void promptCardInfo(boolean isUpdating) {
        AddCardDialog dialog = new AddCardDialog(isUpdating);
        dialog.show(getSupportFragmentManager(), "EDIT_FLASHCARD");
    }

    private void promptCategoryInfo() {
        CreateCategoryDialog dialog = new CreateCategoryDialog();
        dialog.setCategoryListener(new AddCategoryListener() {
            @Override
            public void onCreateCategory(String categoryName) {
                if (categoryName != null) {
                    Log.i("CREATE CATEGORY", categoryName);
                    Category category = new Category(categoryName, user_id);
                    DatabaseHelper.insertCategory(category);
                    categoryAdapter.insert(categoryName, userCategories.size());
                    if (userCategories != null) {
                        userCategories.add(category);
                    } else {
                        userCategories = new ArrayList<Category>();
                        userCategories.add(category);
                    }
                    categoryAdapter.notifyDataSetChanged();
                    Toast.makeText(CreateCardsetActivity.this, "Successfully created new cardset category", Toast.LENGTH_LONG).show();
                } else {
                    Log.i("CREATE CATEGORY", "Invalid input for category name");
                }
            }
        });
        dialog.show(getSupportFragmentManager(), "CREATE CATEGORY");
    }

    private void promptAddToCategory() {
        AddToCategoryDialog dialog = new AddToCategoryDialog(userCategories);
        dialog.setAddToCategoryListener(new AddToCategoryListener() {
            @Override
            public void onAddToCategory(Category category) {
                // Update the current cardset and set it's category here
            }
        });
        dialog.show(getSupportFragmentManager(), "ADD TO CATEGORY");
    }

    private void promptSearchWord() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.INTERNET}, 3);
        } else {
            // TODO Show a dialog allowing the user to input the word
            WordSearchDialog dialog = new WordSearchDialog();
            dialog.setWordSearchListener(new WordSearchListener() {
                @Override
                public void onWordSearched(String word) {
                    // Start API request to Dictionary API here
                    Log.i("WORD SEARCH", word);
                    APIRequest request = new APIRequest(CreateCardsetActivity.this, word, new APIRequestListener() {
                        @Override
                        public void onRequestSuccess(String jsonData) {
                            // Process the json here
                            Log.i("DICTIONARY API RESULT", jsonData);
                            try {
                                JSONArray jsonArray = new JSONArray(jsonData);
                                JSONObject data = jsonArray.getJSONObject(0);
                                JSONArray meanings = data.getJSONArray("meanings");
                                List<String> definitions = new ArrayList<String>();
                                for (int i = 0; i < meanings.length(); i++) {
                                    JSONObject definitionJson = meanings.getJSONObject(i);
                                    JSONArray definitionArray = definitionJson.getJSONArray("definitions");
                                    JSONObject keyData = definitionArray.getJSONObject(0);
                                    String definition = keyData.getString("definition");
                                    Log.i("DEFINITION", definition);
                                    definitions.add(definition);
                                }
                                SearchResultDialog searchResultDialog = new SearchResultDialog(word, definitions);
                                searchResultDialog.setSelectionListener(new DefinitionSelectionListener() {
                                    @Override
                                    public void onDefinitionSelected(String word, String definition) {
                                        Log.i("SELECTED DEFINITION", definition);
                                        // TODO Create a new Card based on the searched word and selected definition
                                        if (definition == null) {
                                            Toast.makeText(CreateCardsetActivity.this, "No definition was selected. Exiting...", Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                        Card newCard = new Card(word, definition, user_id, cardSet_id);
                                        cards.add(newCard);
                                        cardsetRecyclerAdapter.updateDataset(cards);
                                        DatabaseHelper.insertCard(newCard, new InsertCardCallback() {
                                            @Override
                                            public void onCreateCardSuccess() {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CreateCardsetActivity.this, "Successfully created a new card from the searched word", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                            @Override
                                            public void onCreateCardFailed() {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CreateCardsetActivity.this, "An error occurred creating the card. Please try again.", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                                searchResultDialog.show(getSupportFragmentManager(), "SEARCH RESULTS");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onRequestFailed(String errorMessage) {
                            Toast.makeText(CreateCardsetActivity.this, "An error occurred while searching for the word definition", Toast.LENGTH_LONG).show();
                        }
                    });
                    request.startRequest();
                }
            });
            dialog.show(getSupportFragmentManager(), "WORD_SEARCH");
        }
    }

    private void populateSpinner() {
        if (userCategories != null) {
            List<String> selections = new ArrayList<String>();
            selections.add("Select a category...");
            for (Category category : userCategories) {
                selections.add(category.toString());
            }

            categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, selections);
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categorySelections.setAdapter(categoryAdapter);
            if (isExistingCardset) {
                DatabaseHelper.fetchExistingCardsetCategory(user_id, cardsetName, new CardsetCategoryListener() {
                    @Override
                    public void onCategoryFetch(String category_name) {
                        if (category_name != null) {
                            categorySelections.setSelection(selections.indexOf(category_name));
                        } else {
                            categorySelections.setSelection(0);
                        }
                    }
                });
            }
        } else {
            Toast.makeText(this, "No existing categories found. Consider organizing cardsets into categories!", Toast.LENGTH_LONG).show();
            categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new ArrayList<String>());
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }

        categorySelections.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String designatedCategoryName;
                if (position > 0) {
                    designatedCategoryName = (userCategories.isEmpty()) ? null : userCategories.get(position - 1).name;
                } else {
                    designatedCategoryName = categoryAdapter.getItem(0);
                }

                //String designatedCategoryName = userCategories.get(position - 1).name;
                if (cardsetName != null) {
                    DatabaseHelper.moveCardsetToCategory(user_id, cardsetName, designatedCategoryName, new MoveCategoryListener() {
                        @Override
                        public void onMoveSuccess() {
                            Log.i("MOVE CATEGORY", "SUCCESS");
                        }
                        @Override
                        public void onMoveFailed() {
                            Log.i("MOVE CATEGORY", "FAILED");
                        }
                    });
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void promptDeleteCardset() {
        new AlertDialog.Builder(CreateCardsetActivity.this)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCurrentCardset();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                })
                .setTitle("Delete Cardset")
                .setMessage("Are you sure you want to delete this cardset?")
                .create().show();
    }

    private void promptDeleteCategory() {
        DeleteCategoryDialog dialog = new DeleteCategoryDialog(userCategories, new DeleteCategoryListener() {
            @Override
            public void onDeleteCategory(List<Category> categoriesToRemove) {
                // Perform the needed operations for removing the categories here (from database as well)
                for (Category category : categoriesToRemove) {
                    Log.i("REMOVE CATEGORY", category.toString());
                    DatabaseHelper.deleteCategory(user_id, category);
                    userCategories.remove(category);
                    categoryAdapter.remove(category.toString());
                }
                categoryAdapter.notifyDataSetChanged();
            }
        });
        dialog.show(getSupportFragmentManager(), "DELETE CATEGORY DIALOG");
    }

    private void deleteCurrentCardset() {
        DatabaseHelper.deleteCardset(cardSet_id, new DeleteCardsetListener() {
            @Override
            public void onDeleteSuccess() {
                Log.i("DELETE CARDSET", "SUCCESS");
                Toast.makeText(CreateCardsetActivity.this, "Delete cardset was a success", Toast.LENGTH_LONG).show();
                CreateCardsetActivity.this.finish();
            }

            @Override
            public void onDeleteFailed() {
                Log.i("DELETE CARDSET", "FAILED");
                Toast.makeText(CreateCardsetActivity.this, "Failed to delete cardset. Please try again", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showOptionsMenu(View anchor) {
        PopupMenu menu = new PopupMenu(this, anchor);
        menu.getMenuInflater().inflate(R.menu.cardset_options, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.createCategory:
                        // Prompt dialog for creating a new category
                        promptCategoryInfo();
                        return true;
                    case R.id.addToCategory:
                        promptAddToCategory();
                        // Prompt dialog for adding to an existing category
                        return true;
                    case R.id.deleteCardset:
                        promptDeleteCardset();
                        return true;
                    case R.id.deleteCategory:
                        promptDeleteCategory();
                        return true;
                    case R.id.searchWord:
                        promptSearchWord();
                        return true;
                    default:
                        return false;
                }
            }
        });
        menu.show();
    }

    @Override
    public void onCardLongClick(View anchor, Card card) {
        if (card != null) {
            currentSelection = card;
        }
        PopupMenu menu = new PopupMenu(this, anchor);
        menu.getMenuInflater().inflate(R.menu.flashcard_menu, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.card_menu_edit:
                        // Implement the edit flashcard here
                        promptCardInfo(true);
                        return true;
                    case R.id.card_menu_delete:
                        // Implement the delete flashcard here
                        deleteCard(card);
                        return true;
                }
                return false;
            }
        });
        menu.show();
    }

    // Method to update a card in the database and RecyclerView
    private void updateCard(Card card, String newName, String newDefinition) {
        DatabaseHelper.updateCard(newName, newDefinition, card.user_id, card.cardset_id, card.card_id);

        card.cardName = newName;
        card.cardDefinition = newDefinition;
        cardsetRecyclerAdapter.updateDataset(cards);
    }

    // Method to delete a card in the database and RecyclerView
    private void deleteCard(Card card) {
        // Deleting the card from the database
        DatabaseHelper.deleteCard(card);

        // Deleting the card from the RecyclerView
        cards.remove(card);
        cardsetRecyclerAdapter.updateDataset(cards);
        Toast.makeText(this, "Successfully deleted selected card", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_cardset_activity);
        user_id = getIntent().getIntExtra("user_id", -1);
        cardSet_id = getIntent().getIntExtra("cardset_id", -1);
        String cardsJson = getIntent().getStringExtra("cards");

        setFragmentListeners();

        // If cardSet_id > 0, then this is an existing CardSet, so do not prompt the user for a CardSet name
        if (cardSet_id < 0) {
            promptSetName();
        } else {
            cardsetName = getIntent().getStringExtra("cardset_name");
            isExistingCardset = true;
        }
        if (cardsJson != null && !cardsJson.equals("null")) {
            cards = CardsetHelper.CardsFromJson(cardsJson);
            for (Card card: cards) {
                Log.i("CARD", card.toString());
            }
            cardsetName = getIntent().getStringExtra("cardset_name");
        }
        fetchCategories();
        setViewReferences();
        setViewHandlers();
    }
}
