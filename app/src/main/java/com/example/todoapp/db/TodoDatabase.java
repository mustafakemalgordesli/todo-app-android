package com.example.todoapp.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.todoapp.models.Todo;

@Database(entities = {Todo.class}, version = 1)
public abstract class TodoDatabase extends RoomDatabase {
    public abstract TodoDao todoDao();
}