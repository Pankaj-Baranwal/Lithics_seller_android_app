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
import com.fame.plumbum.lithicsin.model.ChatTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by pankaj on 24/7/16.
 */
public class Chat_adapter extends BaseAdapter {
    private Context context;
    private List<ChatTable> chats;

    public Chat_adapter(Context context, List<ChatTable> chats){
        this.context = context;
        this.chats= chats;
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
        RelativeLayout rl_chat = (RelativeLayout)rowView.findViewById(R.id.rL_chat);
        TextView sender_name = (TextView) rowView.findViewById(R.id.sender_name);
        TextView message = (TextView) rowView.findViewById(R.id.message);
        TextView timestamp = (TextView) rowView.findViewById(R.id.timestamp);
        RelativeLayout rl_chat_sent = (RelativeLayout)rowView.findViewById(R.id.rL_chat_sent);
        TextView sender_name_sent = (TextView) rowView.findViewById(R.id.sender_name_sent);
        TextView message_sent = (TextView) rowView.findViewById(R.id.message_sent);
        TextView timestamp_sent = (TextView) rowView.findViewById(R.id.timestamp_sent);
        if (chats.get(position).getStatus()==1) {
            rl_chat_sent.setVisibility(View.GONE);
            rl_chat.setVisibility(View.VISIBLE);
            sender_name.setText(chats.get(position).getName());
            message.setText(chats.get(position).getMessage());

            try {
                timestamp.setText(getActualTimestamp(chats.get(position).getTimestamp()));
            } catch (ParseException ignored) {
            }
        }else{
            rl_chat_sent.setVisibility(View.VISIBLE);
            rl_chat.setVisibility(View.GONE);
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            sender_name_sent.setText(sp.getString("my_name", ""));
            message_sent.setText(chats.get(position).getMessage());
            try {
                timestamp_sent.setText(getActualTimestamp(chats.get(position).getTimestamp()));
            } catch (ParseException ignored) {
            }
        }

        return rowView;
    }

    private String getActualTimestamp(String timestamp) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(context.getResources().getString(R.string.date_format), Locale.US);
        Date date = sdf.parse(timestamp);
        long millis = date.getTime();
        long current_millis = (new Date()).getTime();
        int minutes = (int) ((current_millis - millis)/60000);
        int hours = (int) (((current_millis - millis)/60000)/60);
        int days = (int) (((current_millis - millis)/3600000)/24);
        if (minutes<60){
            return (minutes + " min ago");
        }else if (hours<24){
            return (minutes + " hrs ago");
        }else{
            return (days + " days ago");
        }
    }
}