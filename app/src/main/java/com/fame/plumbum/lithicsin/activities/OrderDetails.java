package com.fame.plumbum.lithicsin.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fame.plumbum.lithicsin.R;
import com.fame.plumbum.lithicsin.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.fame.plumbum.lithicsin.utils.Constants.BASE_URL_DEFAULT;

/**
 * Project Name: 	<Visual Perception For The Visually Impaired>
 * Author List: 		Pankaj Baranwal
 * Filename: 		<>
 * Functions: 		<>
 * Global Variables:	<>
 */
public class OrderDetails extends AppCompatActivity{
    
    TextView order_id, order_date, order_status, customer_name, email_id, product_name, sku, customer_address;
    String increment_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        increment_id = getIntent().getExtras().getString("increment_id");
        setContentView(R.layout.fragment_specific_order);
        init();
    }

    private void init() {
        order_id = (TextView) findViewById(R.id.order_id);
        order_date = (TextView) findViewById(R.id.order_date);
        order_status = (TextView) findViewById(R.id.order_status);
        customer_name = (TextView) findViewById(R.id.customer_name);
        customer_address = (TextView) findViewById(R.id.customer_address);
        email_id = (TextView) findViewById(R.id.email_id);
        product_name = (TextView) findViewById(R.id.product_name);
        sku = (TextView) findViewById(R.id.sku);
        sendRequest(BASE_URL_DEFAULT + "getOrderDetails.php");
    }

    public void sendRequest(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("reponse", response);
                            if (response.contains("status")){
                                JSONObject jO = new JSONArray(response).getJSONObject(0);
                                order_id.setText(jO.getString("increment_id"));
                                order_date.setText(jO.getString("created_at").substring(0, 10));
                                order_status.setText(jO.getString("status"));
                                String cs_name = jO.getString("customer_firstname")+jO.getString("customer_lastname");
                                customer_name.setText(cs_name);
                                email_id.setText(jO.getString("customer_email"));
                                product_name.setText(jO.getString("name"));
                                sku.setText(jO.getString("sku"));
                                String cs_address = jO.getString("street") + jO.getString("city");
                                customer_address.setText(cs_address);
                            }else{
                                Toast.makeText(OrderDetails.this, "No details found!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OrderDetails.this, "Error receiving data!", Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<>();
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(OrderDetails.this);
                Log.e("increment_id", increment_id);
                params.put("seller_id", sp.getString("id", ""));
                params.put("order_id", increment_id.substring(1));
                return params;
            }};
        Singleton.getInstance().addToRequestQueue(stringRequest);
    }
}