package com.fame.plumbum.lithicsin.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.fame.plumbum.lithicsin.R;
import com.fame.plumbum.lithicsin.activities.MessageExchange;
import com.fame.plumbum.lithicsin.database.DBHandler;
import com.fame.plumbum.lithicsin.model.ChatTable;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * Created by pankaj on 15/7/16.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        sendNotification(remoteMessage.getData());
        Intent intents=new Intent();
        intents.setAction("Lithics.in_Firebase");
        intents.putExtra("status", 1);
        intents.putExtra("chat_id", remoteMessage.getData().get("chat_id"));
        intents.putExtra("message", remoteMessage.getData().get("message"));
        intents.putExtra("name", remoteMessage.getData().get("name"));
        SimpleDateFormat sdf = new SimpleDateFormat(getResources().getString(R.string.date_format), Locale.US);
        intents.putExtra("timestamp", sdf.format(new Date()));
        getBaseContext().sendBroadcast(intents);
    }



    private void sendNotification(Map<String, String> messageBody) {
        Intent intent = new Intent(this, MessageExchange.class);
        DBHandler db = new DBHandler(this);
        SimpleDateFormat sdf = new SimpleDateFormat(getResources().getString(R.string.date_format), Locale.US);
        db.addChat(new ChatTable(1, messageBody.get("chat_id"), messageBody.get("name"), messageBody.get("message"), sdf.format(new Date())));
        db.close();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("chat_id", messageBody.get("chat_id"));
        intent.putExtra("name", messageBody.get("name"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo_autocrop)
                .setContentTitle("From Lithics.in")
                .setContentText("New message")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}