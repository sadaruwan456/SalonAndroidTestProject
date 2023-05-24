package com.dilanka456.myprojectcustomer10.fcmHelper;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class fbMessagingService extends FirebaseMessagingService {
    private static final String TAG = "fbMessagingService";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
            } else {
                // Handle message within 10 seconds
//                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

try {
    Toast.makeText(this.getApplicationContext(), "NEW Message : "+
            remoteMessage.getNotification().getTitle()+" "
            +remoteMessage.getNotification().getBody(), Toast.LENGTH_SHORT).show();
}catch (Exception e){

}
        }
}

}
