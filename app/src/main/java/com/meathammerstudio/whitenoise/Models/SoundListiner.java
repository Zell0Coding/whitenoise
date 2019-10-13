package com.meathammerstudio.whitenoise.Models;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class SoundListiner  {

    private Map<Sound, MediaPlayer> soundListiner;
    private Context mContext;

    public SoundListiner(){
        soundListiner = new HashMap<Sound,MediaPlayer>();
    }

    public void setContext(Context context){mContext = context;}
    public void addSound( Sound _sound){

        if(_sound!=null){
            MediaPlayer sound = MediaPlayer.create(mContext, _sound.getPath_sound());
            sound.setLooping(true); // Set looping
            sound.setVolume(_sound.getVolume(),_sound.getVolume());
            soundListiner.put(_sound,sound);
            Log.d("TAG","add");
            if(_sound.isEnabled()) sound.start();
        }
    }
    public void deleteSound(Sound _sound){
        for (Map.Entry<Sound,MediaPlayer> map:soundListiner.entrySet())
        {
            if(map.getKey().getName().equals(_sound.getName())){
                soundListiner.get(_sound).stop();
                Log.d("TAG","stop");
                soundListiner.remove(_sound);
                return;
            }
        }
    }
    public void playSound(Sound _sound){
        for (Map.Entry<Sound,MediaPlayer> map:soundListiner.entrySet())
        {
            if(map.getKey().getName().equals(_sound.getName())){
                map.getValue().seekTo(map.getKey().getSound_sec());
                map.getValue().start();
                Log.d("Play on ",map.getKey().getSound_sec()+"");
                map.getKey().setEnabled(true);
                return;
            }
        }
    }
    public void stopSound(Sound _sound){
        for (Map.Entry<Sound,MediaPlayer> map:soundListiner.entrySet())
        {
            if(map.getKey().getName().equals(_sound.getName())){
                map.getKey().setSound_sec(map.getValue().getCurrentPosition());
                map.getValue().stop();
                map.getValue().prepareAsync();
                map.getKey().setEnabled(false);
                return;
            }
        }
    }
    public void stopAllSound(){
        for (Map.Entry<Sound,MediaPlayer> map: soundListiner.entrySet()) {
            map.getKey().setSound_sec(map.getValue().getCurrentPosition());
            map.getValue().stop();
            map.getValue().prepareAsync();
            map.getKey().setEnabled(false);
        }
    }
    public void playAllSound(){
        for (Map.Entry<Sound,MediaPlayer> map: soundListiner.entrySet()) {
            map.getValue().seekTo(map.getKey().getSound_sec());
            map.getValue().start();
        }
    }
    public void changeVolume(Sound _sound){
        for (Map.Entry<Sound,MediaPlayer> map:soundListiner.entrySet())
        {
            if(map.getKey().getName().equals(_sound.getName())){
                map.getValue().setVolume(_sound.getVolume(),_sound.getVolume());
                map.getKey().setVolume(_sound.getVolume());
                Log.d("Volume",_sound.getVolume()+"");
                return;
            }
        }
    }
}
