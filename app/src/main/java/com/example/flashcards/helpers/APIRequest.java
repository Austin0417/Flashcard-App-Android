package com.example.flashcards.helpers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.flashcards.cardset.interfaces.APIRequestListener;

public class APIRequest {
    private static final String ENDPOINT = "https://api.dictionaryapi.dev/api/v2/entries/en/";
    private APIRequestListener callback;
    private String searchWord;
    private Context context;


    public APIRequest(Context context) { this.context = context;}
    public APIRequest(Context context, String searchWord, APIRequestListener callback) {
        this.context = context;
        this.searchWord = searchWord;
        this.callback = callback;
    }
    public void setAPIRequestListener(APIRequestListener callback) { this.callback = callback; }

    public void startRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ENDPOINT + searchWord, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onRequestSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onRequestFailed(error.toString());
            }
        });
        requestQueue.add(stringRequest);
    }
}
