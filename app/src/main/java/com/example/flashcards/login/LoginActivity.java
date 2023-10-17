package com.example.flashcards.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.flashcards.R;
import com.example.flashcards.daos.UserDAO;
import com.example.flashcards.database.AppDatabase;
import com.example.flashcards.database.FlashcardsDatabase;
import com.example.flashcards.entities.User;
import com.example.flashcards.home.DashboardActivity;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private EditText nameEmailInput;
    private EditText passwordInput;
    private TextInputLayout nameEmailLayout;
    private TextInputLayout passwordLayout;

    private Button loginBtn;

    private void setViewReferences() {
        nameEmailInput = findViewById(R.id.loginActivity_userOrEmail);
        passwordInput = findViewById(R.id.loginActivity_passwordInput);
        nameEmailLayout = findViewById(R.id.loginActivity_userOrEmailLayout);
        passwordLayout = findViewById(R.id.loginActivity_passwordLayout);
        loginBtn = findViewById(R.id.loginActivity_login_btn);
    }


    private void setViewHandlers() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void loginFail() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                nameEmailInput.setText("");
                passwordInput.setText("");
                nameEmailLayout.setError("Invalid credentials!");
            }
        });
    }

    private void launchHomePage(User user) {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.putExtra("user_id", user.getId());
        intent.putExtra("username", user.getUsername());
        startActivity(intent);
    }

    private void login() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                FlashcardsDatabase db = AppDatabase.getInstance();
                UserDAO dao = db.userDAO();
                User userMatch = dao.login(nameEmailInput.getText().toString(), passwordInput.getText().toString());
                if (userMatch != null) {
                    launchHomePage(userMatch);
                } else {
                    loginFail();
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        setViewReferences();
        setViewHandlers();
    }
}
