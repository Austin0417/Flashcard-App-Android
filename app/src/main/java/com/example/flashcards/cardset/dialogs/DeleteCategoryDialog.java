package com.example.flashcards.cardset.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcards.R;
import com.example.flashcards.cardset.interfaces.CategoryClickListener;
import com.example.flashcards.cardset.interfaces.DeleteCategoryListener;
import com.example.flashcards.cardset.recycleradapter.DeleteCategoryAdapter;
import com.example.flashcards.entities.Category;

import java.util.ArrayList;
import java.util.List;

public class DeleteCategoryDialog extends DialogFragment {

    private DeleteCategoryListener callback;
    private EditText searchBar;
    private RecyclerView categoriesList;
    private DeleteCategoryAdapter adapter;

    private List<Category> userCategories;
    private List<Category> selectedCategories = new ArrayList<Category>();

    public DeleteCategoryDialog(List<Category> userCategories, DeleteCategoryListener callback) {
        this.userCategories = userCategories;
        this.callback = callback;
    }
    public DeleteCategoryDialog(List<Category> userCategories) {
        this.userCategories = userCategories;
    }

    public void setDeleteCategoryListener(DeleteCategoryListener callback) { this.callback = callback; }
    public void initializeRecyclerAdapter() {
        adapter = new DeleteCategoryAdapter(userCategories, new CategoryClickListener() {
            @Override
            public void onCategorySelected(Category category) {
                selectedCategories.add(category);
            }
            @Override
            public void onCategoryUnselected(Category category) {
                selectedCategories.remove(category);
            }
        });
        categoriesList.setLayoutManager(new LinearLayoutManager(getContext()));
        categoriesList.setAdapter(adapter);
    }
    private void initializeSearchBar() {
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String latestText = searchBar.getText().toString();
                Log.i("SEARCH TEXT CHANGE", latestText);
                List<Category> filteredCategories = new ArrayList<Category>();
                for (Category category : userCategories) {
                    if (category.name.contains(latestText)) {
                        filteredCategories.add(category);
                    }
                }
                adapter.updateDataset(filteredCategories);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    public void setDialogViewReferences(View dialogView) {
        searchBar = dialogView.findViewById(R.id.delete_searchCategory);
        categoriesList = dialogView.findViewById(R.id.deleteCategoryList);
        if (searchBar != null && categoriesList != null && userCategories != null) {
            // Initialize the RecyclerView's dataset and adapter here
            initializeRecyclerAdapter();
            initializeSearchBar();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.delete_category, null, false);
        setDialogViewReferences(dialogView);
        return new AlertDialog.Builder(getContext())
                .setTitle("Delete a Category")
                .setView(dialogView)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onDeleteCategory(selectedCategories);
                    }
                })
                .create();

    }
}
