package com.meathammerstudio.whitenoise.Controllers;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.meathammerstudio.whitenoise.R;
import com.meathammerstudio.whitenoise.Utills.Manager;

public class SplashScreen extends AppCompatActivity {

    private Manager mManager;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        mManager = Manager.getInstance();
        progress();

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

            }
        }, 3000); //specify the number of milliseconds


    }

}
