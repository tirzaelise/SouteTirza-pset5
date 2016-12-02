/*
 * Native App Studio: Assignment 5
 * Many To Do Lists
 * Tirza Soute
 *
 * This file manages the to do lists.
 */

package com.example.tirza.soutetirza_pset5;

import java.util.ArrayList;

public class ToDoManager {
    private static ToDoManager toDoManager = new ToDoManager();
    private ArrayList<ToDoList> toDoLists;

    public static ToDoManager getInstance() {
        return toDoManager;
    }

    private ToDoManager() {
        toDoLists = new ArrayList<>();
    }

    public void setToDoManager(ArrayList<ToDoList> toDoLists) {
        this.toDoLists = toDoLists;
    }

    public ArrayList<ToDoList> getToDoManager() {
        return toDoLists;
    }

    public int getListIndex(String title) {
        ToDoList currentList;
        int index = 0;

        for (int i = 0; i < toDoLists.size(); i++) {
            currentList = toDoLists.get(i);
            if (currentList.getTitle().equals(title)) index = i;
        }
        return index;
    }

    public void readToDoLists() {

    }

    public void writeToDoLists() {

    }
}
