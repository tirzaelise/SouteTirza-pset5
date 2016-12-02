/*
 * Native App Studio: Assignment 5
 * Many To Do Lists
 * Tirza Soute
 *
 * This file manages the To Do Lists.
 */

package com.example.tirza.soutetirza_pset5;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;

class ToDoManager {
    private static ToDoManager toDoManager = new ToDoManager();
    private ArrayList<ToDoList> toDoLists;
    private final static String FILENAME = "lists.txt";

    static ToDoManager getInstance() {
        return toDoManager;
    }

    private ToDoManager() {
        toDoLists = new ArrayList<>();
    }

    void setToDoManager(ArrayList<ToDoList> toDoLists) {
        this.toDoLists = toDoLists;
    }

    ArrayList<ToDoList> getToDoManager() {
        return toDoLists;
    }

    /** Returns the index of a list in the list of To Do Lists */
    int getListIndex(String title) {
        ToDoList currentList;
        int index = -500;

        for (int i = 0; i < toDoLists.size(); i++) {
            currentList = toDoLists.get(i);
            if (currentList.getTitle().equals(title)) index = i;
        }
        return index;
    }

    /** Reads the To Do Lists */


    /** Creates a string that holds all the information about the user's To Do Lists*/
    private String createManagerString() {
        toDoLists = getToDoManager();
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

    /** Writes to the file to save the new To Do List */
    void writeToDoList(Context context) {
        try {
            String outputString = createManagerString();
            FileOutputStream outputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(outputStream);
            outputWriter.write(outputString);
            outputWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Reads the file that the To Do Lists were written to */
    void readToDoLists(Context context) {
        ArrayList<ToDoList> toDoLists = new ArrayList<>();

        try {
            FileInputStream inputStream = context.openFileInput(FILENAME);

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
