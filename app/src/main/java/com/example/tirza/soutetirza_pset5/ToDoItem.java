/*
 * Native App Studio: Assignment 5
 * Many To Do Lists
 * Tirza Soute
 *
 * This class implements the To Do Item object.
 */

package com.example.tirza.soutetirza_pset5;

class ToDoItem {
    private String title;
    private int completed;

    ToDoItem(String title, int completed) {
        this.title = title;
        this.completed = completed;
    }

    String getTitle() {
        return title;
    }

    int getCompleted() {
        return completed;
    }

    void setCompleted(int status) {
        this.completed = status;
    }
}