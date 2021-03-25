package com.scorpysmurf.recipie2;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class TimerService extends Service {

    private NotificationManagerCompat notificationManagerCompat;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        notificationManagerCompat = NotificationManagerCompat.from(this);

        int time = intent.getIntExtra("time",0);
        String name = intent.getStringExtra("recipe");

        Intent notificationIntent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,notificationIntent,0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, App.CHANNEL_ID);

        Notification notification = builder
                .setContentTitle(getString(R.string.recipie_timer))
                .setContentText(getString(R.string.timer_run) + ": " + ((name != null)? name:""))
                .setSmallIcon(R.raw.timerb1)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1,notification);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent notificationCompleteIntent = new Intent(TimerService.this,MainActivity.class);
                PendingIntent pendingCompleteIntent = PendingIntent.getActivity(TimerService.this, 0,notificationCompleteIntent,0);

                Notification notificationComplete = builder
                        .setSmallIcon(R.raw.timerb1)
                        .setContentTitle(getString(R.string.recipie_timer))
                        .setContentText(getString(R.string.recipie_desc) + " " + ((name != null)? name:(getString(R.string.food))))
                        .setContentIntent(pendingCompleteIntent)
                        .setColor(getColor(R.color.pink100))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_ALARM)
                        .build();

                notificationManagerCompat.notify(1, notificationComplete);

                stopSelf();
            }
        },time * 1000);

        return START_NOT_STICKY;

    }
}
