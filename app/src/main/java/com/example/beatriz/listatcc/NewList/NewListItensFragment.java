package com.example.beatriz.listatcc.NewList;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.beatriz.listatcc.Database.DatabaseHandler;
import com.example.beatriz.listatcc.MainActivity;
import com.example.beatriz.listatcc.R;

import java.util.ArrayList;

/**
 * Created by Beatriz on 23/07/2016.
 */
public class NewListItensFragment extends Fragment {

    private GridView gridView;
    private NewListItensAdapter gridAdapter;
    Context mContext;
    ArrayList<ProductItem> productItems = new ArrayList<>();
    DatabaseHandler db;

    public static NewListItensFragment newInstance() {
        NewListItensFragment fragment = new NewListItensFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ((MainActivity) getActivity())
                .setActionBarTitle("Nova lista", true);
        setHasOptionsMenu(true);

        super.onCreate(savedInstanceState);
        db = new DatabaseHandler(getContext());
        productItems = db.retrieveAllItems();
        ListItensGottenFragment.productGottenItems.clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                ((MainActivity) getActivity())
                        .onBackPressed();

                return true;
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.create_list_gridview, container, false);

        gridView = (GridView) view.findViewById(R.id.gridView);
        gridAdapter = new NewListItensAdapter(mContext, R.layout.create_list_item_gridview, productItems);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ProductItem item = (ProductItem) parent.getItemAtPosition(position);
                updateList(position, item);
            }
        });

        return view;
    }

    public void updateList(int position, ProductItem itemClicked) {
        productItems.remove(position);
        // ListItensGottenFragment.productGotName.add(itemClicked.getTitle());
        ListItensGottenFragment.productGottenItems.add(itemClicked);
        gridAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }
}
