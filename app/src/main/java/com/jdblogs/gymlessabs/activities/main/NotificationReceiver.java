package com.jdblogs.gymlessabs.activities.main;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.jdblogs.gymlessabs.R;

/**
 * Created by Jonny on 29/07/2017.
 */

public class NotificationReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setContentTitle("Workout Reminder")
                        .setContentText("Check your ab routine progress")
                        .setSmallIcon(R.drawable.ic_exercise);

        Intent resultIntent = new Intent(context,HomeActivity.class);


        PendingIntent pendingIntent = PendingIntent.getActivity(context,100,resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        mBuilder.setContentIntent(pendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(100, mBuilder.build());

    }

}
