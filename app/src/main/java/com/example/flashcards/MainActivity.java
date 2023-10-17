package com.example.flashcards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flashcards.daos.UserDAO;
import com.example.flashcards.database.AppDatabase;
import com.example.flashcards.database.FlashcardsDatabase;
import com.example.flashcards.entities.User;
import com.example.flashcards.helpers.CreateAccountListener;
import com.example.flashcards.home.DashboardActivity;
import com.example.flashcards.login.LoginActivity;
import com.example.flashcards.login.LoginValidator;
import com.google.android.material.textfield.TextInputLayout;


public class MainActivity extends AppCompatActivity {

    private FlashcardsDatabase db;

    private EditText emailInput;
    private EditText usernameInput;
    private EditText passwordInput;
    private TextInputLayout emailLayout;
    private TextInputLayout userLayout;
    private TextInputLayout passwordLayout;

    private Button loginBtn;
    private Button createAccount;


    private void initializeDatabase() {
        AppDatabase database = new AppDatabase(this);   // now we can call AppDatabase.getInstance to obtain a handle
        db = AppDatabase.getInstance();
    }

    private void setViewReferences() {
        emailInput = findViewById(R.id.emailInput);
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginBtn = findViewById(R.id.loginBtn);
        createAccount = findViewById(R.id.createAccountBtn);

        emailLayout = findViewById(R.id.emailLayoutInput);
        userLayout = findViewById(R.id.usernameInputLayout);
        passwordLayout = findViewById(R.id.passwordLayoutInput);
    }

    private void setViewHandlers() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchLoginPage();
            }
        });
        usernameInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String username = usernameInput.getText().toString();
                if (!hasFocus && !LoginValidator.validateUsername(username)) {
                    userLayout.setError("Invalid username/username is already taken");
                } else {
                    userLayout.setError(null);
                }
            }
        });
        emailInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String email = emailInput.getText().toString();
                if (!hasFocus && !LoginValidator.isValidEmail(email)) {
                    emailLayout.setError("Invalid email address format");
                } else {
                    emailLayout.setError(null);
                }
            }
        });

        CreateAccountListener listener = new CreateAccountListener() {
            @Override
            public void onCreateAccount(User user) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        UserDAO dao = db.userDAO();
                        int user_id = dao.getUserId(user.getEmail(), user.getUsername(), user.getPassword());
                        user.setId(user_id);
                        launchHomePage(user);
                    }
                });
            }
        };
        usernameInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    createAccount(listener);
                }
                return true;
            }
        });
        emailInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    createAccount(listener);
                }
                return true;
            }
        });
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(listener);
            }
        });
    }

    // Called after a successful account creation or login
    private void launchHomePage(User user) {
        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
        Log.i("CREATE USER", "" + user.getId());
        intent.putExtra("user_id", user.getId());
        intent.putExtra("username", user.getUsername());
        startActivity(intent);
    }

    // When the user clicks the login button
    private void launchLoginPage() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void createAccount(CreateAccountListener listener) {
        if (emailInput.getText().toString().isEmpty() ||
            usernameInput.getText().toString().isEmpty() ||
            passwordInput.getText().toString().isEmpty()) {
            Toast.makeText(this, "One or more input fields are empty", Toast.LENGTH_LONG).show();
            return;
        } else if (!LoginValidator.isValidEmail(emailInput.getText().toString()) || !LoginValidator.validateUsername(usernameInput.getText().toString())) {
            Toast.makeText(this, "One or more input fields are invalid", Toast.LENGTH_LONG).show();
            return;
        }
        // Account creation was successful
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                UserDAO dao = db.userDAO();
                User newUser = new User(emailInput.getText().toString(), usernameInput.getText().toString(), passwordInput.getText().toString());
                dao.insert(newUser);
                listener.onCreateAccount(newUser);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Create Account");
        initializeDatabase();
        setViewReferences();
        setViewHandlers();
    }

}