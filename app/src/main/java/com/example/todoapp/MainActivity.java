package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.todoapp.adapters.TodoAdapter;
import com.example.todoapp.databinding.ActivityMainBinding;
import com.example.todoapp.db.TodoDao;
import com.example.todoapp.db.TodoDatabase;
import com.example.todoapp.models.Todo;
import com.example.todoapp.views.AddTodo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    ArrayList<Todo> todoArrayList;
    TodoAdapter todoAdapter;
    TodoDatabase db;
    TodoDao todoDao;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        db = Room.databaseBuilder(getApplicationContext(), TodoDatabase.class, "Todos")
                .allowMainThreadQueries()
                .build();
        todoDao = db.todoDao();
        compositeDisposable.add(todoDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(MainActivity.this::handleGetAllResponse)
        );
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.todo_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_todo) {
            Intent intent = new Intent(this, AddTodo.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.exit) {
            finish();
        }


        return super.onOptionsItemSelected(item);
    }*/

    public void addTodo(View view) {
        Todo newTodo = new Todo(binding.editTextTodo.getText().toString());
        //todoDao.insert(newTodo).subscribeOn(Schedulers.io()).subscribe();
        compositeDisposable.add(todoDao.insert(newTodo).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(MainActivity.this::handleInsertResponse)
        );
    }

    private void handleInsertResponse() {
        compositeDisposable.add(todoDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((List<Todo> todoList) -> {
                    binding.editTextTodo.setText("");
                    todoAdapter.updateTodosList(todoArrayList);
                })
        );
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    private void handleGetAllResponse(List<Todo> todoList) {
        todoArrayList = (ArrayList<Todo>) todoList;
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        todoAdapter = new TodoAdapter(todoArrayList, db);
        binding.recyclerView.setAdapter(todoAdapter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        todoAdapter.onDestroy();
    }
}