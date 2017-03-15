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
import android.view.Menu;
import android.view.MenuItem;
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
import com.fame.plumbum.lithicsin.model.ChatTable;
import com.fame.plumbum.lithicsin.database.DBHandler;
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

    String remote_id, name;
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
        registerReceiver(receiver, new IntentFilter("MyReceiver"));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

        }
        return super.onOptionsItemSelected(item);
    }

    private void init(){
        chat_list = (ListView) findViewById(R.id.list_chat);
        final EditText add_chat = (EditText) findViewById(R.id.chat_add);
        remote_id = getIntent().getExtras().getString("remote_id");
        name = getIntent().getExtras().getString("name");
        final ImageButton sendChat = (ImageButton) findViewById(R.id.add_button);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);
//        swipeRefreshLayout.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        swipeRefreshLayout.setRefreshing(true);
//                                        refreshUpdate();
//                                    }
//                                }
//        );
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
        chatTables = db.getChat(remote_id);
//        CircleImageView user_img_self = (CircleImageView)findViewById(R.id.image_user_self);
//        CircleImageView user_img_remote = (CircleImageView)findViewById(R.id.image_user_remote);
//        getImage(sp.getString("uid", ""), true, user_img_self);
//        getImage(remote_id, false, user_img_remote);
        adapter = new Chat_adapter(this, chatTables);
        db.close();
        chat_list.setAdapter(adapter);
        chat_list.setSelection(adapter.getCount() - 1);
    }

//    private String getImage(String uid, final boolean local, final CircleImageView user_img) {
//        final String[] image_name = new String[1];
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.BASE_URL_DEFAULT + "ImageName?UserId=" + uid,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject json = new JSONObject(response);
//                            image_name[0] = json.getString("ImageName");
//                            if (local)
//                                picassoLocal(image_name[0], user_img);
//                            else
//                                picassoGlobal(image_name[0], user_img);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(MessageExchange.this, "Error receiving data!", Toast.LENGTH_SHORT).show();
//            }
//        });
//        Singleton.getInstance().addToRequestQueue(stringRequest);
//        return image_name[0];
//    }

//    private void picassoGlobal(String s, CircleImageView user_img) {
//        Picasso.with(this).load(Constants.BASE_URL_DEFAULT + "ImageReturn?ImageName="+s).resize(256,256).error(R.drawable.user).into(user_img);
//    }
//
//    private void picassoLocal(String s, CircleImageView user_img) {
//        Picasso.with(this).load(Constants.BASE_URL_DEFAULT + "ImageReturn?ImageName="+s).resize(256,256).error(R.drawable.user).into(user_img);
//    }

    private void refreshUpdate(){
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        DBHandler db = new DBHandler(this);
        chatTables = db.getChat(remote_id);
//        CircleImageView user_img_self = (CircleImageView)findViewById(R.id.image_user_self);
//        CircleImageView user_img_remote = (CircleImageView)findViewById(R.id.image_user_remote);
//        getImage(sp.getString("uid", ""), true, user_img_self);
//        getImage(remote_id, false, user_img_remote);
        adapter.notifyDataSetChanged();
        chat_list.setSelection(adapter.getCount() - 1);
        //adapter.chats.add(new ChatTable());
        db.close();
//        chat_list.post(new Runnable() {
//            @Override
//            public void run() {
//                // Select the last row so it will scroll into view...
//                chat_list.setAdapter(adapter);
//                chat_list.setSelection(adapter.getCount() - 1);
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void sendChat(final String message){
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL_DEFAULT + "sendViaFirebase.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jO = new JSONObject(response);
                            getDetails(message, jO.getString("timestamp"));
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
                params.put("my_id", sp.getString("id", ""));
                params.put("my_name", sp.getString("my_name", ""));
                return params;
            }
        };
        Singleton.getInstance().addToRequestQueue(stringRequest);
    }

    private void getDetails(String message, String timestamp) {
        DBHandler db = new DBHandler(this);
        int status = 2;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        ChatTable chatTable = new ChatTable(status, sp.getString("id", ""), sp.getString("my_name", ""), message, timestamp);
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
            ChatTable chatTable = new ChatTable(intent.getExtras().getInt("status"), intent.getExtras().getString("remote_id"), intent.getExtras().getString("name"), intent.getExtras().getString("message"), intent.getExtras().getString("timestamp"));
            db.addChat(chatTable);
            db.close();
            chatTables.add(chatTable);
            adapter.notifyDataSetChanged();
            chat_list.setSelection(adapter.getCount() - 1);
        }
    }
}