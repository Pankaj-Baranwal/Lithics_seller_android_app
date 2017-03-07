package com.fame.plumbum.lithicsin.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fame.plumbum.lithicsin.R;
import com.fame.plumbum.lithicsin.database.ChatTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by pankaj on 24/7/16.
 */
public class Chat_adapter extends BaseAdapter {
    private Context context;
    public List<ChatTable> chats;
    int total = 0;

    public Chat_adapter(Context context, List<ChatTable> chats){
        this.context = context;
        this.chats= chats;
        total = chats.size();
    }
    @Override
    public int getCount() {
        return chats.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void setChats(ChatTable chat ) {
        chats.add(chat);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_chat, parent, false);
        RelativeLayout rl_caht = (RelativeLayout)rowView.findViewById(R.id.rL_chat);
        TextView sender_name = (TextView) rowView.findViewById(R.id.sender_name);
        TextView message = (TextView) rowView.findViewById(R.id.message);
        TextView timestamp = (TextView) rowView.findViewById(R.id.timestamp);
        RelativeLayout rl_chat_sent = (RelativeLayout)rowView.findViewById(R.id.rL_chat_sent);
        TextView sender_name_sent = (TextView) rowView.findViewById(R.id.sender_name_sent);
        TextView message_sent = (TextView) rowView.findViewById(R.id.message_sent);
        TextView timestamp_sent = (TextView) rowView.findViewById(R.id.timestamp_sent);
        if (chats.get(position).getStatus()==2) {
            rl_chat_sent.setVisibility(View.GONE);
            rl_caht.setVisibility(View.VISIBLE);
            sender_name.setText(toProperCase(chats.get(position).getRemote_name().replace("%20", " ")));
            message.setText(chats.get(position).getMessage().replace("%20", " "));
            SimpleDateFormat sdf = new SimpleDateFormat(context.getResources().getString(R.string.date_format));
            Date date = null;
            try {
                date = sdf.parse(chats.get(position).getTimestamp());
            } catch (ParseException ignored) {
            }
            long millis = date.getTime();
            long current_millis = (new Date()).getTime();
            int minutes = (int) ((current_millis - millis)/60000);
            int hours = (int) (((current_millis - millis)/60000)/60);
            int days = (int) (((current_millis - millis)/3600000)/24);
            if (minutes<60){
                timestamp.setText(minutes + " min ago");
            }else if (hours<24){
                timestamp.setText(minutes + " hrs ago");
            }else{
                timestamp.setText(days + " days ago");
            }
        }else{
            rl_chat_sent.setVisibility(View.VISIBLE);
            rl_caht.setVisibility(View.GONE);
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            sender_name_sent.setText(sp.getString("user_name", "").replace("%20", " "));
            message_sent.setText(chats.get(position).getMessage().replace("%20", " "));
            SimpleDateFormat sdf = new SimpleDateFormat(context.getResources().getString(R.string.date_format));
            Date date = null;
            try {
                date = sdf.parse(chats.get(position).getTimestamp());
            } catch (ParseException ignored) {
            }
            long millis = date.getTime();
            long current_millis = (new Date()).getTime();
            int minutes = (int) ((current_millis - millis)/60000);
            int hours = (int) (((current_millis - millis)/60000)/60);
            int days = (int) (((current_millis - millis)/3600000)/24);
            if (minutes<60){
                timestamp_sent.setText(minutes + " min ago");
            }else if (hours<24){
                timestamp_sent.setText(minutes + " hrs ago");
            }else{
                timestamp_sent.setText(days + " days ago");
            }
        }

        return rowView;
    }

    private String toProperCase(String name) {
        if (name!=null && name.length()>0) {
            name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
            for (int i = 0; ; ) {
                i = name.indexOf(" ", i + 1);
                if (i < 0)
                    break;
                else {
                    if (i < name.length()-2)
                        name = name.substring(0, i + 1) + name.substring(i + 1, i + 2).toUpperCase() + name.substring(i + 2);
                    else if (i == name.length()-2) {
                        name = name.substring(0, i + 1) + name.substring(i + 1, i + 2).toUpperCase();
                        break;
                    }
                }
            }
        }
        return name;
    }
}