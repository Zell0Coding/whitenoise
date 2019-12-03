package com.ponicamedia.android.whitenoise.Controllers;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ponicamedia.android.whitenoise.Models.CasheSounds;
import com.ponicamedia.android.whitenoise.Models.SoundContainer;
import com.ponicamedia.android.whitenoise.Models.SoundListiner;
import com.ponicamedia.android.whitenoise.Models.Timer;
import com.ponicamedia.android.whitenoise.Models.TimerContainer;
import com.ponicamedia.android.whitenoise.R;
import com.ponicamedia.android.whitenoise.Services.timeServices;
import com.ponicamedia.android.whitenoise.Utills.Manager;
import com.ponicamedia.android.whitenoise.Utills.PersistantStorage;
import com.ponicamedia.android.whitenoise.Utills.StorageManager;
import com.ponicamedia.android.whitenoise.Utills.Utill;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class SplashScreen extends AppCompatActivity {

    private Manager mManager;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Manager.deleteManager();
        stopService(new Intent(getApplicationContext(), timeServices.class));

        mManager = Manager.getInstance();
        PersistantStorage.init(getApplicationContext());

        loadCasheMusic();
        progress();


        BillingClient billingClient = BillingClient.newBuilder(getApplicationContext())
                .setListener(new PurchasesUpdatedListener() {
                    @Override
                    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
                        if (responseCode == BillingClient.BillingResponse.OK && purchases != null) {
                            //сюда мы попадем когда будет осуществлена покупка
                            mManager.setPremium(true);
                        }
                    }
                })
                .build();

        Purchase.PurchasesResult subscriptionResult =
                billingClient.queryPurchases(BillingClient.SkuType.SUBS);

        if(subscriptionResult.getResponseCode() == BillingClient.BillingResponse.OK && subscriptionResult.getPurchasesList().size() > 0){
            mManager.setPremium(true);
            Log.d("has","has");
        }else if(subscriptionResult.getResponseCode() == BillingClient.BillingResponse.SERVICE_UNAVAILABLE
                || subscriptionResult.getResponseCode() == BillingClient.BillingResponse.SERVICE_DISCONNECTED){
            mManager.setPremium(PersistantStorage.getProperty("premium"));
            Log.d("?","internet-");
        }else{
            mManager.setPremium(false);
            Log.d("not has","-");
        }

    }

    private void firstUP(){
        PersistantStorage.init(getApplicationContext());
        boolean init = PersistantStorage.getProperty("firstUP");

        if(!init){
            initTimers();
            PersistantStorage.addProperty("firstUP",true);
        }
    }

    private void initTimers(){
        TimerContainer timerContainer = new TimerContainer();
        timerContainer.addTimer(new Timer(1,0,false));

        Gson gsonNew = new Gson();
        String json_data = gsonNew.toJson(timerContainer);
        StorageManager.writeToFile(Utill.TIMER_SELECTED,json_data,getApplicationContext());

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


        saveTimers();
        firstUP();


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

    private void loadCasheMusic(){
        String res = "";
        try{
            InputStream is = getAssets().open("songs.json");
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String tempStringJson = "";
            StringBuilder stringBilder = new StringBuilder();
            while ((tempStringJson = bufferedReader.readLine())!=null){
                stringBilder.append(tempStringJson);
            }

            is.close();
            res = stringBilder.toString();
            Log.d("size",res.length()+"");


            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            CasheSounds casheSounds = gson.fromJson(res,CasheSounds.class);
            mManager.setCasheSounds(casheSounds);

        }catch (IOException e){

        }

    }

}
