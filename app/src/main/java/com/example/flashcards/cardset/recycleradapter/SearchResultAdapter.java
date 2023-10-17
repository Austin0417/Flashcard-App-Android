package com.example.flashcards.cardset.recycleradapter;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcards.R;
import com.example.flashcards.cardset.interfaces.DefinitionSelectionListener;
import com.example.flashcards.cardset.interfaces.ViewHolderSelectionListener;

import java.util.ArrayList;
import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {
    private List<String> dataset;
    private DefinitionSelectionListener callback;
    private List<ViewHolder> viewHolders = new ArrayList<ViewHolder>();


    public SearchResultAdapter(List<String> dataset) {
        this.dataset = dataset;
    }
    public SearchResultAdapter(List<String> dataset, DefinitionSelectionListener callback) {
        this.dataset = dataset;
        this.callback = callback;
    }
    public void setSelectionListener(DefinitionSelectionListener callback) { this.callback = callback; }
    public void updateDataset(List<String> dataset) {
        this.dataset = dataset;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        viewHolders.add(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView definitionText = holder.getTextView();
        definitionText.setText(dataset.get(position));

        definitionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // User selects a definition/chose a different definition from their previous choice
                if (!holder.selected()) {
                    callback.onDefinitionSelected(null, definitionText.getText().toString());

                // User clicks the currently selected definition, unselect it
                } else {
                    callback.onDefinitionSelected(null, null);
                }
                for (ViewHolder viewHolder : viewHolders) {
                    if (!viewHolder.equals(holder)) {
                        viewHolder.setSelected(false);
                        ViewHolderSelectionListener callback = ((ViewHolderSelectionListener) viewHolder);
                        callback.onSelectionStatusChanged();
                    }
                }
                holder.setSelected(!holder.selected());
                ((ViewHolderSelectionListener) holder).onSelectionStatusChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements ViewHolderSelectionListener {
        private TextView definitionText;
        private boolean isSelected = false;

        @Override
        public void onSelectionStatusChanged() {
            if (isSelected) {
                // Change the text color to indicate this is the current selection
                definitionText.setBackgroundColor(Color.WHITE);
            } else {
                // Change/remain as default text color
                definitionText.setBackgroundColor(Color.TRANSPARENT);
            }
        }

        public ViewHolder(View v) {
            super(v);
            definitionText = v.findViewById(R.id.definition);
        }
        public TextView getTextView() { return definitionText; }
        public boolean selected() { return isSelected; }
        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }
}
