package com.example.flashcards.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.flashcards.entities.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert
    public void insert(User user);

    @Delete
    public void delete(User user);

    @Query("SELECT * FROM Users WHERE id=:id LIMIT 1")
    public User getUserById(int id);

    @Query("SELECT * FROM Users")
    public List<User> getUsers();

    @Query("SELECT * FROM Users WHERE (username=:nameEmail OR email=:nameEmail) AND password=:password LIMIT 1")
    public User login(String nameEmail, String password);

    @Query("SELECT * FROM Users WHERE username=:username LIMIT 1")
    public User checkUsernameExists(String username);

    @Query("SELECT id FROM Users WHERE email=:email AND username=:username AND password=:password")
    public int getUserId(String email, String username, String password);

}
