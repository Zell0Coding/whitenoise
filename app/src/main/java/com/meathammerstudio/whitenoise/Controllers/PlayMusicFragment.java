package com.meathammerstudio.whitenoise.Controllers;

import android.content.Context;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meathammerstudio.whitenoise.Adapters.SwipeToDeleteCallback;
import com.meathammerstudio.whitenoise.Adapters.musicAdapter;
import com.meathammerstudio.whitenoise.Models.Sound;
import com.meathammerstudio.whitenoise.R;
import com.meathammerstudio.whitenoise.Utills.Manager;
import com.meathammerstudio.whitenoise.Utills.Utill;
import com.meathammerstudio.whitenoise.Utills.i_helper;

public class PlayMusicFragment extends Fragment implements View.OnClickListener,  musicAdapter.updateButton, i_helper.i_sound  {

    private ItemTouchHelper mItemTouchHelper;

    private Manager mManager;

    private GridLayout mGridLayout;

    private musicAdapter mMusicAdapter;
    private RecyclerView mRecyclerView;

    private ImageButton whiteNoise;
    private ImageButton rain;
    private ImageButton thunder  ;
    private ImageButton ocean;
    private ImageButton wind;
    private ImageButton river;
    private ImageButton fire;
    private ImageButton forest;
    private ImageButton night;

    private ImageView whiteNoiseIndicator;
    private ImageView rainIndicator;
    private ImageView thunderIndicator;
    private ImageView oceanIndicator;
    private ImageView windIndicator;
    private ImageView riverIndicator;
    private ImageView fireIndicator;
    private ImageView forestIndicator;
    private ImageView nightIndicator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mManager = Manager.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.play_fragment,container,false);

        whiteNoiseIndicator = view.findViewById(R.id.white_noise_indicator);
        rainIndicator = view.findViewById(R.id.rain_indicator);
        thunderIndicator = view.findViewById(R.id.thunder_indicator);
        oceanIndicator = view.findViewById(R.id.ocean_indicator);
        windIndicator = view.findViewById(R.id.wind_indicator);
        riverIndicator = view.findViewById(R.id.river_indicator);
        fireIndicator = view.findViewById(R.id.fire_indicator);
        forestIndicator = view.findViewById(R.id.forest_indicator);
        nightIndicator = view.findViewById(R.id.night_indicator);
        mGridLayout = view.findViewById(R.id.grid_layout);

        whiteNoise = view.findViewById(R.id.white_noise);
        rain = view.findViewById(R.id.rain);
        thunder = view.findViewById(R.id.thunder);
        ocean = view.findViewById(R.id.ocean);
        wind = view.findViewById(R.id.wind);
        river = view.findViewById(R.id.river);
        fire = view.findViewById(R.id.fire);
        forest = view.findViewById(R.id.forest);
        night = view.findViewById(R.id.night);


        whiteNoise.setOnClickListener(this);
        rain.setOnClickListener(this);
        thunder.setOnClickListener(this);
        ocean.setOnClickListener(this);
        wind.setOnClickListener(this);
        river.setOnClickListener(this);
        fire.setOnClickListener(this);
        forest.setOnClickListener(this);
        night.setOnClickListener(this);


        mGridLayout.setMinimumHeight(mManager.getWidth()-96-28);


        mRecyclerView = view.findViewById(R.id.play_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mMusicAdapter = new musicAdapter(this,this);
        mRecyclerView.setAdapter(mMusicAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*

        /ItemTouchHelper.Callback callback = new SwipeToDeleteCallback(mMusicAdapter);
                mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);


         */

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.white_noise:
                Sound white_sound = new Sound("white_noise",Utill.WHITE_NOISE_IMG,Utill.WHITE_NOISE,0.5f,true,0);
                whiteNoiseIndicator.setImageResource(R.drawable.ic_feather_pause_circle);
                mMusicAdapter.addNewSong(white_sound);
                break;
            case R.id.rain:
                Sound rain = new Sound("rain",Utill.RAIN_IMG,Utill.RAIN,0.5f,true,0);
                rainIndicator.setImageResource(R.drawable.ic_feather_pause_circle);
                mMusicAdapter.addNewSong(rain);
                break;
            case R.id.thunder:
                Sound thunder = new Sound("thunder",Utill.THUNDER_IMG,Utill.THUNDER,0.5f,true,0);
                thunderIndicator.setImageResource(R.drawable.ic_feather_pause_circle);
                mMusicAdapter.addNewSong(thunder);
                break;
            case R.id.ocean:
                Sound ocean = new Sound("ocean",Utill.OCEAN_IMG,Utill.OCEAN,0.5f,true,0);
                oceanIndicator.setImageResource(R.drawable.ic_feather_pause_circle);
                mMusicAdapter.addNewSong(ocean);
                break;
            case R.id.wind:
                Sound wind = new Sound("wind",Utill.WIND_IMG,Utill.WIND,0.5f,true,0);
                windIndicator.setImageResource(R.drawable.ic_feather_pause_circle);
                mMusicAdapter.addNewSong(wind);
                break;
            case R.id.river:
                Sound river = new Sound("river",Utill.RIVER_IMG,Utill.RIVER,0.5f,true,0);
                riverIndicator.setImageResource(R.drawable.ic_feather_pause_circle);
                mMusicAdapter.addNewSong(river);
                break;
            case R.id.fire:
                Sound fire = new Sound("fire",Utill.FIRE_IMG,Utill.FIRE,0.5f,true,0);
                fireIndicator.setImageResource(R.drawable.ic_feather_pause_circle);
                mMusicAdapter.addNewSong(fire);
                break;
            case R.id.forest:
                Sound forest = new Sound("forest",Utill.FOREST_IMG,Utill.FOREST,0.5f,true,0);
                forestIndicator.setImageResource(R.drawable.ic_feather_pause_circle);
                mMusicAdapter.addNewSong(forest);
                break;
            case R.id.night:
                Sound night = new Sound("night",Utill.NIGHT_IMG,Utill.NIGHT,0.5f,true,0);
                nightIndicator.setImageResource(R.drawable.ic_feather_pause_circle);
                mMusicAdapter.addNewSong(night);
                break;
        }
    }
    @Override
    public void update(String name, boolean enable) {
        int indicator = (enable) ? R.drawable.ic_feather_pause_circle : R.drawable.ic_outline_play_circle_filled_white;
        switch (name){
            case "white_noise":
                whiteNoiseIndicator.setImageResource(indicator);
                break;
            case "rain":
                rainIndicator.setImageResource(indicator);
                break;
            case "thunder":
                thunderIndicator.setImageResource(indicator);
                break;
            case "ocean":
                oceanIndicator.setImageResource(indicator);
                break;
            case "wind":
                windIndicator.setImageResource(indicator);
                break;
            case "river":
                riverIndicator.setImageResource(indicator);
                break;
            case "fire":
                fireIndicator.setImageResource(indicator);
                break;
            case "forest":
                forestIndicator.setImageResource(indicator);
                break;
            case "night":
                nightIndicator.setImageResource(indicator);
                break;
        }
    }


    // ------------------------- Добавление и изменение песен --------------------------

    @Override
    public void addSound(Sound _sound) {
        mManager.getSoundListiner().addSound(_sound);
    }

    @Override
    public void playSound(Sound _sound) {
        mManager.getSoundListiner().playSound(_sound);
    }

    @Override
    public void stopSound(Sound _sound) {
        mManager.getSoundListiner().stopSound(_sound);
    }

    @Override
    public void deleteSound(Sound _sound) {
        mManager.getSoundListiner().deleteSound(_sound);
    }

    @Override
    public void changeVolume(Sound _sound) {
        mManager.getSoundListiner().changeVolume(_sound);
    }

    // ---------------------------------------------------------------------------------

}
