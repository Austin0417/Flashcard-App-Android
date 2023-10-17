package com.example.flashcards.cardset.recycleradapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcards.R;
import com.example.flashcards.entities.CardSet;
import com.example.flashcards.cardset.interfaces.CardsetListener;

import java.util.List;

public class ViewCardsetAdapter extends RecyclerView.Adapter<ViewCardsetAdapter.ViewHolder> {
    private List<CardSet> dataset;

    public ViewCardsetAdapter(List<CardSet> dataset) {
        this.dataset = dataset;
    }

    public void updateDataset(List<CardSet> dataset) {
        this.dataset = dataset;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(View v) {
            super(v);
            cardView = v.findViewById(R.id.cardsetCardView);
        }
        public CardView getCardView() { return cardView; }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cardset, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardSet currentCardSet = dataset.get(position);
        CardView cardView = holder.getCardView();
        TextView cardsetTitle = cardView.findViewById(R.id.cardsetTitle);
        TextView cardsetSize = cardView.findViewById(R.id.cardsetSize);

        // TODO Get the size of the CardSet. Can do this by adding a List<Card> attribute to the CardSet entity (add @Ignore annotation)
        cardsetTitle.setText(currentCardSet.getCardsetName());
        if (currentCardSet.getCards() != null && !currentCardSet.getCards().isEmpty()) {
            cardsetSize.setText(currentCardSet.getCards().size() + " flashcards");
        } else {
            cardsetSize.setText("null");
        }

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CardsetListener) cardView.getContext()).onCardsetClicked(currentCardSet);
            }
        });
        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((CardsetListener) cardView.getContext()).onCardsetLongClick(cardView, dataset.get(holder.getAdapterPosition()));
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }


}
