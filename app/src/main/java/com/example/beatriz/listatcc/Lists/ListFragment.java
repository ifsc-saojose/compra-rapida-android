package com.example.beatriz.listatcc.Lists;

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
import com.example.beatriz.listatcc.Model.Lists;
import com.example.beatriz.listatcc.R;

import java.util.ArrayList;

public class ListFragment extends Fragment {
    RecyclerView mRecyclerView;
    Context mContext;
    DatabaseHandler db;
    protected View view;
    public static boolean mIsGoingToShopping;


    public static ListFragment newInstance(boolean isGoingToShopping) {
        ListFragment listFragment = new ListFragment();

        Bundle args = new Bundle();
        args.putBoolean("IS_GO_SHOPPING", isGoingToShopping);
        listFragment.setArguments(args);

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

        ((MainActivity) getActivity())
                .setActionBarTitle("Listas", true);
        setHasOptionsMenu(true);

        mIsGoingToShopping = getArguments().getBoolean("IS_GO_SHOPPING");
        db = new DatabaseHandler(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.list_lists, container, false);
        final ArrayList<Lists> lists = db.retrieveAllLists();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        mRecyclerView.setAdapter(new ListAdapter(mContext, lists));
        return view;
    }
}
