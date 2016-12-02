package com.example.vfrin.valentijnfrinkingpset5;

import java.util.ArrayList;

/**
 * Created by vfrin on 2-12-2016.
 */

public class TodoList {
    private String title;
    private ArrayList<TodoItem> itemList;

    public TodoList(String title) {
        this.title = title;
        this.itemList = new ArrayList<>();
    }

    //    Returns the title of this TodoList
    public String getTitle() {
        return this.title;
    }

    //    Returns the TodoItems of this TodoList
    public ArrayList<TodoItem> getTodoItems() {
        return this.itemList;
    }

    //    Adds a new todoItem to this TodoList
    public void addTodoItem (TodoItem item) {
        itemList.add(item);
    }

    public void removeItem (int position) {
        itemList.remove(position);
    }
}