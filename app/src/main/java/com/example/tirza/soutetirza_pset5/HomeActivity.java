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
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class HomeActivity extends AppCompatActivity {
    final static String FILENAME = "lists.txt";
    private ToDoManager toDoManager;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toDoManager = ToDoManager.getInstance();

        readToDoLists();

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
            writeToDoList();
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

        adapter.remove(selectedList);
        adapter.notifyDataSetChanged();
    }

    /** Writes to the file to save the new To Do List */
    private void writeToDoList() {
        try {
            String outputString = createManagerString();
            FileOutputStream outputStream = openFileOutput(FILENAME, MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(outputStream);
            outputWriter.write(outputString);
            outputWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Creates a string that holds all the information about the user's To Do Lists*/
    private String createManagerString() {
        ArrayList<ToDoList> toDoLists = toDoManager.getToDoManager();
        String outputString = "";

        for (int i = 0; i < toDoLists.size(); i++) {
            ToDoList toDoList = toDoLists.get(i);
            String listName = toDoList.getTitle();
            outputString = outputString + "<newList>" + listName;
            ArrayList<ToDoItem> arrayList = toDoList.getToDoList();

            for (int j = 0; j < arrayList.size(); j++) {
                ToDoItem item = arrayList.get(j);
                String itemName = item.getTitle();
                int completed = item.getCompleted();

                outputString = outputString + "<newItem>" + itemName + "<status>" + completed;
            }
        }
        return outputString;
    }

    /** Reads the file that the To Do Lists were written to */
    private void readToDoLists() {
        ArrayList<ToDoList> toDoLists = new ArrayList<>();

        try {
            FileInputStream inputStream = openFileInput(FILENAME);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;

                while ((line = bufferedReader.readLine()) != null) {

                    // Get the different To Do Lists in an array
                    ArrayList<String> lists = new ArrayList<>(Arrays.asList(line.split("<newList>")));
                    lists.remove(0);
                    for (String list : lists) {
                        ArrayList<ToDoItem> newToDoList = new ArrayList<>();

                        // Get the different items of one To Do List in an array
                        ArrayList<String> items = new ArrayList<>(Arrays.asList(list.split("<newItem>")));
                        String listName = items.get(0);
                        items.remove(0);
                        if (items.size() > 1) {
                            for (String item : items) {
                                String[] fields = item.split("<status>");
                                String itemName = fields[0];
                                int itemCompleted = Integer.valueOf(fields[1]);

                                ToDoItem newItem = new ToDoItem(itemName, itemCompleted);
                                newToDoList.add(newItem);
                            }
                        }
                        ToDoList createNewList = new ToDoList(listName, newToDoList);
                        toDoLists.add(createNewList);
                    }
                }
                inputStreamReader.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        toDoManager.setToDoManager(toDoLists);
    }
}
