package com.scorpysmurf.recipie2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class ConnectivityBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,false);

            if (noConnectivity) {
                Toast.makeText(context, R.string.disconnected, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, R.string.connected, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
