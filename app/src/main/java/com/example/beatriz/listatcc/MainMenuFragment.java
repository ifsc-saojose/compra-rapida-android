package com.example.beatriz.listatcc;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.beatriz.listatcc.Database.DatabaseHandler;
import com.example.beatriz.listatcc.Lists.ListFragment;
import com.example.beatriz.listatcc.Location.CurrentLocationPoint;
import com.example.beatriz.listatcc.Market.MarketListFragment;
import com.example.beatriz.listatcc.Model.GoogleAPIObjectResponse;
import com.example.beatriz.listatcc.Model.Mercado;
import com.example.beatriz.listatcc.Model.Result;
import com.example.beatriz.listatcc.NewList.NewListFragment;
import com.example.beatriz.listatcc.Product.ProductFragment;
import com.example.beatriz.listatcc.Purchase.HistoryListFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MainMenuFragment extends CurrentLocationPoint implements RetrofitHelper.RetrofitHelperCallback {

    protected View view;
    public Context mContext;
    public ProgressDialog mProgressDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_menu, null, false);

        ((MainActivity) getActivity())
                .setActionBarTitle("Compra Rápida", false);

        ImageButton btnOpenGoShopping = (ImageButton) view.findViewById(R.id.btn_compras);
        btnOpenGoShopping.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goShopping();
            }
        });

        ImageButton btnOpenLists = (ImageButton) view.findViewById(R.id.btn_listas);
        btnOpenLists.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openLists();
            }
        });

        ImageButton btnOpenSupermarket = (ImageButton) view.findViewById(R.id.btn_mercados);
        btnOpenSupermarket.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openMercados();
            }
        });

        ImageButton btnListPurchase = (ImageButton) view.findViewById(R.id.btn_historico);
        btnListPurchase.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openPurchaseList();
            }
        });

        ImageButton btnShowProducts = (ImageButton) view.findViewById(R.id.btn_produtos);
        btnShowProducts.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openProducts();
            }
        });

        FloatingActionButton btnNewList = (FloatingActionButton) view.findViewById(R.id.new_list_btn);
        btnNewList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                createNewList();
            }
        });

        return view;
    }

    public void goShopping() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, ListFragment.newInstance(true))
                .addToBackStack("Compra")
                .commit();
    }

    public void openLists() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, ListFragment.newInstance(false))
                .setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_NONE).addToBackStack(null)
                .commit();
    }

    public void openMercados() {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("Carregando...");
        mProgressDialog.show();

        RetrofitHelper retrofitHelper = new RetrofitHelper(mContext, this, GoogleApiRequest.class);
        Call<GoogleAPIObjectResponse> mapCall = ((GoogleApiRequest) retrofitHelper.getT()).getSupermarket(mCurrentLatitude + "," + mCurrentLongitude, String.valueOf(5000), "prominence", "grocery_or_supermarket", GoogleApiRequest.GOOGLE_API_KEY);
        retrofitHelper.execute(mapCall);
    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
        if (!mUpdateLocation) {
            mProgressDialog.dismiss();

            GoogleAPIObjectResponse GoogleAPIObjectResponse = (GoogleAPIObjectResponse) response.body();
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, MarketListFragment.newInstance(new Gson().toJson(GoogleAPIObjectResponse.getResults())))
                    .setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_NONE).addToBackStack(null)
                    .commit();
        } else {
            mUpdateLocation = false;
            GoogleAPIObjectResponse googleAPIObjectResponse = (GoogleAPIObjectResponse) response.body();
            List<Result> currentSupermarkets = googleAPIObjectResponse.getResults();
            ArrayList<Mercado> mercadoList = new ArrayList();

            for (int i = 0; i < currentSupermarkets.size(); i++) {
                Result result = currentSupermarkets.get(i);
                Mercado mercado = new Mercado(result.getId(), result.getName(), result.getVicinity(), result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng());
                mercadoList.add(mercado);
            }

            DatabaseHandler db = new DatabaseHandler(mContext);
            db.updateSupermarketCurrentNearby(mercadoList);
            mUpdateLocation = false;
        }
    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }

    public void openProducts() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, new ProductFragment())
                .setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_NONE).addToBackStack(null)
                .commit();
    }

    public void openPurchaseList() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, new HistoryListFragment())
                .setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_NONE).addToBackStack(null)
                .commit();
    }

    public void createNewList() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, new NewListFragment())
                .setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_NONE).addToBackStack(null)
                .commit();
    }
}
