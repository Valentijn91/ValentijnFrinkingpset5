package com.example.vfrin.valentijnfrinkingpset5;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemFragment extends Fragment {
    private ItemClicked itemClicked;

    //    Interface for the methods describing the click listerners
    public interface ItemClicked {
        void removeItem(int position);
        void switchCompleted(int position);
    }

    //    Define the layout for this Fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_item, container, false);
    }

    //    Set the click listeners
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListViewClickListeners();
    }

    //    Define the ItemClick iterface
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        itemClicked = (ItemClicked) context;
    }

    //    Update the ListView containing TodoItems
    public void updateListView(String listTitle, ArrayList<TodoItem> todoItems){
        if (getActivity() != null) {
            TextView listTitleTextView = (TextView) getView().findViewById(R.id.list_title_tv);
            ListView listView = (ListView) getView().findViewById(R.id.item_lv);
            listTitleTextView.setText(listTitle);
            ItemListAdapter adapter = new ItemListAdapter(getActivity(),
                    R.layout.item_tditem_list_view, todoItems);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
        }
    }

    //    Set the click-listeners for the ListView
    private void setListViewClickListeners() {
        ListView listView = (ListView) getView().findViewById(R.id.item_lv);

//        Switch the completed status of a TodoItem on click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemClicked.switchCompleted(position);
            }
        });

//        Remove a Todoitem on long click
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                itemClicked.removeItem(position);
                return true;
            }
        });
    }
}