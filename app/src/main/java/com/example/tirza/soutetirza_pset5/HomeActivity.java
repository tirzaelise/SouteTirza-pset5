/*
 * Native App Studio: Assignment 5
 * Many To Do Lists
 * Tirza Soute
 *
 * This file shows the user what To Do Lists he/she has.
 */

package com.example.tirza.soutetirza_pset5;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private ToDoManager toDoManager;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toDoManager = ToDoManager.getInstance();

        toDoManager.readToDoLists(this);

        final ListView toDoLists = (ListView) findViewById(R.id.toDoListTitles);
        setListView(toDoLists);

        // Set OnItemLongClickListener so that the user can go to the To Do List that was clicked
        toDoLists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedList = toDoLists.getItemAtPosition(position).toString();
                Intent goToListActivity = new Intent(view.getContext(), ListActivity.class);
                goToListActivity.putExtra("clickedList", clickedList);
                startActivity(goToListActivity);
            }
        });
    }

    /** Sets the list view so that the user can see the titles of his To Do Lists */
    public void setListView(ListView listView) {
        ArrayList<String> toDoListNames = getAllListTitles();
        adapter = new ArrayAdapter<>(this, R.layout.layout_row, R.id.listViewItem, toDoListNames);
        listView.setAdapter(adapter);
    }

    /** Creates an ArrayList<String> that holds the title of every To Do List*/
    private ArrayList<String> getAllListTitles() {
        ArrayList<ToDoList> toDoLists = toDoManager.getToDoManager();
        ArrayList<String> toDoListNames = new ArrayList<>();
        String title;

        for (int i = 0; i < toDoLists.size(); i++) {
            title = toDoLists.get(i).getTitle();
            toDoListNames.add(title);
        }
        return toDoListNames;
    }

    /** Adds a new list to the list of To Do Lists */
    public void addList(View view) {
        EditText newListEt = (EditText) findViewById(R.id.listToAddEt);
        String listName = newListEt.getText().toString();

        if (!(listName.equals(""))) {
            // Force shut down keyboard and remove the String that was typed
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(newListEt.getWindowToken(), 0);
            newListEt.setText("");

            ArrayList<ToDoItem> tasks = new ArrayList<>();
            ToDoList newToDoList = new ToDoList(listName, tasks);
            ArrayList<ToDoList> toDoLists = toDoManager.getToDoManager();
            toDoLists.add(newToDoList);
            toDoManager.setToDoManager(toDoLists);

            adapter.add(listName);
            adapter.notifyDataSetChanged();
            toDoManager.writeToDoList(this);
        } else {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
        }
    }

    /** Removes a list from the To Do Lists */
    public void remove(View view) {
        ArrayList<ToDoList> toDoLists = toDoManager.getToDoManager();
        RelativeLayout layout = (RelativeLayout) view.getParent();
        TextView textView = (TextView) layout.getChildAt(1);
        String selectedList = textView.getText().toString();

        int index = toDoManager.getListIndex(selectedList);
        toDoLists.remove(index);
        toDoManager.setToDoManager(toDoLists);
        toDoManager.writeToDoList(this);

        adapter.remove(selectedList);
        adapter.notifyDataSetChanged();
    }
}
