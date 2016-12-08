package com.latentdev.uberesque;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Laten on 12/6/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        Log.d(TAG,"From: "+remoteMessage.getFrom());
        Log.d(TAG,"Message: "+remoteMessage.getNotification().getBody());
        Toast.makeText(this, "From: " + remoteMessage.getFrom(), Toast.LENGTH_LONG).show();
        Toast.makeText(this, "Message: "+remoteMessage.getNotification().getBody(), Toast.LENGTH_LONG).show();
    }
}
