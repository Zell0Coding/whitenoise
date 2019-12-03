package com.ponicamedia.android.whitenoise.Controllers;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ponicamedia.android.whitenoise.Models.Timer;
import com.ponicamedia.android.whitenoise.Models.currentLanguage;
import com.ponicamedia.android.whitenoise.R;
import com.ponicamedia.android.whitenoise.Services.timeServices;
import com.ponicamedia.android.whitenoise.Utills.Manager;
import com.ponicamedia.android.whitenoise.Utills.PerfectLoopMediaPlayer;
import com.ponicamedia.android.whitenoise.Utills.PersistantStorage;
import com.ponicamedia.android.whitenoise.Utills.StorageManager;
import com.ponicamedia.android.whitenoise.Utills.Utill;
import com.ponicamedia.android.whitenoise.Services.musicServices;
import com.ponicamedia.android.whitenoise.Utills.i_helper;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LanguageFragment.selectLanguage, i_helper.i_timer_servies, PlayMusicFragment.openBuyFragment
{





    private Manager mManager;
    private ActionBar mActionBar;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mManager = Manager.getInstance();
        mManager.createListiner(getApplicationContext());
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        mManager.setWidth(width);
        Toolbar mActionBarToolbar =  findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowTitleEnabled(false);
        setSettings();
        Intent sound = new Intent(getApplicationContext(), musicServices.class );

        startService(sound);

        initialFirebase();

        checkHoursGone();

        addFragment();

    }



    @Override
    protected void onRestart() {
        super.onRestart();
        checkHoursGone();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.timer:
                addFragment(Utill.TIMER_FRAGMENT);
                break;
            case R.id.sleep:
                addFragment(Utill.SLEEP_MODE);
                break;
            case R.id.settings:
                addFragment(Utill.SETTINGS);
                break;
            case android.R.id.home:
                addFragment(Utill.MUSIC_FRAGMENT);
                break;
        }

        return true;
    }

    private void addFragment(String fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        switch (fragment){
            case Utill.MUSIC_FRAGMENT:
                PlayMusicFragment playMusicFragment = new PlayMusicFragment();
                fragmentTransaction.replace(R.id.container,playMusicFragment,"music");
                getSupportFragmentManager().beginTransaction().addToBackStack(null);
                mActionBar.setDisplayHomeAsUpEnabled(false);
                mActionBar.setDisplayShowHomeEnabled(false);
                mActionBar.setDisplayShowTitleEnabled(false);

                break;
            case Utill.TIMER_FRAGMENT:
                TimerFragment TimerFragment = new TimerFragment();
                fragmentTransaction.replace(R.id.container,TimerFragment,"timer");
                mActionBar.setDisplayHomeAsUpEnabled(true);
                fragmentTransaction.addToBackStack(null);
                mActionBar.setDisplayShowHomeEnabled(true);
                mActionBar.setDisplayShowTitleEnabled(true);
                mActionBar.setTitle(R.string.timer);

                break;
            case Utill.SETTINGS:

                LanguageFragment languageFragment = new LanguageFragment();
                fragmentTransaction.replace(R.id.container,languageFragment,"settings");
                mActionBar.setDisplayHomeAsUpEnabled(true);
                fragmentTransaction.addToBackStack(null);
                mActionBar.setDisplayShowHomeEnabled(true);
                mActionBar.setDisplayShowTitleEnabled(true);
                mActionBar.setTitle(R.string.settings);

                break;

            case Utill.SLEEP_MODE:

                AsleepFragment asleepFragment = new AsleepFragment();
                fragmentTransaction.replace(R.id.container,asleepFragment,"sleep");
                mActionBar.setDisplayHomeAsUpEnabled(true);
                mActionBar.setDisplayShowHomeEnabled(true);
                fragmentTransaction.addToBackStack(null);
                mActionBar.setDisplayShowTitleEnabled(true);
                mActionBar.setTitle(R.string.sleeping_mode);

                break;
        }
        fragmentTransaction.commit();
    }

    private void setSettings(){

        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();

        currentLanguage language = getLanguage();


        if(language==null){
            language = new currentLanguage();
            language.setLanguage_abbr(configuration.locale.getLanguage().toLowerCase());
            Gson gson = new Gson();
            String json_data = gson.toJson(language);
            StorageManager.writeToFile(Utill.SETTINGS,json_data,getApplicationContext());

        }else{

            try {
                configuration.setLocale(new Locale(language.getLanguage_abbr().toLowerCase()));
            }catch (RuntimeException e){
                configuration.locale = new Locale(language.getLanguage_abbr().toLowerCase());
            }
            resources.updateConfiguration(configuration,displayMetrics);

        }
        mManager.setCurrentLang(language.getLanguage_abbr().toLowerCase());
    }

    private currentLanguage getLanguage(){
        try {
            String data = StorageManager.readFromFile(Utill.LANGUAGE, getApplicationContext());
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            currentLanguage language = gson.fromJson(data,currentLanguage.class);
            return language;
        }catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void select() {
        mActionBar.setTitle(R.string.timer);
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();
        Fragment current = getSupportFragmentManager().findFragmentById(R.id.container); // пытаемся получить выбранный фрагмент

        if (count == 0 || current.getTag().equals(Utill.MUSIC_FRAGMENT)) {

            super.onBackPressed();
            finish();
            System.exit(0);
        }
        else {
            getSupportFragmentManager().popBackStack();
        }

    }

    private void checkHoursGone(){

            PersistantStorage.init(getApplicationContext());
            String date = PersistantStorage.getPropertyString("LASTADS");

            if(date==null){

                Date date1 = new Date();
                date =  String.valueOf(date1.getTime());
                PersistantStorage.addProperty("LASTADS",date);
                Log.d("DATE",date1.getTime()+"");
                startAd(date);

            }else{
                try{

                    long firstDate = Long.parseLong(date);
                    Date date2 = new Date();

                    long between = date2.getTime() - firstDate;
                    Log.d("ПРОШЛО",between + "");
                    if( between > 18000000 ){
                        date = String.valueOf(date2.getTime());
                        startAd(date);
                    }

                }catch (NullPointerException e){
                    e.printStackTrace();
                }

            }

    }

    private void startAd(final String date){

        if(!mManager.isPremium()){
            final InterstitialAd mInterstitialAd = mManager.getMInterstitialAd();
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
                PersistantStorage.addProperty("LASTADS",date);
            } else {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {

                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                            PersistantStorage.addProperty("LASTADS",date);
                        }else{
                            Log.d("TAG","НЕГОТОВО");
                        }

                    }
                }, 5000); //specify the number of milliseconds

            }
        }
    }

    private void initialFirebase(){
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Log.d("#aa",mFirebaseAnalytics.getFirebaseInstanceId());
    }

    private void addFragment(){

        PlayMusicFragment playMusicFragment = new PlayMusicFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,playMusicFragment,"music");
        fragmentTransaction.commit();
        mActionBar.setDisplayHomeAsUpEnabled(false);
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);

    }

    @Override
    public void startTimer(Timer timer) {

        mManager.setCurrent_timer_hour(timer.getHours()); //УСТАНАВЛИВАЕМ СКОЛЬКО НУЖНО ЧАСОВ
        mManager.setCurrent_timer_minute(timer.getMinute());  //УСТАНАВЛИВАЕМ СКОЛЬКО НУЖНО МИНУТ
        mManager.setTimerEnabled(true);
        Log.d("TIMER","таймер включен");

        Intent intent = new Intent(getApplicationContext(), timeServices.class);
        PendingIntent pendingIntent = createPendingResult(0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        intent.putExtra("PARAM_INTENT", pendingIntent);
        startService(intent);

    }

    @Override
    public void stopTimer() {
        try{
            mManager.setTimerEnabled(false);
            stopService(new Intent(getApplicationContext(),timeServices.class));
            Log.d("TIMER","таймер отключен");
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }



    @Override
    public void restartSettings() {
        addFragment(Utill.SETTINGS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment current = getSupportFragmentManager().findFragmentById(R.id.container); // пытаемся получить выбранный фрагмент

        /* ТАЙМЕР ОТРАБОТАН */
        if(resultCode == 100 && current != null){
            if(current.getTag().equals("music")){
                getSupportFragmentManager().beginTransaction().remove(current).commit();
                addFragment(Utill.MUSIC_FRAGMENT);
            }else if(current.getTag().equals("timer")){
                getSupportFragmentManager().beginTransaction().remove(current).commit();
                addFragment(Utill.TIMER_FRAGMENT);
            }

            stopTimer();

            /* ТАЙМЕР ОБНОВИТЬ */
        }else if(resultCode == 200 &&current != null){
            if(current.getTag().equals("timer")){
                ((TimerFragment)current).updateTimer(data.getStringExtra("hours"),data.getStringExtra("minute"),data.getIntExtra("dots",0));
            }
        }


        /*if(resultCode==100){
            //TODO:обновляем фрагмент
            if(current!=null){
                try{
                    if(current.getTag().equals("music")){
                        Log.d("ВЫБРАННЫЙ-","МУЗЫКА");
                        addFragment(Utill.MUSIC_FRAGMENT);


                    }else if(current.getTag().equals("timer")){
                        addFragment(Utill.TIMER_FRAGMENT);
                    }

                    Toast.makeText(getApplicationContext(),"Timer stopped!!!",Toast.LENGTH_SHORT).show();

                }catch (NullPointerException e){
                    Toast.makeText(getApplicationContext(),"ERROR-2",Toast.LENGTH_SHORT).show();
                }
            }
            Toast.makeText(getApplicationContext(),"CODE-100",Toast.LENGTH_SHORT).show();
        }
        else if(resultCode == 200){
            if(current!=null){
                try{
                    if(current.getTag().equals("timer")){
                        ((TimerFragment)current).updateTimer(data.getStringExtra("hours"),data.getStringExtra("minute"),data.getIntExtra("dots",0));
                    }
                }catch (NullPointerException e){
                    Toast.makeText(getApplicationContext(),"ERROR-3",Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            Toast.makeText(getApplicationContext(),"CODE-"+resultCode,Toast.LENGTH_SHORT).show();
        }*/
    }

        @Override
        public void openBuy() {

            Bundle bundle = new Bundle();
            bundle.putInt("fragment",2);

            LanguageFragment languageFragment = new LanguageFragment();
            languageFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.container,languageFragment,"settings").commit();
            getSupportFragmentManager().beginTransaction().addToBackStack(null);

            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setDisplayShowHomeEnabled(true);
            mActionBar.setDisplayShowTitleEnabled(true);
            mActionBar.setTitle(R.string.settings);


        }

}

