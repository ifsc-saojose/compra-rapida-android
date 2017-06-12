package com.example.beatriz.listatcc.Purchase;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.beatriz.listatcc.Database.DatabaseHandler;
import com.example.beatriz.listatcc.Model.Purchase;
import com.example.beatriz.listatcc.Util.Tab;
import com.example.beatriz.listatcc.Util.TabFragment;

import java.util.ArrayList;

/**
 * Created by Beatriz on 05/09/2016.
 */
public class PurchaseFragment extends TabFragment {

    long mListId;
    DatabaseHandler db;

    public static PurchaseFragment newInstance(long listId) {
        PurchaseFragment shoppingList = new PurchaseFragment();

        Bundle args = new Bundle();
        args.putLong("listId", listId);
        shoppingList.setArguments(args);

        return shoppingList;
    }

    public static PurchaseFragment newInstance(long listId, long purchaseId) {
        PurchaseFragment shoppingList = new PurchaseFragment();

        Bundle args = new Bundle();
        args.putLong("LIST_ID", listId);
        args.putLong("PURCHASE_ID", purchaseId);
        shoppingList.setArguments(args);

        return shoppingList;
    }

    public static PurchaseFragment newInstance(String supermarketId, long listId, long firstCategory) {
        PurchaseFragment shoppingList = new PurchaseFragment();

        Bundle args = new Bundle();
        args.putString("SUPERMARKET_ID", supermarketId);
        args.putLong("LIST_ID", listId);
        args.putLong("FIRST_CATEGORY", firstCategory);
        shoppingList.setArguments(args);

        return shoppingList;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Purchase purchase = null;
        long firstCategory = 0;
        long purchaseId;
        db = new DatabaseHandler(getContext());
        mListId = getArguments().getLong("LIST_ID");
        ArrayList<Tab> tabs = new ArrayList<>();

        boolean isHistoryDetail = true;
        if (!getArguments().containsKey("PURCHASE_ID")) {
            Log.i("TAG_CREATE_PU: ", "CREATE PURCHASE");
            purchase = createPurchase();
            purchaseId = purchase.getId();
            isHistoryDetail = false;
        } else
            purchaseId = getArguments().getLong("PURCHASE_ID");

        if (getArguments().containsKey("FIRST_CATEGORY")) {
            firstCategory = getArguments().getLong("FIRST_CATEGORY");
        }

        String marketId = purchase == null ? "" : purchase.getMarketId();

        PurchaseItensFragment mGotItens = PurchaseItensFragment.newInstance(mListId, PurchaseItensFragment.OPT_GOT, purchaseId, marketId, isHistoryDetail, firstCategory);
        PurchaseItensFragment mCurrentItens = PurchaseItensFragment.newInstance(mListId, PurchaseItensFragment.OPT_GETTING, purchaseId, marketId, isHistoryDetail, firstCategory);
        PurchaseItensFragment mDidntGetItens = PurchaseItensFragment.newInstance(mListId, PurchaseItensFragment.OPT_DONT_GET, purchaseId, marketId, isHistoryDetail, firstCategory);

        tabs.add(new Tab("Pegos", mGotItens));
        tabs.add(new Tab("Itens", mCurrentItens));
        tabs.add(new Tab("Nao Pegos", mDidntGetItens));

        setTabs(tabs);
    }

    public Purchase createPurchase() {

        Purchase purchase = new Purchase();
        purchase.setFromList(mListId);
        purchase.setMarketId(getArguments().containsKey("SUPERMARKET_ID") ? getArguments().getString("SUPERMARKET_ID") : "");

        long purchaseId = db.createPurchase(purchase);
        purchase.setId(purchaseId);

        //Populando os itens da compra na tabela PurchaseItemStatusTable
        db.populatePurchaseItens(purchase);

        return purchase;
    }
}
