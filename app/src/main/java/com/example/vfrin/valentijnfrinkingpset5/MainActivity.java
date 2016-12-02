package com.example.vfrin.valentijnfrinkingpset5;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements ListFragment.ListClicked, ItemFragment.ItemClicked{

    private TodoManager manager;
    private ListFragment listFragment;
    private ItemFragment itemFragment;
    private AddListFragment addListFragment;
    private FrameLayout listFrame;
    private FrameLayout itemFrame;
    private FrameLayout addListFrame;
    private int openFragment;
    private int selectedList;

    //    Define the Fragments and FrameLayout upon creation of the Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = TodoManager.getInstance();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        listFragment = new ListFragment();
        itemFragment = new ItemFragment();
        addListFragment = new AddListFragment();
        fragmentTransaction.add(R.id.list_fragment_fl, listFragment);
        fragmentTransaction.add(R.id.item_fragment_fl, itemFragment);
        fragmentTransaction.add(R.id.add_list_fragment_fl, addListFragment);
        fragmentTransaction.commit();

        listFrame = (FrameLayout) findViewById(R.id.list_fragment_fl);
        itemFrame = (FrameLayout) findViewById(R.id.item_fragment_fl);
        addListFrame = (FrameLayout) findViewById(R.id.add_list_fragment_fl);
    }

    //    Read the TodoManager,update the TodoLists List View and restore the state upon start of the
//    Activity.
    @Override
    protected void onStart() {
        super.onStart();
        manager.readTodos(this);
        String[] listTitles = manager.getListTitles();
        listFragment.updateListView(listTitles);

        int orientation = getResources().getConfiguration().orientation;
        SharedPreferences pref = getApplicationContext().getSharedPreferences("State", 0);

        openFragment = pref.getInt("open_fragment", 0);
        selectedList = pref.getInt("selected_list", -1);

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            findViewById(R.id.to_lists_b).setVisibility(View.VISIBLE);
            if (openFragment == 0) {
                layoutPortraitListFragment();
            } else if (openFragment == 1) {
                openItemFragment(selectedList);
            } else {
                layoutAddListFragment();
            }
        } else {
            findViewById(R.id.to_lists_b).setVisibility(View.INVISIBLE);
            if (openFragment == 0) {
                layoutLandscapeListFragment();
            } else if (openFragment == 1) {
                openItemFragment(selectedList);
            } else {
                layoutAddListFragment();
            }
        }
    }

    //    Writes the TodoManager when the activity is paused and saves the state.
    @Override
    protected void onPause() {
        manager.writeTodos(this);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("State", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("open_fragment", openFragment);
        editor.putInt("selected_list", selectedList);
        editor.commit();
        super.onPause();
    }

    //    Change the layout when the screen orientation is changed
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            findViewById(R.id.to_lists_b).setVisibility(View.INVISIBLE);
            if (openFragment == 0) {
                layoutLandscapeListFragment();
            } else if (openFragment == 1) {
                layoutLandscapeItemFragement();
            } else {
                layoutAddListFragment();
            }
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            findViewById(R.id.to_lists_b).setVisibility(View.VISIBLE);
            if (openFragment == 0) {
                layoutPortraitListFragment();
            } else if (openFragment == 1) {
                layoutPortraitItemFragement();
            } else {
                layoutAddListFragment();
            }
        }
    }

    //    Open the ItemFragment
    @Override
    public void openItemFragment(int position){
        selectedList = position;
        openFragment = 1;
        ArrayList<TodoItem> todoItems = manager.getTodos(selectedList);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutPortraitItemFragement();
        } else {
            layoutLandscapeItemFragement();
        }
        String listTitle = manager.getListTitle(selectedList);
        itemFragment.updateListView(listTitle, todoItems);
    }

    //    Remove a TodoItem from a TodoList
    @Override
    public void removeItem(int position) {
        manager.removeItem(position, selectedList);
        ArrayList<TodoItem> todoItems = manager.getTodos(selectedList);
        String listTitle = manager.getListTitle(selectedList);
        itemFragment.updateListView(listTitle, todoItems);
    }

    //    Switch the completed status from a TodoItem
    @Override
    public void switchCompleted(int position) {
        manager.switchCompleted(position, selectedList);
        ArrayList<TodoItem> todoItems = manager.getTodos(selectedList);
        String listTitle = manager.getListTitle(selectedList);
        itemFragment.updateListView(listTitle, todoItems);
    }

    //    Remove a TodoList
    @Override
    public void removeList(int position) {
        manager.removeList(position);
        String[] listTitles = manager.getListTitles();
        listFragment.updateListView(listTitles);
        if (position == selectedList) {
            openFragment = 0;
            selectedList = -1;
            itemFrame.setVisibility(View.INVISIBLE);
        }
    }

    //    Open the addListFragment
    public void openAddListFragment(View view) {
        openFragment = 2;
        layoutAddListFragment();
        selectedList = -1;
    }

    //    Add a TodoItem to an TodoList
    public void addItemToList(View view) {
        EditText itemEditText = (EditText) findViewById(R.id.add_item_et);
        String item = itemEditText.getText().toString();
        manager.addItem(this, item, selectedList);
        ArrayList<TodoItem> todoItems = manager.getTodos(selectedList);
        String listTitle = manager.getListTitle(selectedList);
        itemFragment.updateListView(listTitle, todoItems);
        itemEditText.setText("");
    }

    //    Add a TodoList to the TodoManager
    public void addTodoList(View view) {
        openFragment = 1;

        EditText titleET = (EditText) findViewById(R.id.title_et);
        String title = titleET.getText().toString();
        titleET.setText("");
        manager.addList(title);

        selectedList = manager.getNumberOfLists() - 1;
        ArrayList<TodoItem> todoItems = manager.getTodos(selectedList);
        String listTitle = manager.getListTitle(selectedList);
        itemFragment.updateListView(listTitle, todoItems);

        String[] listTitles = manager.getListTitles();
        listFragment.updateListView(listTitles);

        addListFrame.setVisibility(View.GONE);
        itemFrame.setVisibility(View.VISIBLE);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            findViewById(R.id.to_lists_b).setVisibility(View.VISIBLE);
            listFrame.setVisibility(View.GONE);
        } else {
            findViewById(R.id.to_lists_b).setVisibility(View.INVISIBLE);
            listFrame.setVisibility(View.VISIBLE);
        }
    }

    //    Open the ListFragment
    public void toLists(View view) {
        openFragment = 0;
        layoutPortraitListFragment();
    }

    //    Set the layout settings for a portait orientation with focus on the ListFragment
    public void layoutPortraitListFragment() {
        listFrame.setVisibility(View.VISIBLE);
        itemFrame.setVisibility(View.GONE);
        addListFrame.setVisibility(View.GONE);
    }

    //    Set the layout settings for a portait orientation with focus on the ItemFragment
    public void layoutPortraitItemFragement() {
        listFrame.setVisibility(View.GONE);
        itemFrame.setVisibility(View.VISIBLE);
        addListFrame.setVisibility(View.GONE);
    }

    //    Set the layout settings for a landscape orientation with focus on the ListFragment
    public void layoutLandscapeListFragment() {
        listFrame.setVisibility(View.VISIBLE);
        itemFrame.setVisibility(View.INVISIBLE);
        addListFrame.setVisibility(View.GONE);
    }

    //    Set the layout settings for a landscape orientation with focus on the ItemFragment
    public void layoutLandscapeItemFragement() {
        listFrame.setVisibility(View.VISIBLE);
        itemFrame.setVisibility(View.VISIBLE);
        addListFrame.setVisibility(View.GONE);
    }

    //    Set the layout settings with focus on the AddListFragment
    public void layoutAddListFragment() {
        listFrame.setVisibility(View.GONE);
        itemFrame.setVisibility(View.GONE);
        addListFrame.setVisibility(View.VISIBLE);
    }
}