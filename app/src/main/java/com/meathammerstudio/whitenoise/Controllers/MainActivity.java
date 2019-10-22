package com.meathammerstudio.whitenoise.Controllers;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.meathammerstudio.whitenoise.Models.Sound;
import com.meathammerstudio.whitenoise.Models.SoundContainer;
import com.meathammerstudio.whitenoise.Models.SoundListiner;
import com.meathammerstudio.whitenoise.Models.Timer;
import com.meathammerstudio.whitenoise.Models.currentLanguage;
import com.meathammerstudio.whitenoise.R;
import com.meathammerstudio.whitenoise.Services.timeServices;
import com.meathammerstudio.whitenoise.Utills.Manager;
import com.meathammerstudio.whitenoise.Utills.StorageManager;
import com.meathammerstudio.whitenoise.Utills.Utill;
import com.meathammerstudio.whitenoise.Services.musicServices;
import com.meathammerstudio.whitenoise.Utills.i_helper;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LanguageFragment.selectLanguage, i_helper.i_timer_servies{

    private Manager mManager;
    private ActionBar mActionBar;
    SoundPool soundPool;

    MediaPlayer mp1;
    MediaPlayer mp2;


    private MediaPlayer mCurrentPlayer = null;
    private MediaPlayer mNextPlayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mManager = Manager.getInstance();
        mManager.getSoundListiner().setContext(getApplicationContext());


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        mManager.setWidth(width);


        Toolbar mActionBarToolbar =  findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBar = getSupportActionBar();

        mActionBar.setDisplayShowTitleEnabled(false);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        PlayMusicFragment playMusicFragment = new PlayMusicFragment();
        fragmentTransaction.replace(R.id.container,playMusicFragment);
        fragmentTransaction.commit();

        setSettings();

        Intent sound = new Intent(getApplicationContext(), musicServices.class );
        startService(sound);

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
                mActionBar.setDisplayHomeAsUpEnabled(false);
                mActionBar.setDisplayShowHomeEnabled(false);
                mActionBar.setDisplayShowTitleEnabled(false);

                break;
            case Utill.TIMER_FRAGMENT:
                TimerFragment TimerFragment = new TimerFragment();
                fragmentTransaction.replace(R.id.container,TimerFragment,"timer");
                mActionBar.setDisplayHomeAsUpEnabled(true);
                mActionBar.setDisplayShowHomeEnabled(true);
                mActionBar.setDisplayShowTitleEnabled(true);
                mActionBar.setTitle(R.string.timer);

                break;
            case Utill.SETTINGS:

                LanguageFragment languageFragment = new LanguageFragment();
                fragmentTransaction.replace(R.id.container,languageFragment);
                mActionBar.setDisplayHomeAsUpEnabled(true);
                mActionBar.setDisplayShowHomeEnabled(true);
                mActionBar.setDisplayShowTitleEnabled(true);
                mActionBar.setTitle(R.string.settings);

                break;

            case Utill.SLEEP_MODE:

                AsleepFragment asleepFragment = new AsleepFragment();
                fragmentTransaction.replace(R.id.container,asleepFragment);
                mActionBar.setDisplayHomeAsUpEnabled(true);
                mActionBar.setDisplayShowHomeEnabled(true);
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

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }


    @Override
    public void startTimer(Timer timer) {
        mManager.setCurrent_timer_hour(timer.getHours());
        mManager.setCurrent_timer_minute(timer.getMinute());
        Log.d("TIMER","таймер включен");

        Intent intent = new Intent(getApplicationContext(), timeServices.class);
        PendingIntent pendingIntent = createPendingResult(0,intent,0);
        intent.putExtra("PARAM_INTENT", pendingIntent);
        startService(intent);

    }

    @Override
    public void stopTimer() {
        try{
            stopService(new Intent(this,timeServices.class));
            Log.d("TIMER","таймер отключен");
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==100){
            //TODO:обновляем фрагмент

            Fragment current = (Fragment) getSupportFragmentManager().findFragmentById(R.id.container);
            if(current!=null){
                if(current.getTag().equals("music")){
                    Log.d("ВЫБРАННЫЙ-","МУЗЫКА");
                    addFragment(Utill.MUSIC_FRAGMENT);
                }else if(current.getTag().equals("timer")){
                    addFragment(Utill.TIMER_FRAGMENT);
                }
            }

        }
    }
}
