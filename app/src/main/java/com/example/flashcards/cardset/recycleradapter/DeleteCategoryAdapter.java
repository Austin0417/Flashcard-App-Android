package com.example.flashcards.cardset.recycleradapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcards.R;
import com.example.flashcards.cardset.interfaces.CategoryClickListener;
import com.example.flashcards.entities.Category;

import java.util.List;

public class DeleteCategoryAdapter extends RecyclerView.Adapter<DeleteCategoryAdapter.ViewHolder>{
    private List<Category> dataset;
    private CategoryClickListener callback;

    public DeleteCategoryAdapter(List<Category> dataset, CategoryClickListener callback) {
        this.dataset = dataset;
        this.callback = callback;
    }
    public void updateDataset(List<Category> dataset) {
        this.dataset = dataset;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        public boolean isSelected = false;

        public ViewHolder(View v) {
            super(v);
            cardView = v.findViewById(R.id.delete_category_card_view);
        }
        public CardView getCardView() { return cardView; }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category, null, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Category category = dataset.get(position);

        CardView categoryCard = viewHolder.getCardView();
        TextView categoryName = categoryCard.findViewById(R.id.card_category_title);
        ImageView checkBox = categoryCard.findViewById(R.id.categorySelectBox);
        categoryName.setText(category.name);

        categoryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.isSelected) {
                    // Uncheck the ImageView
                    checkBox.setImageResource(android.R.drawable.checkbox_off_background);
                    callback.onCategoryUnselected(category);
                } else {
                    // Check the ImageView
                    checkBox.setImageResource(android.R.drawable.checkbox_on_background);
                    callback.onCategorySelected(category);
                }
                viewHolder.isSelected = !viewHolder.isSelected;
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
