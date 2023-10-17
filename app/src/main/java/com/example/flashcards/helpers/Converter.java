package com.example.flashcards.helpers;
import androidx.room.TypeConverter;

import com.example.flashcards.entities.Card;
import com.example.flashcards.entities.CardSet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class Converter {
    @TypeConverter
    public static List<Card> cardListfromJsonString(String value) {
        Type listType = new TypeToken<List<Card>>(){}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String cardListtoJsonString(List<Card> cards) {
        Gson gson = new Gson();
        return gson.toJson(cards);
    }

    @TypeConverter
    public static List<CardSet> cardsetFromJsonString(String value) {
        Type listType = new TypeToken<List<CardSet>>(){}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String cardsetToJsonString(List<CardSet> cardSets) {
        Gson gson = new Gson();
        return gson.toJson(cardSets);
    }

}
