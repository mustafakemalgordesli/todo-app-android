package com.example.todoapp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.todoapp.MainActivity;
import com.example.todoapp.databinding.ActivityAddTodoBinding;
import com.example.todoapp.db.TodoDao;
import com.example.todoapp.db.TodoDatabase;
import com.example.todoapp.models.Todo;

public class AddTodo extends AppCompatActivity {


    private ActivityAddTodoBinding binding;

    TodoDatabase db;
    TodoDao todoDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTodoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        db = Room.databaseBuilder(getApplicationContext(), TodoDatabase.class, "Todos")
                .allowMainThreadQueries()
                .build();
        todoDao = db.todoDao();
    }

    public void addTodo(View view) {
        Todo newTodo = new Todo(binding.editTextTextPersonName2.getText().toString());
        todoDao.insert(newTodo);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}