package com.example.flashcards.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "Cards", foreignKeys = { @ForeignKey(entity= User.class, parentColumns="id", childColumns = "user_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
                                             @ForeignKey(entity= CardSet.class, parentColumns = "cardset_id", childColumns = "cardset_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)})
public class Card {
    @PrimaryKey(autoGenerate = true)
    public int card_id;

    @ColumnInfo
    public int user_id;

    @ColumnInfo
    public int cardset_id;

    @ColumnInfo
    public String cardName;

    @ColumnInfo
    public String cardDefinition;

    public Card() {}

    public Card(String cardName, String cardDefinition, int user_id, int cardSet_id) {
        this.cardName = cardName;
        this.cardDefinition = cardDefinition;
        this.user_id = user_id;
        this.cardset_id = cardSet_id;
    }

    @Override
    public String toString() {
        return "{card_id: " + card_id + ", user_id: " + user_id + ", cardset_id: " + cardset_id + ", name: " + cardName + ", definition: " + cardDefinition + "}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(card_id, user_id, cardset_id, cardName, cardDefinition);
    }

    @Override
    public boolean equals(Object o) {
        Card compare = (Card) o;
        return card_id == compare.card_id && user_id == compare.user_id && cardset_id == compare.cardset_id && cardName.equals(compare.cardName) && cardDefinition.equals(compare.cardDefinition);
    }
}
