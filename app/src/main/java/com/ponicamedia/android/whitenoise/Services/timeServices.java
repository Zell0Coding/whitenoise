package com.ponicamedia.android.whitenoise.Services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ponicamedia.android.whitenoise.Models.Sound;
import com.ponicamedia.android.whitenoise.Models.SoundContainer;
import com.ponicamedia.android.whitenoise.Models.SoundListiner;
import com.ponicamedia.android.whitenoise.Models.TimerContainer;
import com.ponicamedia.android.whitenoise.Utills.Manager;
import com.ponicamedia.android.whitenoise.Utills.StorageManager;
import com.ponicamedia.android.whitenoise.Utills.Utill;

import java.util.Timer;
import java.util.TimerTask;


public class timeServices extends Service {

    private Manager mManager;
    //private CountDownTimer timer;
    private PendingIntent mPendingIntent;

    private Timer mTimer;

    private final int STOPTIMER = 100;
    private final int BREAK = 0;

    private  long millisecund;
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


        long minute = (mManager.getCurrent_timer_hour()*60) + mManager.getCurrent_timer_minute();
        millisecund = minute * 60;

        Log.d("millisecund",millisecund+"");

        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new mainTask(),0,1000);


    }

    private class mainTask extends TimerTask
    {
        public void run()
        {
            if(millisecund>0){
                try{
                    millisecund = millisecund - 1;
                    Log.d("Осталось", "" + millisecund);

                    Intent intent = new Intent();
                    long h = ((millisecund)/3600);
                    long m = ((millisecund)%3600)/60;
                    long s = ((millisecund)%3600)%60;

                    mManager.setCurrent_timer_hour(h);
                    mManager.setCurrent_timer_minute(m);

                    String hours = (h<109)?"0"+ h : "" + h;
                    String minute = (m<10)?"0"+ m : "" + m;
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
                    e.printStackTrace();
                }
            }else{
                state = STOPTIMER;
                loadSound();
                loadTimer();
                mManager.setTimerEnabled(false);
                stopSelf();
            }

        }
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
        soundListiner.stopAllSoundTimer();

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

        if(!mManager.isTimerEnabled()){
            mTimer.cancel();
            Log.d("Закрыть","закрыть");
            try{
                mPendingIntent.send(state);
            }catch (PendingIntent.CanceledException e){

            }

        }else{

            Intent broadcastIntent = new Intent();
            broadcastIntent.putExtra("PARAM_INTENT",mPendingIntent);
            sendBroadcast(broadcastIntent);
        }
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {

        Intent broadcastIntent = new Intent();
        broadcastIntent.putExtra("PARAM_INTENT",mPendingIntent);
        sendBroadcast(broadcastIntent);

    }
}
