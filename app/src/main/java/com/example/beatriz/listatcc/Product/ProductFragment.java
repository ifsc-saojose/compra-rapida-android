package com.example.beatriz.listatcc.Product;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.beatriz.listatcc.Database.DatabaseHandler;
import com.example.beatriz.listatcc.MainActivity;
import com.example.beatriz.listatcc.Model.Category;
import com.example.beatriz.listatcc.Model.Item;
import com.example.beatriz.listatcc.R;

import java.util.ArrayList;

public class ProductFragment extends Fragment implements ItemClickListener {
    Context mContext;
    static DatabaseHandler db;
    protected View view;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHandler(mContext);

        ((MainActivity) getActivity()).setActionBarTitle("Produtos", true);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                try {
                    getFragmentManager().popBackStack();

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                break;
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ArrayList<Category> allCategories = db.retrieveAllCategories();

        view = inflater.inflate(R.layout.product_recycler_view, container, false);
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.product_view);
        SectionedExpandableLayoutHelper sectionedExpandableLayoutHelper = new SectionedExpandableLayoutHelper(mContext,
                mRecyclerView, this, 3);

        for (int i = 0; i < allCategories.size(); i++) {
            ArrayList<Item> allItensByCategory = db.retrieveItensByCategory(allCategories.get(i).getId());
            sectionedExpandableLayoutHelper.addSection(allCategories.get(i).getName(), allItensByCategory);
        }

        sectionedExpandableLayoutHelper.notifyDataSetChanged();
        //TODO DESENVOLVER TELA VAZIA
        return view;
    }

    @Override
    public void itemClicked(Item item) {
        Toast.makeText(mContext, "Item: " + item.getId() + " clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void itemClicked(Section section) {
        Toast.makeText(mContext, "Section: " + section.getName() + " clicked", Toast.LENGTH_SHORT).show();
    }
}
