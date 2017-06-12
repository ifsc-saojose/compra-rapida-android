package com.example.beatriz.listatcc.Lists;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.beatriz.listatcc.MainActivity;
import com.example.beatriz.listatcc.Market.MarketListFragment;
import com.example.beatriz.listatcc.Model.Lists;
import com.example.beatriz.listatcc.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private ArrayList<Lists> mListList;
    private Context mContext = null;

    public ListAdapter(Context context, ArrayList<Lists> listLists) {
        super();
        mContext = context;
        mListList = listLists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_cell_list, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Lists listItem = mListList.get(i);

        if (listItem != null) {
            viewHolder.mListName.setText(listItem.getName());

            Date date=new Date(listItem.getDate());
            SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy HH:mm");
            String dateText = df2.format(date);

            viewHolder.mListDate.setText(dateText);
            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    fragmentJump(listItem);
                }
            });
        }
    }

    private void fragmentJump(Lists mItemSelected) {
        if (ListFragment.mIsGoingToShopping) {
            //open lista de mercados
            switchContent(R.id.content_frame, MarketListFragment.newInstance(mItemSelected.getId()));
        } else {
            switchContent(R.id.content_frame, ListDetailFragment.newInstance(mItemSelected.getId()));
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
        return mListList.size();
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
