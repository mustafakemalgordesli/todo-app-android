package com.example.todoapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.todoapp.models.Todo;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface TodoDao {

    @Query("SELECT * FROM Todo")
    Flowable<List<Todo>> getAll();

    @Query("SELECT * FROM Todo WHERE id = :todoId")
    Flowable<Todo> getById(int todoId);

    @Insert
    Completable insert(Todo todo);

    @Delete
    Completable delete(Todo todo);
}
