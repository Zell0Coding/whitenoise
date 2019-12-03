package com.ponicamedia.android.whitenoise.Controllers;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.ponicamedia.android.whitenoise.Adapters.LanguageAdapter;
import com.ponicamedia.android.whitenoise.Models.Languages;
import com.ponicamedia.android.whitenoise.Models.currentLanguage;
import com.ponicamedia.android.whitenoise.R;
import com.ponicamedia.android.whitenoise.Utills.Manager;
import com.ponicamedia.android.whitenoise.Utills.PersistantStorage;
import com.ponicamedia.android.whitenoise.Utills.StorageManager;
import com.ponicamedia.android.whitenoise.Utills.Utill;
import com.ponicamedia.android.whitenoise.Utills.i_helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LanguageFragment extends Fragment implements i_helper.i_language,PurchasesUpdatedListener {

    public static final int BUY_FRAGMENT = 2;

    private int current_fragment;

    public interface selectLanguage{
        void select();
        void restartSettings();
    }

    private Map<String, SkuDetails> mSkuDetailsMap = new HashMap<>();


    public selectLanguage select;

    private RecyclerView mRecyclerView;
    private LanguageAdapter mLanguageAdapter;
    private Manager mManager;

    private TabLayout tabs;
    private ConstraintLayout language;
    private ConstraintLayout podpiska;
    private Button buy;

    private BillingClient billingClient;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            select = (selectLanguage)context;
        }catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement selectLanguage");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mManager = Manager.getInstance();

        if(getArguments()!=null){
            current_fragment = getArguments().getInt("fragment");
        }

        try {
            getActivity().getActionBar().setTitle(R.string.settings);
        }catch (NullPointerException e){

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment,container,false);

        mRecyclerView = view.findViewById(R.id.recycler_language);
        tabs = view.findViewById(R.id.select_tab);
        language = view.findViewById(R.id.language_item);
        buy = view.findViewById(R.id.buy_button);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnConnectToBuy();
            }
        });
        podpiska = view.findViewById(R.id.podpiska_item);

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    language.setVisibility(View.VISIBLE);
                    podpiska.setVisibility(View.GONE);
                }else{
                    language.setVisibility(View.GONE);
                    podpiska.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mLanguageAdapter = new LanguageAdapter(getContext(),mManager,this);
        mRecyclerView.setAdapter(mLanguageAdapter);

        InAppInitial();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(current_fragment == BUY_FRAGMENT){
            language.setVisibility(View.GONE);
            podpiska.setVisibility(View.VISIBLE);
            tabs.getTabAt(1).select();
        }

    }


    @Override
    public void selectLanguage(Languages.language language) {

        Log.d("language",language.getAbbr());

        currentLanguage language_ = new currentLanguage();
        language_.setLanguage_abbr(language.getAbbr().toLowerCase());
        Gson gson = new Gson();
        String json_data = gson.toJson(language_);
        StorageManager.writeToFile(Utill.LANGUAGE,json_data,getContext());
        mManager.setCurrentLang(language_.getLanguage_abbr());

        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();

        try {
            configuration.setLocale(new Locale(language.getAbbr().toLowerCase()));
        }catch (RuntimeException e){
            configuration.locale = new Locale(language.getAbbr().toLowerCase());
        }
        resources.updateConfiguration(configuration,displayMetrics);
        select.select();
        select.restartSettings();
    }


    // ИНИЦИАЛИЗАЦИЯ
    private void InAppInitial(){
        billingClient = BillingClient.newBuilder(getContext())
                .setListener(this)
                .build();

        connectionToAPIBilling();

    }




    //ПОЛУЧЕНИЕ ВСЕХ ПОЗИЦИЙ
    private void connectionToAPIBilling(){


        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(int responseCode) {
                if(responseCode == BillingClient.BillingResponse.OK){

                    List<Purchase> purchasesList = queryPurchases(); //запрос о покупках

                    for (int i = 0; i < purchasesList.size(); i++) {
                        String purchaseId = purchasesList.get(i).getSku();
                        if(TextUtils.equals("whitenoisepodpiska", purchaseId)) {
                            takePremium();
                        }
                    }
                }
            }
            @Override
            public void onBillingServiceDisconnected() {

            }
        });

    }

    // ПОКУПКА
    private void OnConnectToBuy(){

        //TODO: DELETE
        mManager.setPremium(true);
        PersistantStorage.init(getContext());
        PersistantStorage.addProperty("premium",true);

            BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                    .setSku("whitenoisepodpiska")
                    .setType(BillingClient.SkuType.SUBS)
                    .build();
            billingClient.launchBillingFlow(getActivity(), billingFlowParams);
    }

    //ОБНОВЛЕНИЕ ДАННЫХ О ПОКУПКАХ
    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
        if (responseCode == BillingClient.BillingResponse.OK && purchases != null) {
            //сюда мы попадем когда будет осуществлена покупка
            takePremium();
        }
    }


    // ЗАПРОС О ПОКУПКАХ
    private List<Purchase> queryPurchases() {
        Purchase.PurchasesResult purchasesResult = billingClient.queryPurchases(BillingClient.SkuType.SUBS);
        return purchasesResult.getPurchasesList();
    }


    // ДАТЬ ПРЕМИУМ
    private void takePremium(){
        Toast.makeText(getContext(),"HAS PREMIUM",Toast.LENGTH_LONG).show();
    }

}
