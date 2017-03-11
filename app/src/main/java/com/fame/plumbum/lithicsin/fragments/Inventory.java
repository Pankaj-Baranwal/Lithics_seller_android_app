package com.fame.plumbum.lithicsin.fragments;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.fame.plumbum.lithicsin.R;
import com.fame.plumbum.lithicsin.Singleton;
import com.fame.plumbum.lithicsin.adapters.OrderAdapter;
import com.fame.plumbum.lithicsin.model.Orders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fame.plumbum.lithicsin.utils.Constants.BASE_URL_DEFAULT;

/**
 * Created by pankaj on 17/1/17.
 */

public class Inventory extends Fragment{
    View rootView;
    private RecyclerView list_orders;
    private OrderAdapter adapter;
    private List<Orders> dataList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_orders, container, false);

        initCollapsingToolbar();
        try {
            Glide.with(getContext()).load(R.drawable.cover).into((ImageView) rootView.findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
        init();
        return rootView;
    }

    private void init() {
        list_orders = (RecyclerView) rootView.findViewById(R.id.list_products);
        adapter = new OrderAdapter(getContext(), dataList, "0");
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        list_orders.setLayoutManager(mLayoutManager);
        list_orders.addItemDecoration(new Inventory.GridSpacingItemDecoration(1, dpToPx(10), true));
        list_orders.setItemAnimator(new DefaultItemAnimator());
        list_orders.setAdapter(adapter);
        sendRequest(BASE_URL_DEFAULT + "getInventory.php");
    }

    private void sendRequest(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
//                            Log.e("Last Orders", response);
                            JSONArray jA = new JSONArray(response);
                            int i = 0;
                            Log.e("JALength", jA.length()+"");
                            while (i < jA.length()) {
                                String entity_id = jA.getJSONObject(i).getString("entity_id");
                                Orders order = new Orders();
                                order.setOrder_id(jA.getJSONObject(i).getString("sku"));
                                order.setThumbnail(R.drawable.bag1 + "");
                                while (jA.getJSONObject(i).getString("entity_id").contentEquals(entity_id)) {
                                    String attribb = jA.getJSONObject(i).getString("attribute_id");
                                    Log.e("Attribute", jA.getJSONObject(i).getString("attribute_id"));
                                    if (attribb.contentEquals("71")) {
                                        Log.e("TAG", jA.getJSONObject(i).getString("value"));
                                        order.setName(jA.getJSONObject(i).getString("value"));
                                    }
                                        else if (attribb.contentEquals("86")) {
                                        Log.e("TAG", jA.getJSONObject(i).getString("value"));
                                            order.setThumbnail(jA.getJSONObject(i).getString("value"));
                                        }else if (attribb.contentEquals("75")) {
                                        Log.e("TAG", jA.getJSONObject(i).getString("value"));
                                            order.setPrice(Double.parseDouble(jA.getJSONObject(i).getString("value")));
                                        }
                                    i++;
                                    if (i >= jA.length()) break;
                                }
                                if (i >= jA.length()) break;
                                Log.e("VALUE OF I", i+"");
                                dataList.add(order);
//                                break;
                            }
                            Log.e("SIZE", dataList.size()+"");
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Log.e("ERROR", e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error receiving data!", Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                params.put("seller_id", sp.getString("seller_id", "24"));
                params.put("count", "20");
                params.put("page", "0");
                return params;
            }};
        Singleton.getInstance().addToRequestQueue(stringRequest);
    }

    private JSONArray sortJSONArray(String response) {
        JSONArray jsonArr = null;
        try {
            jsonArr = new JSONArray(response);
            JSONArray sortedJsonArray = new JSONArray();

            List<JSONObject> jsonValues = new ArrayList<JSONObject>();
            for (int i = 0; i < jsonArr.length(); i++) {
                jsonValues.add(jsonArr.getJSONObject(i));
            }
            Collections.sort( jsonValues, new Comparator<JSONObject>() {
                //You can change "Name" with "ID" if you want to sort by ID
                private static final String KEY_NAME = "entity_id";

                @Override
                public int compare(JSONObject a, JSONObject b) {
                    String valA = "", valB = "";

                    try {
                        valA = (String) a.get(KEY_NAME);
                        valB = (String) b.get(KEY_NAME);
                    }
                    catch (JSONException e) {
                        //do something
                    }

                    return valA.compareTo(valB);
                }
            });

            for (int i = 0; i < jsonArr.length(); i++) {
                sortedJsonArray.put(jsonValues.get(i));
            }
            return sortedJsonArray;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }


    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);
        TextView title = (TextView) rootView.findViewById(R.id.title_collapsable);
        TextView subtitle = (TextView) rootView.findViewById(R.id.subtitle_collapsable);
        title.setText("Your Inventory");
        subtitle.setText("All your products");


        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) rootView.findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.inventory));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}