package com.fame.plumbum.lithicsin.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fame.plumbum.lithicsin.R;
import com.fame.plumbum.lithicsin.Singleton;
import com.fame.plumbum.lithicsin.adapters.SellersAdapter;
import com.fame.plumbum.lithicsin.model.Sellers;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fame.plumbum.lithicsin.utils.Constants.BASE_URL_DEFAULT;

/**
 * Project Name: 	<Visual Perception For The Visually Impaired>
 * Author List: 		Pankaj Baranwal
 * Filename: 		<>
 * Functions: 		<>
 * Global Variables:	<>
 */
public class MessageExchangeList extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    List<Sellers> sellersList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_list);
        init();
    }

    private void init() {
        sendRequest(BASE_URL_DEFAULT + "getAllSellersSku.php");
    }

    private void sendRequest(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jA = new JSONArray(response);
                            for (int i = 0; i<jA.length(); i++){
                                sellersList.add(new Sellers(jA.getJSONObject(i).getString("id"), jA.getJSONObject(i).getString("seller_name"), jA.getJSONObject(i).getString("contact_person"), jA.getJSONObject(i).getString("seller_id"), jA.getJSONObject(i).getString("seller_sku")));
                            }
                            Log.e(getClass().getName(), jA.length() + " " + sellersList.size());
                            RecyclerView list_orders = (RecyclerView) findViewById(R.id.sellers_list);
                            list_orders.setLayoutManager(new LinearLayoutManager(MessageExchangeList.this));
                            adapter = new SellersAdapter(MessageExchangeList.this, sellersList);
                            list_orders.setItemAnimator(new DefaultItemAnimator());
                            list_orders.setAdapter(adapter);
                        } catch (JSONException e) {
                            Log.e("ERROR", e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MessageExchangeList.this, "Error receiving data!", Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MessageExchangeList.this);
                params.put("id", sp.getString("id", ""));
                return params;
            }};
        Singleton.getInstance().addToRequestQueue(stringRequest);
    }
}
