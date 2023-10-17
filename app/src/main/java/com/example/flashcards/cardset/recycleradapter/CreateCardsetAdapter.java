package com.example.flashcards.cardset.recycleradapter;

import android.animation.ObjectAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcards.R;
import com.example.flashcards.cardset.interfaces.CardListener;
import com.example.flashcards.entities.Card;

import java.util.List;

public class CreateCardsetAdapter extends RecyclerView.Adapter<CreateCardsetAdapter.ViewHolder> {
    private List<Card> cards;
    public static int currentViewIndex = 0;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        public boolean shouldShowDefinition = false;

        public ViewHolder(View v) {
            super(v);
            cardView = v.findViewById(R.id.flashcard_card_view);
        }
        public CardView getCardView() { return cardView; }
    }

    public CreateCardsetAdapter(List<Card> cards) { this.cards = cards; }

    public void updateDataset(List<Card> newCards) {
        this.cards = newCards;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View v = inflater.inflate(R.layout.flashcard, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        CardView flashcard = viewHolder.getCardView();
        Card currentCard = cards.get(position);

        ImageButton back = flashcard.findViewById(R.id.flashcardBack);
        ImageButton forward = flashcard.findViewById(R.id.flashcardForward);
        TextView flashcardInfo = flashcard.findViewById(R.id.flashcardInfo);

        flashcardInfo.setText(currentCard.cardName);
        back.setClickable(false);
        back.setAlpha(0.5f);

        flashcardInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Add a flip animation when transitioning between states
                if (!viewHolder.shouldShowDefinition) {
                    flashcardInfo.setText(currentCard.cardDefinition);
                } else {
                    flashcardInfo.setText(currentCard.cardName);
                }
                ObjectAnimator animator = ObjectAnimator.ofFloat(flashcard, "rotationX", 0f, 180f);
                animator.setDuration(500);
                animator.start();
                viewHolder.shouldShowDefinition = !viewHolder.shouldShowDefinition;
            }
        });
        flashcard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((CardListener) flashcard.getContext()).onCardLongClick(flashcard, cards.get(viewHolder.getAdapterPosition()));
                return true;
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO go back in the RecyclerView
                currentViewIndex--;
                if (currentViewIndex <= 0) {
                    currentViewIndex = 0;
                    back.setAlpha(0.5f);
                    back.setClickable(false);
                }
                forward.setAlpha(1f);
                forward.setClickable(true);
            }
        });
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO go forward in the RecyclerView
                currentViewIndex++;
                if (currentViewIndex >= cards.size() - 1) {
                    currentViewIndex = cards.size() - 1;
                    forward.setAlpha(0.5f);
                    forward.setClickable(false);
                }
                back.setAlpha(1f);
                back.setClickable(true);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (cards != null) {
            return cards.size();
        } else {
            return 0;
        }
    }
}
