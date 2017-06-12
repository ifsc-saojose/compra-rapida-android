package com.example.beatriz.listatcc.Market;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.beatriz.listatcc.Database.DatabaseHandler;
import com.example.beatriz.listatcc.MainActivity;
import com.example.beatriz.listatcc.Model.Category;
import com.example.beatriz.listatcc.Model.Mercado;
import com.example.beatriz.listatcc.Model.Result;
import com.example.beatriz.listatcc.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beatriz on 29/05/2016.
 */
public class MarketListFragment extends Fragment {

    MarketListAdapter allUsersAdapter;
    RecyclerView mRecyclerView;
    protected View view;
    String urlMarket;
    List<Result> supermarketNearby;
    public long mListId = 0;
    List<Mercado> mercadoList;
    Context mContext;
    DatabaseHandler db;
    public static long mFirstCategory;


    public static MarketListFragment newInstance(String result) {

        MarketListFragment supermarketListFragment = new MarketListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("TAG", result);

        supermarketListFragment.setArguments(bundle);

        return supermarketListFragment;
    }

    public static MarketListFragment newInstance(long purchaseId) {
        MarketListFragment supermarketListFragment = new MarketListFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("PURCHASE_ID", purchaseId);
        supermarketListFragment.setArguments(bundle);

        return supermarketListFragment;
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
        db = new DatabaseHandler(getContext());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configActionBar();
        if (getArguments().containsKey("TAG")) {
            urlMarket = getArguments().getString("TAG");
            supermarketNearby = new Gson().fromJson(urlMarket, new TypeToken<List<Result>>() {
            }.getType());
        } else {
            mListId = getArguments().getLong("PURCHASE_ID");
            mercadoList = db.retrieveAllSupermarketCurrentNearby();
            openOrderPurchaseDialog();
        }
    }

    private void configActionBar() {
        ((MainActivity) getActivity())
                .setActionBarTitle("Mercados", true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.supermarket_view, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.supermarket_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        if (mListId == 0) {
            mercadoList = new ArrayList();

            for (int i = 0; i < supermarketNearby.size(); i++) {
                Result result = supermarketNearby.get(i);
                Mercado mercado = new Mercado(result.getId(), result.getName(), result.getVicinity(), result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng());
                mercadoList.add(mercado);
            }
            allUsersAdapter = new MarketListAdapter(mercadoList);
        } else {
            allUsersAdapter = new MarketListAdapter(mContext, mercadoList, mListId);
        }

        mRecyclerView.setAdapter(allUsersAdapter);
        return view;
    }

    public static long getFirstCategory(){
        return mFirstCategory;
    }
    private void openOrderPurchaseDialog() {

        final ArrayList<Category> categories = db.retrievePurchaseCategory(mListId);
        final String[] categoryArray = new String[categories.size()];
        int i = 0;
        final long[] firstCategory = {0};
        for (Category category : categories) {
            categoryArray[i] = category.getName();
            i++;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Em qual seção irá iniciar as compras?");

        int checkedItem = 0;
        firstCategory[0] = categories.get(0).getId();
        builder.setSingleChoiceItems(categoryArray, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (Category category : categories) {
                    if (category.getName().equals(categoryArray[which])) {
                        firstCategory[0] = category.getId();
                        break;
                    }
                }
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mFirstCategory = firstCategory[0];
            }
        });

        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
