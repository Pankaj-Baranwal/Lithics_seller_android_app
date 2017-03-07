package com.fame.plumbum.lithicsin.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.fame.plumbum.lithicsin.R;
import com.fame.plumbum.lithicsin.activities.MessageExchange;
import com.fame.plumbum.lithicsin.database.DBHandler;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

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

        intents.putExtra("message", remoteMessage.getData().get("message"));
        intents.putExtra("send_to", "");

        getBaseContext().sendBroadcast(intents);
    }



    private void sendNotification(Map<String, String> messageBody) {
        Intent intent = new Intent(this, MessageExchange.class);
        DBHandler db = new DBHandler(this);
        Log.e("TAG", messageBody.get("message"));
//        db.addChat(new ChatTable(messageBody.get("SenderId"), messageBody.get("Message"), messageBody.get("CreatedAt")));
        intent.putExtra("message", messageBody.get("message"));
        intent.putExtra("seller_id", messageBody.get("send_to"));

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo_autocrop)
                .setContentTitle("From Lithics.in")
                .setContentText(messageBody.get("Title"))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}