package com.meathammerstudio.whitenoise.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.meathammerstudio.whitenoise.Utills.Manager;

public class musicServices extends Service {

    private static final String MUSIC = null;
    private Manager mManager;

    public IBinder onBind(Intent arg0) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
    public int onStartCommand(Intent intent, int flags, int startId) {

        mManager = Manager.getInstance();
        return musicServices.START_STICKY;
    }


    public IBinder onUnBind(Intent arg0) {
        // TO DO Auto-generated method
        return null;
    }

    public void onStop() {

    }
    public void onPause() {

    }
    @Override
    public void onDestroy() {
        Log.d("Остановки сервиса песен","STOP");
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {

    }
}
