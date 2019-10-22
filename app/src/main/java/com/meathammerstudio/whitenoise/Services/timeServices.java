package com.meathammerstudio.whitenoise.Services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.meathammerstudio.whitenoise.Controllers.TimerFragment;
import com.meathammerstudio.whitenoise.Models.SoundContainer;
import com.meathammerstudio.whitenoise.Models.SoundListiner;
import com.meathammerstudio.whitenoise.Models.TimerContainer;
import com.meathammerstudio.whitenoise.Utills.Manager;
import com.meathammerstudio.whitenoise.Utills.StorageManager;
import com.meathammerstudio.whitenoise.Utills.Utill;


public class timeServices extends Service {

    private Manager mManager;
    private CountDownTimer timer;
    private PendingIntent mPendingIntent;

    public IBinder onBind(Intent arg0) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
    public int onStartCommand(Intent intent, int flags, int startId) {

        mManager = Manager.getInstance();
        startTimer();

        mPendingIntent = intent.getParcelableExtra("PARAM_INTENT");

        return musicServices.START_STICKY;
    }

    private void startTimer(){

        int minute = (mManager.getCurrent_timer_hour()*60) + mManager.getCurrent_timer_minute();
        final long millisecund = minute * 60 * 1000;

        Log.d("millisecund",millisecund+"");
        timer = new CountDownTimer(millisecund,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("осталось",millisUntilFinished/1000+"");
            }

            @Override
            public void onFinish() {
                loadSound();
                loadTimer();
                stopSelf();
                getApplicationContext().stopService(new Intent(getApplicationContext(),musicServices.class));
            }
        }.start();

    }

    private void loadSound(){
        try {
            String data = StorageManager.readFromFile(Utill.SELECTED_SOUND, getApplicationContext());
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            SoundContainer soundContainer = gson.fromJson(data,SoundContainer.class);
            saveSound(soundContainer);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }
    private void saveSound(SoundContainer soundContainer){

        SoundListiner soundListiner = mManager.getSoundListiner();
        soundListiner.stopAllSound();

        for(int i = 1; i < soundContainer.mSoundList.size();i++){
            soundContainer.mSoundList.get(i).setEnabled(false);
        }
        Gson gson = new Gson();
        String json_data = gson.toJson(soundContainer);
        StorageManager.writeToFile(Utill.SELECTED_SOUND,json_data,getApplicationContext());
    }

    private void loadTimer(){
        try {
            String data = StorageManager.readFromFile(Utill.TIMER_SELECTED, getApplicationContext());
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            TimerContainer timerContainer = gson.fromJson(data,TimerContainer.class);
            saveTime(timerContainer);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }
    private void saveTime(TimerContainer timerContainer){
        for(int i = 0; i < timerContainer.mTimerList.size();i++){
            timerContainer.mTimerList.get(i).setEnable(false);
        }
        Gson gson = new Gson();
        String json_data = gson.toJson(timerContainer);
        StorageManager.writeToFile(Utill.TIMER_SELECTED,json_data,getApplicationContext());
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
        timer.cancel();
        Log.d("Закрыть","закрыть");
        try{
            mPendingIntent.send(100);
        }catch (PendingIntent.CanceledException e){

        }

        super.onDestroy();
    }

    @Override
    public void onLowMemory() {

    }
}
