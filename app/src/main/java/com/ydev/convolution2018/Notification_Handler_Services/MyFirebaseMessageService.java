package com.ydev.convolution2018.Notification_Handler_Services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ydev.convolution2018.Home;
import com.ydev.convolution2018.Notification.NotificationFragment;
import com.ydev.convolution2018.R;
import com.ydev.convolution2018.MainActivity;

/**
 * Created by YRT on 19/10/2017.
 */

public class MyFirebaseMessageService extends FirebaseMessagingService {

    @SuppressLint("NewApi")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        /*
        String TAG = "FCM";
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Message:"+ remoteMessage.getNotification().getBody());



        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
*/

        //Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("Notification", "Message data payload: " + remoteMessage.getData().get("Data"));
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.convolutionlogo);
        builder.setContentText(remoteMessage.getNotification().getBody());
        builder.setContentTitle("Convolution 2018");

        builder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
        builder.setColor(Color.rgb(255, 255, 255));

        //User Click on Notification
        Intent intent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify("Mynotify", 123, builder.build());

    }


}
