package com.ponicamedia.android.whitenoise.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ponicamedia.android.whitenoise.Models.SoundContainer;
import com.ponicamedia.android.whitenoise.Models.SoundListiner;
import com.ponicamedia.android.whitenoise.Models.TimerContainer;
import com.ponicamedia.android.whitenoise.R;
import com.ponicamedia.android.whitenoise.Utills.Manager;
import com.ponicamedia.android.whitenoise.Utills.StorageManager;
import com.ponicamedia.android.whitenoise.Utills.Utill;

public class SplashScreen extends AppCompatActivity {

    private Manager mManager;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        mManager = Manager.getInstance();
        progress();
        saveTimers();

    }

    private void progress(){

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-5530031616350650/6913775402");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mManager.setMInterstitialAd(mInterstitialAd);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();

            }
        }, 3000); //specify the number of milliseconds


    }

    private void saveTimers(){

        try {
            String data = StorageManager.readFromFile(Utill.TIMER_SELECTED, getApplicationContext());
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            TimerContainer timerContainer = gson.fromJson(data,TimerContainer.class);

            for (int i = 0; i < timerContainer.mTimerList.size();i++){
                timerContainer.mTimerList.get(i).setEnable(false);
            }

            Gson gsonNew = new Gson();
            String json_data = gsonNew.toJson(timerContainer);
            StorageManager.writeToFile(Utill.TIMER_SELECTED,json_data,getApplicationContext());

        }catch (NullPointerException e){
            e.printStackTrace();
        }



    }


}
