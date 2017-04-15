package com.fame.plumbum.lithicsin.adapters;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fame.plumbum.lithicsin.R;
import com.fame.plumbum.lithicsin.interfaces.Choose_image;
import com.fame.plumbum.lithicsin.interfaces.Load_more;
import com.fame.plumbum.lithicsin.model.Orders;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.view.View.GONE;
import static com.fame.plumbum.lithicsin.adapters.OrderAdapter.VIEW_TYPES.Footer;
import static com.fame.plumbum.lithicsin.adapters.OrderAdapter.VIEW_TYPES.Normal;

/**
 * Created by pankaj on 12/1/17.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    private Context mContext;
    private static List<Orders> albumList;
    private String parent;
    private Load_more instance = null;
    private Choose_image image_instance = null;

    class VIEW_TYPES {
        static final int Normal = 2;
        static final int Footer = 3;
    }


    @Override
    public int getItemViewType(int position) {
        if (isPositionFooter(position)) {
            return Footer;
        }
        return Normal;
    }

    private static boolean isPositionFooter(int position) {
        return position == albumList.size()-1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView product_name, price, status, order_number;
        ImageView thumbnail;
        Button load_more;
        RelativeLayout rl_orders;

        MyViewHolder(View view) {
            super(view);
            rl_orders = (RelativeLayout) view.findViewById(R.id.rl_orders_included);
            order_number = (TextView) view.findViewById(R.id.order_number);
            product_name = (TextView) view.findViewById(R.id.product_name);
            thumbnail = (ImageView) view.findViewById(R.id.product_image);
            price = (TextView) view.findViewById(R.id.product_price);
            status = (TextView) view.findViewById(R.id.order_status);
            load_more = (Button) view.findViewById(R.id.load_more);
        }
    }


    public OrderAdapter(Context mContext, List<Orders> albumList, String parent, Load_more instance, Choose_image image_instance) {
        this.mContext = mContext;
        OrderAdapter.albumList = albumList;
        this.parent = parent;
        this.instance = instance;
        this.image_instance = image_instance;
    }

    public OrderAdapter(Context mContext, List<Orders> albumList, String parent, Load_more instance) {
        this.mContext = mContext;
        OrderAdapter.albumList = albumList;
        this.parent = parent;
        this.instance = instance;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_products, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Orders album = albumList.get(position);
        holder.product_name.setText(album.getName());
        holder.price.setText(album.getPrice());
        holder.order_number.setText(album.getOrder_id());
        holder.status.setText(album.getStatus());
        Picasso.with(mContext).load("http://www.lithics.in/media/catalog/product"+albumList.get(position).getThumbnail()).resize(120, 120).into(holder.thumbnail);
        if (parent.contentEquals("SchedulePickup")) {
            holder.rl_orders.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    image_instance.load_image_picker();
                }
            });
        }
        switch(getItemViewType(position)) {
            case Normal:
                holder.load_more.setVisibility(GONE);
                break;
            case Footer:
                holder.load_more.setVisibility(View.VISIBLE);
                holder.load_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        instance.onInterfaceClick();
                    }
                });
                break;
        }
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        if (parent.contentEquals("Pickup")){
            inflater.inflate(R.menu.menu_pickup, popup.getMenu());
        } else if (parent.contentEquals("MyOrders"))
            inflater.inflate(R.menu.menu_orders, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    private class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        MyMenuItemClickListener() {

        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_schedule_pickup:
                    Toast.makeText(mContext, "Schedule Pickup", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_cancel:
                    Toast.makeText(mContext, "Cancel", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }
}