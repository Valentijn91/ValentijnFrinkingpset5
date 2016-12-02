package com.example.vfrin.valentijnfrinkingpset5;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class ListFragment extends Fragment {
    private ListClicked listClicked;

    //    Interface for the methods describing the click-listeners
    public interface ListClicked {
        void openItemFragment(int position);
        void removeList(int position);
    }

    //    Set this Fragments layout, when this View is being created.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    //    Set the click-listeners of this Fragment when this View is created.
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setClickListeners();
    }

    //    Define the ListClicked interface when this View is attached.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listClicked = (ListClicked) context;
    }

    //    Updates the ListView of list titles.
    public void updateListView(String[] listTitles) {
        ListView listView = (ListView) getView().findViewById(R.id.list_lv);
        ListAdapter adapter = new ArrayAdapter<>(getActivity(), R.layout.item_tdlist_list_view,
                R.id.list_title_tv, listTitles);
        listView.setAdapter(adapter);
    }

    //    Set the click-listeners for the ListView.
    private void setClickListeners() {
        ListView listView = (ListView) getView().findViewById(R.id.list_lv);

//        Opens the ItemFragment on click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listClicked.openItemFragment(position);
            }
        });

//        Removes a TodoList on a long click
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                listClicked.removeList(position);
                return true;
            }
        });
    }
}