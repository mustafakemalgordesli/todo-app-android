package com.example.todoapp.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.todoapp.MainActivity;
import com.example.todoapp.databinding.RecyclerRowBinding;
import com.example.todoapp.db.TodoDao;
import com.example.todoapp.db.TodoDatabase;
import com.example.todoapp.models.Todo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoHolder> {

    ArrayList<Todo> todoArrayList;
    TodoDatabase db;
    TodoDao todoDao;
    private final CompositeDisposable compositeDisposable;

    public TodoAdapter(ArrayList<Todo> todoArrayList, TodoDatabase db) {

        this.todoArrayList = todoArrayList;
        this.db = db;
        todoDao = this.db.todoDao();
        compositeDisposable = new CompositeDisposable();
    }

    public void onDestroy() {
        compositeDisposable.clear();
    }

    @NonNull
    @Override
    public TodoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding recyclerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TodoHolder(recyclerRowBinding);
    }

    public void updateTodosList(ArrayList<Todo> newlist) {
        todoArrayList = newlist;
        this.notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull TodoHolder holder, int position) {
        int isPosition = holder.getPosition();
        holder.binding.recyclerViewTextView.setText(todoArrayList.get(position).todo);

        holder.binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Todo deletedTodo = todoArrayList.get(isPosition);
                compositeDisposable.add(todoDao.delete(deletedTodo)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe()
                );
                todoArrayList.remove(isPosition);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoArrayList.size();
    }

    public class TodoHolder extends RecyclerView.ViewHolder {

        private RecyclerRowBinding binding;

        public TodoHolder(RecyclerRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
