package com.example.flashcards.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName="Categories", foreignKeys = @ForeignKey(entity = User.class, parentColumns = "id",
childColumns="user_id",
onDelete = ForeignKey.CASCADE,
onUpdate = ForeignKey.CASCADE))
public class Category {
    @PrimaryKey
    @NonNull
    public String name;

    @ColumnInfo
    public int user_id;

    public Category() {}
    public Category(String name, int user_id) {
        this.name = name;
        this.user_id = user_id;
    }

    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        Category compared = (Category) o;
        return name.equals(compared.name) && user_id == compared.user_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, user_id);
    }
}
