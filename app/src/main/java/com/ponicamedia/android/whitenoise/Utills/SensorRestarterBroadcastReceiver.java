package com.ponicamedia.android.whitenoise.Utills;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ponicamedia.android.whitenoise.Services.timeServices;

public class SensorRestarterBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(SensorRestarterBroadcastReceiver.class.getSimpleName(), "Service Stops! Oops!!!!");

        Intent service = new Intent(context, timeServices.class);
        PendingIntent pendingIntent = intent.getParcelableExtra("PARAM_INTENT");
        intent.putExtra("PARAM_INTENT", pendingIntent);
        context.startService(service);

    }

}
