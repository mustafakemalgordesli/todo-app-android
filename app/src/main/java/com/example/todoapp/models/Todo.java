package com.example.todoapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Todo {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "todo")
    public String todo;


    public Todo(String todo) {
        this.todo = todo;
    }
}
