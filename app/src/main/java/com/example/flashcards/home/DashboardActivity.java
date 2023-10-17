package com.example.flashcards.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.flashcards.R;
import com.example.flashcards.cardset.CreateCardsetActivity;
import com.example.flashcards.cardset.ViewCardsetActivity;

public class DashboardActivity extends AppCompatActivity {

    private TextView welcomeText;
    private int user_id;
    private String username;

    private Button createCardsetBtn;
    private Button viewCardsetBtn;

    private void setViewReferences() {
        welcomeText = findViewById(R.id.welcomeText);
        createCardsetBtn = findViewById(R.id.createCardSetBtn);
        viewCardsetBtn = findViewById(R.id.viewCardSetBtn);

        user_id = getIntent().getIntExtra("user_id", -1);
        username = getIntent().getStringExtra("username");

        if (username != null) {
            welcomeText.setText("Welcome back, " + username);
        }
        Log.i("DASHBOARD", "User id: " + user_id);
    }

    private void setViewHandlers() {
        createCardsetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCreateCardsetActivity();
            }
        });
        viewCardsetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchViewCardsetActivity();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        setViewReferences();
        setViewHandlers();
    }


    private void launchCreateCardsetActivity() {
        Intent intent = new Intent(this, CreateCardsetActivity.class);
        intent.putExtra("user_id", user_id);
        startActivity(intent);
    }

    private void launchViewCardsetActivity() {
        Intent intent = new Intent(this, ViewCardsetActivity.class);
        intent.putExtra("user_id", user_id);
        startActivity(intent);
    }
}
