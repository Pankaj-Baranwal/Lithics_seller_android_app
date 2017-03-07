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

import com.fame.plumbum.lithicsin.R;
import com.fame.plumbum.lithicsin.adapters.Chat_adapter;
import com.fame.plumbum.lithicsin.database.ChatTable;
import com.fame.plumbum.lithicsin.database.DBHandler;

import java.util.List;

/**
 * Created by pankaj on 26/2/17.
 */

public class MessageExchange extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    String remote_id, remote_name;
    ListView chat_list;
    Chat_adapter adapter;
    List<ChatTable> chatTables;
    String time_created;
    public SwipeRefreshLayout swipeRefreshLayout;
    MessageExchange.MyReceiver receiver;


    @Override
    public void onRefresh() {
//        refreshUpdate();
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
        remote_name = getIntent().getExtras().getString("remote_name");
        final ImageButton sendChat = (ImageButton) findViewById(R.id.add_button);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
//                                        refreshUpdate();
                                    }
                                }
        );
        sendChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String add_chat_text = add_chat.getText().toString();
                if (add_chat_text.length()>0){
                    add_chat.setText("");
//                    sendChat(add_chat_text.replace(" ", "%20"));
                }
            }
        });
        refresh();
    }

    private void refresh(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        DBHandler db = new DBHandler(this);
        chatTables = db.getChat(remote_id);
//        CircleImageView user_img_self = (CircleImageView)findViewById(R.id.image_user_self);
//        CircleImageView user_img_remote = (CircleImageView)findViewById(R.id.image_user_remote);
//        getImage(sp.getString("uid", ""), true, user_img_self);
//        getImage(remote_id, false, user_img_remote);
        adapter = new Chat_adapter(this, chatTables);
        db.close();
        chat_list.setAdapter(adapter);
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

//    private void refreshUpdate(){
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
//        DBHandler db = new DBHandler(this);
//        chatTables = db.getChat(remote_id);
//        CircleImageView user_img_self = (CircleImageView)findViewById(R.id.image_user_self);
//        CircleImageView user_img_remote = (CircleImageView)findViewById(R.id.image_user_remote);
//        getImage(sp.getString("uid", ""), true, user_img_self);
//        getImage(remote_id, false, user_img_remote);
//        adapter = new Chat_adapter(this, chatTables);
//        //adapter.chats.add(new ChatTable());
//        db.close();
//        chat_list.post(new Runnable() {
//            @Override
//            public void run() {
//                // Select the last row so it will scroll into view...
//                chat_list.setAdapter(adapter);
//                chat_list.setSelection(adapter.getCount() - 1);
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

//    private void sendChat(final String message){
//        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
//        //SimpleDateFormat sdf = new SimpleDateFormat("MMM dd");
//        SimpleDateFormat sdf = new SimpleDateFormat(getResources().getString(R.string.date_format));
//        time_created = sdf.format(new Date());
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.BASE_URL_DEFAULT,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jO = new JSONObject(response);
//                            getDetails(message);
//                        } catch (JSONException ignored) {
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(MessageExchange.this, "Error sending data!", Toast.LENGTH_SHORT).show();
//            }
//        });
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        Singleton.getInstance().addToRequestQueue(stringRequest);
//    }

    private void getDetails(String message) {
        DBHandler db = new DBHandler(this);
        int status = 1;
        ChatTable chatTable = new ChatTable(status, remote_id, remote_name, message.replace("%20", " "), time_created.replace("%20", " "));
        db.addChat(chatTable);
        db.close();
        chatTables.add(chatTable);
        adapter.chats = chatTables;
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
//            ChatTable chatTable = new ChatTable(, intent.getExtras().getString("remote_id"), intent.getExtras().getString("remote_name"), intent.getExtras().getString("message"), intent.getExtras().getString("created_at"));
//            db.addChat(chatTable);
            db.close();
//            chatTables.add(chatTable);
//            adapter.chats = chatTables;
//            adapter.notifyDataSetChanged();
//            chat_list.setSelection(adapter.getCount() - 1);
        }
    }
}