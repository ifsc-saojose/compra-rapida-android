package com.example.beatriz.listatcc.Product;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import com.example.beatriz.listatcc.Model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by bpncool on 2/23/2016.
 */
public class SectionedExpandableLayoutHelper implements SectionStateChangeListener {

    //data list
    private LinkedHashMap<Section, ArrayList<Item>> mSectionDataMap = new LinkedHashMap<Section, ArrayList<Item>>();
    private ArrayList<Object> mDataArrayList = new ArrayList<Object>();

    //section map
    //TODO : look for a way to avoid this
    private HashMap<String, Section> mSectionMap = new HashMap<String, Section>();

    //adapter
    private SectionedExpandableGridAdapter mSectionedExpandableGridAdapter;

    //recycler view
    RecyclerView mRecyclerView;

    public SectionedExpandableLayoutHelper(Context context, RecyclerView recyclerView, ItemClickListener itemClickListener,
                                           int gridSpanCount) {

        //setting the recycler view
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, gridSpanCount);
        recyclerView.setLayoutManager(gridLayoutManager);
        mSectionedExpandableGridAdapter = new SectionedExpandableGridAdapter(context, mDataArrayList,
                gridLayoutManager, itemClickListener, this);
        recyclerView.setAdapter(mSectionedExpandableGridAdapter);

        mRecyclerView = recyclerView;
    }

    public void notifyDataSetChanged() {
        //TODO : handle this condition such that these functions won't be called if the recycler view is on scroll
        generateDataList();
        mSectionedExpandableGridAdapter.notifyDataSetChanged();
    }

    public void addSection(String section, ArrayList<Item> items) {
        Section newSection;
        mSectionMap.put(section, (newSection = new Section(section)));
        mSectionDataMap.put(newSection, items);
    }

    public void addItem(String section, Item item) {
        mSectionDataMap.get(mSectionMap.get(section)).add(item);
    }

    public void removeItem(String section, Item item) {
        Log.i("WJA_", "Checkando quais intens estao na secao");
        int index = 0;
        int indexToRemove = 0;

        Item itemToRemove = null;
        for (Map.Entry<Section, ArrayList<Item>> entry : mSectionDataMap.entrySet()){
            Section Key = entry.getKey();
            ArrayList<Item> value = entry.getValue();
            for(Item item1 : value){
                Log.i("WJA_", item1.getName());
                if(item1.getId() == item.getId()){
                    itemToRemove = item1;
                }
            }
        }

        //mSectionDataMap.get(mSectionMap.get(section)).remove(item);
        mSectionDataMap.get(mSectionMap.get(section)).remove(itemToRemove);
        Log.i("WJA_", "REMOVE ITEM jah acionado");

        for (Map.Entry<Section, ArrayList<Item>> entry : mSectionDataMap.entrySet()){
            Section Key = entry.getKey();
            ArrayList<Item> value = entry.getValue();
            for(Item name : value){
                Log.i("WJA_", name.getName());
            }
        }
    }

    public void removeSection(String section) {
        mSectionDataMap.remove(mSectionMap.get(section));
        mSectionMap.remove(section);
    }

    private void generateDataList () {
        mDataArrayList.clear();
        for (Map.Entry<Section, ArrayList<Item>> entry : mSectionDataMap.entrySet()) {
            Section key;
            mDataArrayList.add((key = entry.getKey()));
            if (key.isExpanded)
                mDataArrayList.addAll(entry.getValue());
        }
    }

    @Override
    public void onSectionStateChanged(Section section, boolean isOpen) {
        if (!mRecyclerView.isComputingLayout()) {
            section.isExpanded = isOpen;
            notifyDataSetChanged();
        }
    }
}
