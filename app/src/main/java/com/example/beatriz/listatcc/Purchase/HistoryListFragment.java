package com.example.beatriz.listatcc.Purchase;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.beatriz.listatcc.Database.DatabaseHandler;
import com.example.beatriz.listatcc.MainActivity;
import com.example.beatriz.listatcc.Model.Purchase;
import com.example.beatriz.listatcc.R;

import java.util.ArrayList;

public class HistoryListFragment extends Fragment {
    RecyclerView mRecyclerView;
    Context mContext;
    DatabaseHandler db;
    protected View view;

    public static HistoryListFragment newInstance() {
        HistoryListFragment listFragment = new HistoryListFragment();

        return listFragment;
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
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHandler(getActivity());

        ((MainActivity) getActivity()).setActionBarTitle("Histórico", true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.list_lists, container, false);
        final ArrayList<Purchase> purchases = db.retrieveAllPurchases();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        mRecyclerView.setAdapter(new HistoryListAdapter(mContext, purchases));
        return view;
    }
}
