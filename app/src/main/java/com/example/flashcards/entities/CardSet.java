package com.example.flashcards.entities;


import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(tableName = "Cardsets", foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "id",
        childColumns = "user_id",
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE))
public class CardSet {
    @PrimaryKey(autoGenerate = true)
    public int cardset_id;

    @ColumnInfo
    public String cardset_name;

    @ColumnInfo
    public int user_id;

    @ColumnInfo
    @Nullable
    public String category_name = null;

    @Ignore
    public List<Card> cards;

    public CardSet() {}

    public CardSet(String cardset_name, int user_id) {
        this.cardset_name = cardset_name;
        this.user_id = user_id;
    }

    public CardSet(String cardset_name, int user_id, String category_name) {
        this.cardset_name = cardset_name;
        this.user_id = user_id;
        this.category_name = category_name;
    }

    public int getCardsetId() { return cardset_id; }
    public String getCardsetName() { return cardset_name; }
    public int getUserId() { return user_id; }
    public List<Card> getCards() { return cards; }
    public String getCategoryName() { return category_name; }

    public void addCard(Card card) {
        cards.add(card);
    }
    public void setCards(List<Card> cards) { this.cards = cards; }
    public void setCategoryName(String category_name) { this.category_name = category_name; }


    @Override
    public int hashCode() {
        return Objects.hash(cardset_id, cardset_name, user_id, cards);
    }

    @Override
    public boolean equals(Object o) {
        CardSet compared = (CardSet) o;
        return cardset_id == compared.cardset_id && cardset_name.equals(compared.cardset_name) && user_id == compared.user_id && cards.equals(compared.cards);
    }
}
