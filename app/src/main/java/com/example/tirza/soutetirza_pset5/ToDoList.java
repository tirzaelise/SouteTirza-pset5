/*
 * Native App Studio: Assignment 5
 * Many To Do Lists
 * Tirza Soute
 *
 * This file implements the ToDoList object, which is an ArrayList that holds ToDoItems.
 */

package com.example.tirza.soutetirza_pset5;

import java.util.ArrayList;

public class ToDoList {
    private String title;
    private ArrayList<ToDoItem> toDoList;

    /** Creates ToDoList constructor */
    public ToDoList(String title, ArrayList<ToDoItem> toDoList) {
        this.title = title;
        this.toDoList = toDoList;
    }

    /** Sets the title of the To Do List*/
    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    /** Gets the title of the To Do List */
    public String getTitle() {
        return title;
    }

    /** Sets the To Do List*/
    public void setToDoList(ArrayList<ToDoItem> newToDoList) {
        this.toDoList = newToDoList;
    }

    /** Gets the To Do List*/
    public ArrayList<ToDoItem> getToDoList() {
        return toDoList;
    }

    /** Returns the index of an item in a list*/
    public int getItemIndex(String title) {
        int index = 0;

        for (int i = 0; i < toDoList.size(); i++) {
            ToDoItem item = toDoList.get(i);
            if (item.getTitle().equals(title)) index = i;
        }
        return index;
    }

    public void getItem(String title) {
        ToDoItem item;

        for (int i = 0; i < toDoList.size(); i++) {
            item = toDoList.get(i);
//            if (item.getTitle().equals(title))
        }
//        return item;
    }
}
