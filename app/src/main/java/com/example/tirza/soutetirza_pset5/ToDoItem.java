/*
 * Native App Studio: Assignment 5
 * Many To Do Lists
 * Tirza Soute
 *
 * This class implements the To Do Item object.
 */

package com.example.tirza.soutetirza_pset5;

public class ToDoItem {
    private String title;
    private int completed;

    public ToDoItem(String title, int completed) {
        this.title = title;
        this.completed = completed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int status) {
        this.completed = status;
    }
}