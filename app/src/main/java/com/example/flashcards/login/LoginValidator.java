package com.example.flashcards.login;

import android.os.AsyncTask;

import com.example.flashcards.daos.UserDAO;
import com.example.flashcards.database.AppDatabase;
import com.example.flashcards.database.FlashcardsDatabase;
import com.example.flashcards.entities.User;

import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginValidator {
    private static boolean isValid = false;
    private static Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$", Pattern.CASE_INSENSITIVE);

    public static boolean validateUsername(String username) {
        FlashcardsDatabase db = AppDatabase.getInstance();
        CountDownLatch latch = new CountDownLatch(1);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                UserDAO dao = db.userDAO();
                User existingUser = dao.checkUsernameExists(username);
                if (existingUser == null) {
                    isValid = true;
                }
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return isValid;
    }

    public static boolean isValidEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }
}
