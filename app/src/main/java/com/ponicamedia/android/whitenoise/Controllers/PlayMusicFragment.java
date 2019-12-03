package com.ponicamedia.android.whitenoise.Controllers;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ponicamedia.android.whitenoise.Adapters.PagesAdapter;
import com.ponicamedia.android.whitenoise.Adapters.musicAdapter;
import com.ponicamedia.android.whitenoise.Models.CasheSounds;
import com.ponicamedia.android.whitenoise.Models.Sound;
import com.ponicamedia.android.whitenoise.Models.SoundContainer;
import com.ponicamedia.android.whitenoise.R;
import com.ponicamedia.android.whitenoise.Utills.Manager;
import com.ponicamedia.android.whitenoise.Utills.StorageManager;
import com.ponicamedia.android.whitenoise.Utills.Utill;
import com.ponicamedia.android.whitenoise.Utills.i_helper;

import java.util.ArrayList;
import java.util.List;

public class PlayMusicFragment extends Fragment implements View.OnClickListener,  musicAdapter.updateButton, i_helper.i_sound,  PagesAdapter.getSound {

    public interface openBuyFragment{
        void openBuy();
    }

    private openBuyFragment main;
    private Manager mManager;
    private musicAdapter mMusicAdapter;
    private RecyclerView mRecyclerView;
    private ViewPager pager;

    private ImageButton allPlay;
    private PagesAdapter pagesAdapter;

    private RelativeLayout buttonWrapper;
    private List<Sound> sounds;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        main = (openBuyFragment) context;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mManager = Manager.getInstance();
        sounds = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.play_fragment,container,false);

        pager = view.findViewById(R.id.pager);


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

//setting margins around imageimageview
        params.height=mManager.getWidth()-96-28; //left, top, right, bottom


        pager.setLayoutParams(params);

        allPlay = view.findViewById(R.id.play_all);
        buttonWrapper = view.findViewById(R.id.button_wrapper);

        allPlay.setOnClickListener(this);

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
        InitialSounds();
    }

    private void InitialSounds(){
        for (int i = 0; i < mManager.getCasheSounds().songs.size(); i++){

            CasheSounds.SoundItem vol = mManager.getCasheSounds().songs.get(i);

            String button_path = vol.image;
            String sound_path = vol.music;
            String sound_duplicate_path = vol.music_duplicate;


            int id = getResources().getIdentifier(button_path, "drawable", getContext().getPackageName());
            int sound = getResources().getIdentifier(sound_path,"raw",getContext().getPackageName());
            int sound_dupl = getResources().getIdentifier(sound_duplicate_path,"raw",getContext().getPackageName());

            Log.d("name",""+sound);


            Sound item = new Sound(vol.name,id,sound,sound_dupl,0.5f,false,0,vol.premium);
            sounds.add(item);
        }

        pagesAdapter = new PagesAdapter(getContext(),this);
        pager.setAdapter(pagesAdapter);
        pager.setCurrentItem(1);

        reCreateSounds(); // пересоздаем звуки с предыдущей сессии*/

    }


    private void reCreateSounds(){
        SoundContainer soundContainer = loadSound();
        if(soundContainer!=null){
            if(soundContainer.mSoundList.size()>1){
                for(int i = 1; i < soundContainer.mSoundList.size();i++){
                    mMusicAdapter.addNewSong(soundContainer.mSoundList.get(i),true);
                    update(soundContainer.mSoundList.get(i).getName(),soundContainer.mSoundList.get(i).isEnabled());
                    if(soundContainer.mSoundList.get(i).isEnabled()) allPlay.setImageResource(R.drawable.all_pause);
                }
            }else{
                buttonWrapper.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_all:

                List<Sound> sounds = mMusicAdapter.getItems();
                if (sounds != null) {
                    for (int i = 1; i < sounds.size(); i++) {

                        if (sounds.get(i) != null) {
                            if (sounds.get(i).isEnabled()) {
                                mManager.getSoundListiner().stopAllSound();
                                mMusicAdapter.allStop();
                                allPlay.setImageResource(R.drawable.all_play);
                                saveSound();
                                return;
                            }
                        }
                    }
                    mManager.getSoundListiner().playAllSound();
                    mMusicAdapter.allPlay();
                    allPlay.setImageResource(R.drawable.all_pause);
                    saveSound();
                }

                break;


        }
    }
    @Override
    public void update(String name, boolean enable) {
        Log.d("+","update:" + name);
        for (int i = 0; i < sounds.size();i++){
            if(sounds.get(i).getName().equals(name)){
                sounds.get(i).setEnabled(enable);
                break;
            }
        }
        pagesAdapter.updateSoundState(name,enable);
    }


    private void updateStateAllButtonIcon(){

        List<Sound> sounds = mMusicAdapter.getItems();
        if(sounds.size() > 1){
            buttonWrapper.setVisibility(View.VISIBLE);
            for(int i = 1; i < sounds.size();i++){
                if(sounds.get(i)!=null){
                    if(sounds.get(i).isEnabled()){
                        allPlay.setImageResource(R.drawable.all_pause);
                        return;
                    }
                }
            }
            allPlay.setImageResource(R.drawable.all_play);
        }else{
            buttonWrapper.setVisibility(View.GONE);
        }

    }

    // ------------------------- Добавление и изменение песен --------------------------

    @Override
    public void addSound(Sound _sound, boolean re_create) {

        mManager.getSoundListiner().addSound(_sound);
        if(re_create) return;
        saveSound();

    }

    @Override
    public void playSound(Sound _sound) {
        mManager.getSoundListiner().playSound(_sound);
        updateStateAllButtonIcon();
        saveSound();
    }

    @Override
    public void stopSound(Sound _sound) {
        mManager.getSoundListiner().stopSound(_sound);
        updateStateAllButtonIcon();
        saveSound();
    }

    @Override
    public void deleteSound(Sound _sound) {
        mManager.getSoundListiner().deleteSound(_sound);
        updateStateAllButtonIcon();
        saveSound();
    }

    @Override
    public void changeVolume(Sound _sound) {
        mManager.getSoundListiner().changeVolume(_sound);
        saveSound();
    }

    // ---------------------------------------------------------------------------------


    // ----------------- SAVE SELECTED SOUND -----------------

    private void saveSound(){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Sound> sounds = mMusicAdapter.getItems();
                SoundContainer soundContainer = new SoundContainer();

                for(int i = 0; i < sounds.size();i++){
                    try {
                        Log.d("save-count",sounds.get(i).getName());
                    }catch (NullPointerException e){}
                    soundContainer.addSound(sounds.get(i));
                }

                Gson gson = new Gson();
                String json_data = gson.toJson(soundContainer);
                StorageManager.writeToFile(Utill.SELECTED_SOUND,json_data,getContext());
            }
        });
        thread.run();
    }

    // -------------------------------------------------------

    private SoundContainer loadSound(){
        try {
            String data = StorageManager.readFromFile(Utill.SELECTED_SOUND, getContext());
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            SoundContainer soundContainer = gson.fromJson(data,SoundContainer.class);
            return soundContainer;
        }catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }
    }

    // -------------------------------------------------------


    @Override
    public List<Sound> getSounds() {
        return sounds;
    }

    @Override
    public boolean hasPremium() {
        return mManager.isPremium();
    }

    @Override
    public void clickOnSound(Sound sound){

        if(sound.isPremium() && mManager.isPremium()){
            mMusicAdapter.addNewSong(sound,false);
            updateStateAllButtonIcon();
        }else if(!sound.isPremium()){
            mMusicAdapter.addNewSong(sound,false);
            updateStateAllButtonIcon();
        }else{
            main.openBuy();
        }
    }


}
