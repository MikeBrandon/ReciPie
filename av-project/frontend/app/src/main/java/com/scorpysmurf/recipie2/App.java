package com.scorpysmurf.recipie2;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class App extends Application {

    public static final String CHANNEL_ID = "timerChannel";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel timerChannel = new NotificationChannel(
                    CHANNEL_ID,
                    getString(R.string.timer),
                    NotificationManager.IMPORTANCE_HIGH
            );

            timerChannel.setDescription("This is for Timer Notifications.");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(timerChannel);
            
        }

    }
}
