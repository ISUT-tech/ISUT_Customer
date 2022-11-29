package com.isut.customer.fire_services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.isut.customer.MainActivity;
import com.isut.customer.R;

import java.util.Map;

import androidx.core.app.NotificationCompat;

public class MyFirebaseMessagingService  extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional

        if (!remoteMessage.getData().isEmpty()) {




          Log.d(TAG, "Notification Message Body: " + remoteMessage.getData().get("body"));
            Log.d(TAG, "Notification Message Body: " + remoteMessage.getData().get("extra"));

            Map<String, String> data = remoteMessage.getData();
            if(data.get("status") != null) {
                String totalFare = data.get("fair");
                String driverId = data.get("driverId");
                String bookingId = data.get("bookingId");
                if (Integer.parseInt(data.get("status")) == 1) {

                   if (data.get("isSchedule").equals("1"))
                   {
                       sendNotification("your booking schedule successfully..thank you ", "3",driverId,totalFare,bookingId);

                   }
                   else {
                       sendNotification("Your Booking Accepted Happy journey ", "1", driverId
                               , totalFare
                               , bookingId);
                   }

                }
                if (Integer.parseInt(data.get("s        ` bbtatus")) == 2) {
                    sendNotification("your booking cancelled.. try another ", "2",driverId,totalFare,bookingId);

                }
                if (Integer.parseInt(data.get("status")) == 3) {
                    sendNotification("your booking complete..thank you ", "3",driverId,totalFare,bookingId);

                }

            }
          /*  String userid =    data.get("userId");
            String driverid =    data.get("driverId");
            String name =    data.get("name");
            String source =    data.get("sourceLocation");
            String destination =    data.get("destinationLocation");
            String fair =    data.get("fair");
            int bookingid = Integer.parseInt(data.get("bookingId"));

            sendNotification( "messageBody", "data");*/


        }
        }

        //This method is only generating push notification
        //It is same as we did in earlier posts

    private void sendNotification(String messageBody,String data,String driverId,String fare,String bookingId) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("key","fromonti");
        intent.putExtra("data",data);
        intent.putExtra("driverId",driverId);
        intent.putExtra("fare",fare);
        intent.putExtra("bookingId",bookingId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "YOUR CHANNEL";
            String description = "YOUR CHANNEL DESCRIPTION";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel( "YOUR CHANNEL ID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = MyFirebaseMessagingService.this.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.isut)
        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.isut))
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
               .setFullScreenIntent(pendingIntent,true)
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
          notificationBuilder.setChannelId("YOUR CHANNEL ID");
        notificationManager.notify(0, notificationBuilder.build());
    }


    }
