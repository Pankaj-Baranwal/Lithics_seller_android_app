package com.fame.plumbum.lithicsin.fragments;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.fame.plumbum.lithicsin.interfaces.Load_more;
import com.fame.plumbum.lithicsin.model.ChatTable;
import com.fame.plumbum.lithicsin.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project Name: 	<Visual Perception For The Visually Impaired>
 * Author List: 		Pankaj Baranwal
 * Filename: 		<>
 * Functions: 		<>
 * Global Variables:	<>
 */
public class MessageExchange extends Fragment implements Load_more, SwipeRefreshLayout.OnRefreshListener {

    String remote_id, name;
    ListView chat_list;
    Chat_adapter adapter;
    List<ChatTable> chatTables;
    public SwipeRefreshLayout swipeRefreshLayout;
    MessageExchange.MyReceiver receiver;
    View rootView;

    @Override
    public void onRefresh() {
        refreshUpdate();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.particulat_chat, container, false);

        receiver = new MessageExchange.MyReceiver();
        getContext().registerReceiver(receiver, new IntentFilter("MyReceiver"));

        init();
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        getContext().unregisterReceiver(receiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        getContext().registerReceiver(receiver, new IntentFilter("MyReceiver"));
    }

    private void init(){
        chat_list = (ListView) rootView.findViewById(R.id.list_chat);
        final EditText add_chat = (EditText) rootView.findViewById(R.id.chat_add);
        remote_id = getActivity().getIntent().getExtras().getString("remote_id");
        name = getActivity().getIntent().getExtras().getString("name");
        final ImageButton sendChat = (ImageButton) rootView.findViewById(R.id.add_button);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe);
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
        DBHandler db = new DBHandler(getContext());
        chatTables = db.getChat(remote_id);
//        CircleImageView user_img_self = (CircleImageView)findViewById(R.id.image_user_self);
//        CircleImageView user_img_remote = (CircleImageView)findViewById(R.id.image_user_remote);
//        getImage(sp.getString("uid", ""), true, user_img_self);
//        getImage(remote_id, false, user_img_remote);
        adapter = new Chat_adapter(getContext(), chatTables);
        db.close();
        chat_list.setAdapter(adapter);
        chat_list.setSelection(adapter.getCount() - 1);
    }

    private void refreshUpdate(){
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        DBHandler db = new DBHandler(getContext());
        chatTables = db.getChat(remote_id);

        adapter.notifyDataSetChanged();
        chat_list.setSelection(adapter.getCount() - 1);

        db.close();
    }

    private void sendChat(final String message){
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
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
                Toast.makeText(getContext(), "Error sending data!", Toast.LENGTH_SHORT).show();
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
        DBHandler db = new DBHandler(getContext());
        int status = 2;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
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
                    (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
            DBHandler db = new DBHandler(getContext());
            ChatTable chatTable = new ChatTable(intent.getExtras().getInt("status"), intent.getExtras().getString("remote_id"), intent.getExtras().getString("name"), intent.getExtras().getString("message"), intent.getExtras().getString("timestamp"));
            db.addChat(chatTable);
            db.close();
            chatTables.add(chatTable);
            adapter.notifyDataSetChanged();
            chat_list.setSelection(adapter.getCount() - 1);
        }
    }

    @Override
    public void onInterfaceClick() {

    }
}