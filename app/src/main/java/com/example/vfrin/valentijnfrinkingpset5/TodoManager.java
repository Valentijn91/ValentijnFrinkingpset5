package com.example.vfrin.valentijnfrinkingpset5;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;



public class TodoManager {
    private static TodoManager manager;
    private ArrayList<TodoList> lists;

    private final static String PREFIX_TDL = "tdl:";
    private final static String PREFIX_TDI = "tdi:";
    private final static String FILE_NAME = "TODO_MANAGER_FILE";


    private TodoManager() {
        this.lists = new ArrayList<>();
    }

    //    Returns a instance of this class
    public static TodoManager getInstance() {
        manager = new TodoManager();
        return manager;
    }

    //    Adds a TodoList
    public void addList(String listTitle) {
        TodoList list = new TodoList(listTitle);
        lists.add(list);
    }

    //    Removes a TodoList
    public void removeList(int position) {
        lists.remove(position);
    }

    //    Adds a TodoItem to a List
    public void addItem(Context context, String itemTitle, int position) {
        TodoItem item = new TodoItem(itemTitle);
        lists.get(position).addTodoItem(item);
        writeTodos(context);
    }

    //    Removes a TodoItem from a TodoList
    public void removeItem(int position, int selectedList) {
        lists.get(selectedList).removeItem(position);
    }

    //    Switches the completed boolean from an TodoItem
    public void switchCompleted(int position, int selectedList) {
        lists.get(selectedList).getTodoItems().get(position).switchCompleted();;
    }


    //    Writes the TodoLists and TodoItems to the internal storage
    public void writeTodos(Context context) {
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            for (TodoList list : lists) {
                writer.write(String.format("%s%s", PREFIX_TDL, list.getTitle()));
                writer.newLine();

                for (TodoItem item : list.getTodoItems()) {
                    writer.write(String.format("%s%s", PREFIX_TDI, item.getTitle()));
                    writer.newLine();
                    writer.write(String.valueOf(item.getCompleted()));
                    writer.newLine();
                }

            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //    Reads the TodoLists and TodoItems from the internal storage
    public void readTodos(Context context) {
        manager = TodoManager.getInstance();
        FileInputStream inputStream;

        try {
            inputStream = context.openFileInput(FILE_NAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            TodoList list = null;
            while((line = reader.readLine()) != null) {
                if (line.startsWith(PREFIX_TDL)) {
                    if (list != null) {
                        lists.add(list);
                    }
                    line = line.replaceFirst(PREFIX_TDL, "");
                    list = new TodoList(line);
                } else if (line.startsWith(PREFIX_TDI)) {
                    line = line.replaceFirst(PREFIX_TDI, "");
                    TodoItem item = new TodoItem(line);
                    line  = reader.readLine();
                    item.setCompleted(Boolean.valueOf(line));
                    list.addTodoItem(item);
                }
            }
            if (list != null) {
                lists.add(list);
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //    Return an array containing all titles of lists
    public String[] getListTitles() {
        int numTitles = lists.size();
        String[] titles = new String[numTitles];

        for (int i = 0; i < numTitles; i++) {
            titles[i] = lists.get(i).getTitle();
        }
        return titles;
    }

    //    Returns the TodoItems given a TodoList
    public ArrayList<TodoItem> getTodos (int position) {
        return lists.get(position).getTodoItems();
    }

    //    Returns the number of lists
    public int getNumberOfLists() {
        return lists.size();
    }

    //    Returns the title of a Todolist
    public String getListTitle(int position) {
        return lists.get(position).getTitle();
    }
}