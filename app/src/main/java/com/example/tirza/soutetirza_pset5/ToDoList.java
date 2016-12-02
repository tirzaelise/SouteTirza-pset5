/*
 * Native App Studio: Assignment 5
 * Many To Do Lists
 * Tirza Soute
 *
 * This file implements the ToDoList object, which is an ArrayList that holds ToDoItems.
 */

package com.example.tirza.soutetirza_pset5;

import java.util.ArrayList;

class ToDoList {
    private String title;
    private ArrayList<ToDoItem> toDoList;

    ToDoList(String title, ArrayList<ToDoItem> toDoList) {
        this.title = title;
        this.toDoList = toDoList;
    }

    String getTitle() {
        return title;
    }

    void setToDoList(ArrayList<ToDoItem> newToDoList) {
        this.toDoList = newToDoList;
    }

    ArrayList<ToDoItem> getToDoList() {
        return toDoList;
    }

    /** Returns the index of an item in a To Do List */
    int getItemIndex(String title) {
        int index = 0;

        for (int i = 0; i < toDoList.size(); i++) {
            ToDoItem item = toDoList.get(i);
            if (item.getTitle().equals(title)) index = i;
        }
        return index;
    }
}
