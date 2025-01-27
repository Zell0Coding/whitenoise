package com.ponicamedia.android.whitenoise.Services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Debug;
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

    /*private Manager mManager; //хранятся все песни
    private PendingIntent mPendingIntent; // интент для передачи данных для таймера

    private Timer mTimer; // сам таймер

    private static final int STOPTIMER = 100; //таймер отработан
    private static final int BREAK = 0; // таймер не отработан
    private static final int UPDATE = 200; // таймер не отработан

    private long millisecund;
    private int state;

    /*public IBinder onBind(Intent arg0) {

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
    }*/

    /*private void startTimer(){


        long minute = (mManager.getCurrent_timer_hour()*60) + mManager.getCurrent_timer_minute();
        millisecund = minute * 60;

        Log.d("millisecund",millisecund+"");

        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new mainTask(),0,1000);


    }*/

    /*private class mainTask extends TimerTask
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

                    try{
                        mPendingIntent.send(getApplicationContext(),200,intent);
                    }catch (NullPointerException e){
                        state = STOPTIMER;
                        stopSelf();
                    }

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
    }/*


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

        if(!mManager.isTimerEnabled()){
            mTimer.cancel();
            Log.d("Закрыть","закрыть");
            try{
                    mPendingIntent.send(state);
            }catch (PendingIntent.CanceledException e){
                Toast.makeText(getApplicationContext(),"ERROR",Toast.LENGTH_SHORT).show();
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



    /*
    *
    *
    * НОВЫЙ СЕРВИС ДЛЯ ТАЙМЕРА
    *
    * */



    /*public IBinder onBind(Intent arg0) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
    public int onStartCommand(Intent intent, int flags, int startId) {

        mManager = Manager.getInstance(); //обращаемся к менеджеру
        mPendingIntent = intent.getParcelableExtra("PARAM_INTENT"); //получаем сссылку на интенет
        state = BREAK; //ставим положение на "не закончено"

        startTimer(); // запускаем таймер

        return musicServices.START_NOT_STICKY;
    }

    private void startTimer(){


        millisecund = (mManager.getCurrent_timer_hour()*60) + mManager.getCurrent_timer_minute() * 60; //сколько нужно миллисекунд

        mTimer = new Timer(); //создаем таймер
        mTimer.scheduleAtFixedRate(new mainTask(),0,1000); //запускаем таймер


    }

    private class mainTask extends TimerTask
    {
        public void run()
        {
            if(millisecund>0){
                millisecund = millisecund - 1; // отнимаем 1 секунду
                Log.d("Осталось", "" + millisecund);
                Intent intent = new Intent();
                long h = ((millisecund)/3600); //сколько часов
                long m = ((millisecund)%3600)/60; //сколько минут
                long s = ((millisecund)%3600)%60; //сколько секунд

                mManager.setCurrent_timer_hour(h); //сколько осталось часов
                mManager.setCurrent_timer_minute(m); //сколько осталось минут

                String hours = (h<10)?"0"+ h : "" + h;
                String minute = (m<10)?"0"+ m : "" + m;
                int s_ = (s%2==0)? 1 : 0;


                intent.putExtra("hours",hours);
                intent.putExtra("minute",minute);
                intent.putExtra("dots",s_);

                try{
                    mPendingIntent.send(getApplicationContext(),UPDATE,intent);
                }catch (PendingIntent.CanceledException e){
                    e.printStackTrace();
                }

            }else{
                state = STOPTIMER; //останавливаем таймер
                updateFiles();
                mManager.setTimerEnabled(false);
            }

        }
    }

    private void updateFiles(){

        mManager.getSoundListiner().stopAllSound(); //останавливаем все треки
        try {

            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();

            /* ПЕРЕЗАИСЬ ФАЙЛА ТАЙМЕРОВ */

       /*     String data = StorageManager.readFromFile(Utill.TIMER_SELECTED, getApplicationContext()); //выбранные таймеры
            TimerContainer timerContainer = gson.fromJson(data,TimerContainer.class);
            for(int i = 0; i < timerContainer.mTimerList.size();i++){
                timerContainer.mTimerList.get(i).setEnable(false);
            }
            String json_data = gson.toJson(timerContainer);
            StorageManager.writeToFile(Utill.TIMER_SELECTED,json_data,getApplicationContext());

            /* --------------------------------  */
            /* ПЕРЕЗАИСЬ ФАЙЛА ПЕСЕН */

       /*     String dataSound = StorageManager.readFromFile(Utill.SELECTED_SOUND, getApplicationContext()); //выбранные таймеры
            SoundContainer soundContainer = gson.fromJson(dataSound,SoundContainer.class);
            for(int i = 0; i < soundContainer.mSoundList.size();i++){
                try{
                    soundContainer.mSoundList.get(i).setEnabled(false);
                }catch (NullPointerException e){
                    e.printStackTrace();
                }

            }
            String json_music = gson.toJson(soundContainer);
            StorageManager.writeToFile(Utill.SELECTED_SOUND,json_music,getApplicationContext());

            /* --------------------------------  */
     /*   }catch (NullPointerException e){
            e.printStackTrace();
        }
        stopSelf(); //закрываем сервис

    }

    @Override
    public void onDestroy() {
        mTimer.cancel();
        if(state == STOPTIMER){
            Log.d("Закрыть","ТАЙМЕР ЗАКОНЧИЛСЯ");
            try{
                mPendingIntent.send(state);
            }catch (PendingIntent.CanceledException e){
                e.printStackTrace();
            }
        }else if(mManager.isTimerEnabled()){
            Log.d("Закрыть","ПЕРЕСОЗДАТЬ");
            Intent broadcastIntent = new Intent();
            broadcastIntent.putExtra("PARAM_INTENT",mPendingIntent);
            sendBroadcast(broadcastIntent);
        }else{
            Log.d("Закрыть","ТАЙМЕР ЗАКОНЧИЛСЯ");
            try{
                mPendingIntent.send(state);
            }catch (PendingIntent.CanceledException e){
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

*/


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
        mPendingIntent = intent.getParcelableExtra("PARAM_INTENT"); //получаем сссылку на интенет
        startTimer();

        state = BREAK;

        return musicServices.START_NOT_STICKY;
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
                mManager.setTimerEnabled(false);
                mTimer.cancel();
                loadSound();
                loadTimer();
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
        Log.d("+","ВЫЗОВ СТОП СЕЛФ");



        stopService(); // ВРУЧНУЮ ХУЯРИМ
    }
    private void saveTime(TimerContainer timerContainer){
        for(int i = 0; i < timerContainer.mTimerList.size();i++){
            timerContainer.mTimerList.get(i).setEnable(false);
        }
        Gson gson = new Gson();
        String json_data = gson.toJson(timerContainer);
        StorageManager.writeToFile(Utill.TIMER_SELECTED,json_data,getApplicationContext());
    }

    private void stopService(){

        Log.d("Закрыть","ЗАШЛИ");
        if(state == STOPTIMER){
            Log.d("Закрыть","закрыть");
            try{
                mPendingIntent.send(state);
            }catch (PendingIntent.CanceledException e){
                e.printStackTrace();
            }

        }

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
    if(mManager.isTimerEnabled() && state != STOPTIMER){
            Log.d("Закрыть","СЬО");

            Intent broadcastIntent = new Intent();
            broadcastIntent.putExtra("PARAM_INTENT",mPendingIntent);
            sendBroadcast(broadcastIntent);
    }else{
        Log.d("end","+");
        mTimer.cancel();
    }
    super.onDestroy();
    }

    @Override
    public void onLowMemory() {

    }


}
