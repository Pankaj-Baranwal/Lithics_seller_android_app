package com.fame.plumbum.lithicsin.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fame.plumbum.lithicsin.R;
import com.fame.plumbum.lithicsin.activities.MessageExchange;
import com.fame.plumbum.lithicsin.model.Sellers;

import java.util.List;

/**
 * Project Name: 	<Visual Perception For The Visually Impaired>
 * Author List: 		Pankaj Baranwal
 * Filename: 		<>
 * Functions: 		<>
 * Global Variables:	<>
 */
public class SellersAdapter extends RecyclerView.Adapter<SellersAdapter.MyViewHolder> {

    private Context mContext;
    private List<Sellers> sellerList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView sellerName, sellerOrg;
        LinearLayout ll_sellers_list;


        MyViewHolder(View view) {
            super(view);
            sellerName = (TextView) view.findViewById(R.id.seller_name);
            sellerOrg = (TextView) view.findViewById(R.id.seller_org);
            ll_sellers_list = (LinearLayout) view.findViewById(R.id.ll_sellers_list);
        }
    }


    public SellersAdapter(Context mContext, List<Sellers> sellerList) {
        this.mContext = mContext;
        this.sellerList = sellerList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_sellers, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Sellers sellers = sellerList.get(position);
        holder.sellerName.setText(sellers.getContact_person());
        holder.sellerOrg.setText(sellers.getSeller_name());
        holder.ll_sellers_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MessageExchange.class);
                intent.putExtra("chat_id", sellers.getId());
                intent.putExtra("name", sellers.getContact_person());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return sellerList.size();
    }
}