package com.example.beatriz.listatcc.Purchase;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.beatriz.listatcc.MainActivity;
import com.example.beatriz.listatcc.Model.Purchase;
import com.example.beatriz.listatcc.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.ViewHolder> {

    private ArrayList<Purchase> mPurchaseList;
    private Context mContext = null;

    public HistoryListAdapter(Context context, ArrayList<Purchase> mPurchaseList) {
        super();
        mContext = context;
        this.mPurchaseList = mPurchaseList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_cell_list, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Purchase purchaseItem = mPurchaseList.get(i);

        if (purchaseItem != null) {
            Date date = new Date(purchaseItem.getDate());
            SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy HH:mm");

            viewHolder.mListName.setText(purchaseItem.getName());
            viewHolder.mListDate.setText(df2.format(date));
            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switchContent(R.id.content_frame, PurchaseFragment.newInstance(purchaseItem.getFromList(), purchaseItem.getId()));
                }
            });
        }
    }

    public void switchContent(int id, Fragment fragment) {
        if (mContext == null) {
            return;
        }

        if (mContext instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) mContext;
            mainActivity.switchContent(id, fragment);
        }
    }

    @Override
    public int getItemCount() {
        return mPurchaseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView mListName;
        public final TextView mListDate;
        public CardView cardView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            cardView = (CardView) view.findViewById(R.id.card_view);
            mListName = (TextView) view.findViewById(R.id.list_name);
            mListDate = (TextView) view.findViewById(R.id.list_date);
        }
    }
}
