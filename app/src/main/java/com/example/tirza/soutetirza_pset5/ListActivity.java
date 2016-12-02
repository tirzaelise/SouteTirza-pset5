/*
 * Native App Studio: Assignment 5
 * Many To Do Lists
 * Tirza Soute
 *
 * This file shows the user the to do list that he clicked on.
 */

package com.example.tirza.soutetirza_pset5;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    private ToDoManager toDoManager;
    private ArrayList<ToDoItem> currentList;
    private ToDoList currentToDoList;
    private String clickedList;
    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        toDoManager = ToDoManager.getInstance();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            clickedList = extras.getString("clickedList");
            TextView titleView = (TextView) findViewById(R.id.listTitle);
            titleView.setText(clickedList);
            titleView.setGravity(Gravity.CENTER_HORIZONTAL);

            getClickedList();
            toDoManager.readToDoLists(this);
            final ListView toDoList = (ListView) findViewById(R.id.toDoLists);
            setListView(toDoList);

            // Set OnItemClickListener so that items can be checked off
            toDoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String clickedItem = toDoList.getItemAtPosition(position).toString();
                    TextView clickedTextView = (TextView) view.findViewById(R.id.listViewItem);
                    int index = currentToDoList.getItemIndex(clickedItem);
                    ToDoItem selectedItem = currentList.get(index);
                    int completed = selectedItem.getCompleted();

                    if (completed == 1) {
                        selectedItem.setCompleted(0);
                        // Revert the text colour back to the original grey colour
                        clickedTextView.setTextColor(Color.parseColor("#808080"));
                    } else {
                        selectedItem.setCompleted(1);
                        // Set the text colour to green
                        clickedTextView.setTextColor(Color.parseColor("#006400"));
                    }

                    currentList.set(index, selectedItem);
                    currentToDoList.setToDoList(currentList);
                    int listIndex = toDoManager.getListIndex(clickedList);
                    ArrayList<ToDoList> toDoLists = toDoManager.getToDoManager();
                    toDoLists.set(listIndex, currentToDoList);
                    toDoManager.setToDoManager(toDoLists);
                }
            });
        }
    }

    /** Sets the list view so that the user can see the titles of his To Do Lists */
    public void setListView(ListView listView) {
        ArrayList<String> toDoListNames = getAllTaskTitles();
        adapter = new CustomAdapter(this, R.layout.layout_row, R.id.listViewItem, toDoListNames,
                                    currentToDoList);
        listView.setAdapter(adapter);
    }

    /** Gets the clicked To Do List */
    private void getClickedList() {
        ArrayList<ToDoList> toDoLists = toDoManager.getToDoManager();

        for (int i = 0; i < toDoLists.size(); i++) {
            currentToDoList = toDoLists.get(i);
            String title = currentToDoList.getTitle();

            if (title.equals(clickedList)) {
                currentList = currentToDoList.getToDoList();
                break;
            }
        }
    }

    /** Gets the titles of the tasks in the To Do List */
    private ArrayList<String> getAllTaskTitles() {
        ArrayList<String> taskTitles = new ArrayList<>();
        ToDoItem item;
        String title;

        for (int i = 0; i < currentList.size(); i++) {
            item = currentList.get(i);
            title = item.getTitle();
            taskTitles.add(title);
        }
        return taskTitles;
    }

    /** Adds a task to the To Do List */
    public void addTask(View view) {
        EditText newTaskEt = (EditText) findViewById(R.id.taskToAddEt);
        String newTask = newTaskEt.getText().toString();

        if (!newTask.equals("")) {
            ToDoItem newToDoItem = new ToDoItem(newTask, 0);
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(newTaskEt.getWindowToken(), 0);
            newTaskEt.setText("");

            currentList.add(newToDoItem);
            int index = toDoManager.getListIndex(clickedList);
            ArrayList<ToDoList> toDoLists = toDoManager.getToDoManager();

            currentToDoList.setToDoList(currentList);
            toDoLists.set(index, currentToDoList);
            toDoManager.setToDoManager(toDoLists);
            toDoManager.writeToDoList(this);

            adapter.add(newTask);
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Please enter an item", Toast.LENGTH_SHORT).show();
        }
    }

    /** Removes an item from the To Do List */
    public void remove(View view) {
        ArrayList<ToDoList> toDoLists = toDoManager.getToDoManager();
        RelativeLayout layout = (RelativeLayout) view.getParent();
        TextView textView = (TextView) layout.getChildAt(1);
        String selectedItem = textView.getText().toString();

        int index = currentToDoList.getItemIndex(selectedItem);
        currentList.remove(index);
        currentToDoList.setToDoList(currentList);
        int listIndex = toDoManager.getListIndex(clickedList);
        toDoLists.set(listIndex, currentToDoList);
        toDoManager.setToDoManager(toDoLists);
        toDoManager.writeToDoList(this);

        adapter.remove(selectedItem);
        adapter.notifyDataSetChanged();
    }
}