package com.example.beatriz.listatcc.NewList;


import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.beatriz.listatcc.Model.Lists;
import com.example.beatriz.listatcc.R;

import java.util.ArrayList;

/**
 * Created by Beatriz on 24/07/2016.
 */
public class ListItensGottenFragment extends Fragment {

    private GridView gridView;
    private ListItensGottenAdapter gridAdapter;
    Context mContext;
    public Lists mLists;
    public static ArrayList<ProductItem> productGottenItems = new ArrayList<>();

    public static ListItensGottenFragment newInstance() {
        ListItensGottenFragment fragment = new ListItensGottenFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mLists = new Lists();
    }

    public ListItensGottenFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.create_list_gridview, container, false);
        gridView = (GridView) view.findViewById(R.id.gridView);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        gridAdapter = new ListItensGottenAdapter(mContext, R.layout.create_list_item_gridview, productGottenItems);
        gridView.setFastScrollEnabled(true);
        gridView.setAdapter(gridAdapter);

        updateList();
    }

    public void updateList() {
        gridAdapter.notifyDataSetChanged();
    }
}
