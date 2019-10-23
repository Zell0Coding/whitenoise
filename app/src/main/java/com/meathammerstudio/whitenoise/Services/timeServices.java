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

    private final int STOPTIMER = 100;
    private final int BREAK = 0;
    private int state;

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
        state = BREAK;

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
                try{
                    Intent intent = new Intent();

                    long h = ((millisUntilFinished/1000)/3600);
                    long m = ((millisUntilFinished/1000)%3600)/60;
                    long s = ((millisUntilFinished/1000)%3600)%60;

                    String hours = (h<9)?"0"+ h : "" + h;
                    String minute = (m<9)?"0"+ m : "" + m;
                    int s_;

                    if(s%2==0){
                        s_ = 1;
                    }else{
                        s_ = 0;
                    }

                    intent.putExtra("hours",hours);
                    intent.putExtra("minute",minute);
                    intent.putExtra("dots",s_);

                    mPendingIntent.send(getApplicationContext(),200,intent);
                }catch (PendingIntent.CanceledException e){
                    Log.d("Закрыть","ERROR");
                }

            }

            @Override
            public void onFinish() {
                state = STOPTIMER;
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
            mPendingIntent.send(state);
        }catch (PendingIntent.CanceledException e){

        }

        super.onDestroy();
    }

    @Override
    public void onLowMemory() {

    }
}
