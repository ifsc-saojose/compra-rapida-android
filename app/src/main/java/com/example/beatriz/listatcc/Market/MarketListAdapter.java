package com.example.beatriz.listatcc.Market;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.beatriz.listatcc.Database.DatabaseHandler;
import com.example.beatriz.listatcc.MainActivity;
import com.example.beatriz.listatcc.Model.Mercado;
import com.example.beatriz.listatcc.Purchase.PurchaseFragment;
import com.example.beatriz.listatcc.R;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Beatriz on 29/05/2016.
 */
public class MarketListAdapter extends RecyclerView.Adapter<MarketListAdapter.MercadoViewHolder> {

    private DatabaseHandler db;
    private List<Mercado> mercadoList;
    public long mListId = 0;
    public Context mContext;

    public MarketListAdapter(List<Mercado> userList) {
        this.mercadoList = userList;
    }

    public MarketListAdapter(Context context, List<Mercado> mercadoList, Long listId) {
        this.mercadoList = mercadoList;
        this.mListId = listId;
        mContext = context;
        db = new DatabaseHandler(mContext);
    }

    @Override
    public MercadoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.supermarket_cardview, viewGroup, false);

        return new MercadoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MercadoViewHolder holder, int position) {
        final Mercado supermarket = mercadoList.get(position);
        if (supermarket != null) {
            String[] endereco = supermarket.getRua().split(Pattern.quote(","));
            holder.nome.setText(supermarket.getName());
            holder.rua.setText(endereco[0]);
            holder.cidade.setText((endereco[endereco.length - 1]).trim());
            if (mListId != 0) {
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity mainActivity = (MainActivity) mContext;
                        mainActivity.switchContent(R.id.content_frame, PurchaseFragment.newInstance(supermarket.getId(), mListId, MarketListFragment.getFirstCategory()));
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return mercadoList.size();
    }

    class MercadoViewHolder extends RecyclerView.ViewHolder {
        TextView nome, rua, cidade;
        View mView;
        CardView cardView;

        public MercadoViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            nome = (TextView) mView.findViewById(R.id.name);
            rua = (TextView) mView.findViewById(R.id.rua);
            cidade = (TextView) mView.findViewById(R.id.cidade);
            cardView = (CardView) mView.findViewById(R.id.card_view);
        }
    }
}
