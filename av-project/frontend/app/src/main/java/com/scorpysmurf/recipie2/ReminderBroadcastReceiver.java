package com.scorpysmurf.recipie2;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;

public class ReminderBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("com.scorpysmurf.recipie2.groceries", Context.MODE_PRIVATE);
        ArrayList<String> groceryArray;

        if (sharedPreferences.getStringSet("groceries",null) == null) {
            groceryArray = new ArrayList<>();
        } else {
            groceryArray = new ArrayList<>(sharedPreferences.getStringSet("groceries",null));
        }

        if(groceryArray.size() > 0) {
            Intent intent1 = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, App.CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)
                    .setContentTitle(context.getString(R.string.groceries))
                    .setContentText(context.getString(R.string.you_still_have_items))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

            notificationManagerCompat.notify(2, builder.build());
        }

    }
}
