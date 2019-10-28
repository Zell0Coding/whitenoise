package com.ponicamedia.android.whitenoise.Controllers;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.ponicamedia.android.whitenoise.Adapters.LanguageAdapter;
import com.ponicamedia.android.whitenoise.Models.Languages;
import com.ponicamedia.android.whitenoise.Models.currentLanguage;
import com.ponicamedia.android.whitenoise.R;
import com.ponicamedia.android.whitenoise.Utills.Manager;
import com.ponicamedia.android.whitenoise.Utills.StorageManager;
import com.ponicamedia.android.whitenoise.Utills.Utill;
import com.ponicamedia.android.whitenoise.Utills.i_helper;

import java.util.Locale;

public class LanguageFragment extends Fragment implements i_helper.i_language {

    public interface selectLanguage{
        void select();
        void restartSettings();
    }


    public selectLanguage select;

    private RecyclerView mRecyclerView;
    private LanguageAdapter mLanguageAdapter;
    private Manager mManager;

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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mLanguageAdapter = new LanguageAdapter(getContext(),mManager,this);
        mRecyclerView.setAdapter(mLanguageAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
}
