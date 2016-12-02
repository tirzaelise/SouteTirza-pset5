/*
 * Native App Studio: Assignment 5
 * Many To Do Lists
 * Tirza Soute
 *
 * This file extends the ArrayAdapter<String> to be able to show colours and check items off.
 */


package com.example.tirza.soutetirza_pset5;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class CustomAdapter extends ArrayAdapter<String> {
    private ToDoList toDoList;

    CustomAdapter(Context context, int resource, int textViewResource, ArrayList<String> toDoArray,
                  ToDoList toDoList) {
        super(context, resource, textViewResource, toDoArray);
        this.toDoList = toDoList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view.findViewById(R.id.listViewItem);
        ArrayList<ToDoItem> itemsToDo = toDoList.getToDoList();
        ToDoItem item;
        int completed;

        for (int i = 0; i < itemsToDo.size(); i++) {
            item = itemsToDo.get(i);
            completed = item.getCompleted();
            //TODO: text colour changes back on add and delete
            if (completed == 1) textView.setTextColor(Color.parseColor("#006400"));
            else textView.setTextColor(Color.parseColor("#808080"));
        }
        return view;
    }
}
