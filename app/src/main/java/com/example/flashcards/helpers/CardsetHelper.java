package com.example.flashcards.helpers;

import com.example.flashcards.entities.Card;
import com.example.flashcards.entities.CardSet;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CardsetHelper {

    public static String CardsToJson(List<Card> cards) {
        Gson gson = new Gson();
        return gson.toJson(cards);
    }

    public static List<Card> CardsFromJson(String cardsJson) {
        Type cardListType = new TypeToken<List<Card>>() {}.getType();
        Gson gson = new Gson();
        return gson.fromJson(cardsJson, cardListType);
    }

    public static List<Card> CardsFromJsonManual(String cardsJson) throws JSONException {
        JSONArray array = new JSONArray(cardsJson);
        List<Card> res = new ArrayList<Card>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject cardObject = array.getJSONObject(i);
            String cardName = cardObject.getString("cardName");
            String cardDefinition = cardObject.getString("cardDefinition");
            int card_id = cardObject.getInt("card_id");
            int cardset_id = cardObject.getInt("cardset_id");
            int user_id = cardObject.getInt("user_id");
            Card card = new Card(cardName, cardDefinition, user_id, cardset_id);
            card.card_id = card_id;
            res.add(card);
        }
        return res;
    }
}
