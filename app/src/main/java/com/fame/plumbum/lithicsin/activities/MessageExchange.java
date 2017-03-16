package com.fame.plumbum.lithicsin.activities;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fame.plumbum.lithicsin.R;
import com.fame.plumbum.lithicsin.Singleton;
import com.fame.plumbum.lithicsin.adapters.Chat_adapter;
import com.fame.plumbum.lithicsin.database.DBHandler;
import com.fame.plumbum.lithicsin.model.ChatTable;
import com.fame.plumbum.lithicsin.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pankaj on 26/2/17.
 */

public class MessageExchange extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    String chat_id, name;
    ListView chat_list;
    Chat_adapter adapter;
    List<ChatTable> chatTables;
    public SwipeRefreshLayout swipeRefreshLayout;
    MessageExchange.MyReceiver receiver;

    @Override
    public void onRefresh() {
        refreshUpdate();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.particulat_chat);
        receiver = new MessageExchange.MyReceiver();
        registerReceiver(receiver, new IntentFilter("Lithics.in_Firebase"));
        init();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter("MyReceiver"));
    }


    private void init(){
        chat_list = (ListView) findViewById(R.id.list_chat);
        final EditText add_chat = (EditText) findViewById(R.id.chat_add);
        chat_id = getIntent().getExtras().getString("chat_id");
        name = getIntent().getExtras().getString("name");
        final ImageButton sendChat = (ImageButton) findViewById(R.id.add_button);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);
        sendChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String add_chat_text = add_chat.getText().toString();
                if (add_chat_text.length()>0){
                    add_chat.setText("");
                    sendChat(add_chat_text);
                }
            }
        });
        DBHandler db = new DBHandler(this);
        chatTables = db.getChat(chat_id);

        adapter = new Chat_adapter(this, chatTables);
        db.close();
        chat_list.setAdapter(adapter);
        chat_list.setSelection(adapter.getCount() - 1);
    }

    private void refreshUpdate(){
        DBHandler db = new DBHandler(this);
        chatTables = db.getChat(chat_id);
        adapter.notifyDataSetChanged();
        chat_list.setSelection(adapter.getCount() - 1);
        db.close();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void sendChat(final String message){
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL_DEFAULT + "send_firebase_message.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jO = new JSONObject(response);
                            Log.e(getClass().getName(), response);
                            getDetails(message, jO.getString("timestamp"), jO.getString("chat_id"));
                        } catch (JSONException ignored) {
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MessageExchange.this, "Error sending data!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("message", message);
                params.put("send_to", "admin");
                params.put("my_id", sp.getString("id", ""));
                params.put("my_name", sp.getString("my_name", "Pankaj"));
                return params;
            }
        };
        Singleton.getInstance().addToRequestQueue(stringRequest);
    }

    private void getDetails(String message, String timestamp, String chat_id) {
        DBHandler db = new DBHandler(this);
        int status = 2;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        ChatTable chatTable = new ChatTable(status, chat_id, sp.getString("my_name", ""), message, timestamp);
        db.addChat(chatTable);
        db.close();
        chatTables.add(chatTable);
        adapter.notifyDataSetChanged();
        chat_list.setSelection(adapter.getCount() - 1);
    }

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
            DBHandler db = new DBHandler(MessageExchange.this);
            ChatTable chatTable = new ChatTable(intent.getExtras().getInt("status"), intent.getExtras().getString("chat_id"), intent.getExtras().getString("name"), intent.getExtras().getString("message"), intent.getExtras().getString("timestamp"));
            db.addChat(chatTable);
            db.close();
            chatTables.add(chatTable);
            adapter.notifyDataSetChanged();
            chat_list.setSelection(adapter.getCount() - 1);
        }
    }
}