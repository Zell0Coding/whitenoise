package com.ponicamedia.android.whitenoise.Models;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class SoundListiner  {

    private Map<Sound, MediaPlayer> soundListiner;
    private Map<Sound, MediaPlayer> soundListinerDiplacite;
    private Context mContext;

    public SoundListiner(){
        soundListiner = new HashMap<>();
        soundListinerDiplacite = new HashMap<>();
    }

    public void setContext(Context context){mContext = context;}
    public void addSound( final Sound _sound){

        if(_sound!=null){
            for (Map.Entry<Sound,MediaPlayer> entry: soundListiner.entrySet())
            {
                if(entry.getKey().getName().equals(_sound.getName())){
                    return;
                }
            }
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    MediaPlayer sound = MediaPlayer.create(mContext, _sound.getPath_sound());
                    MediaPlayer sound_duplicate = MediaPlayer.create(mContext, _sound.getPath_sound_duplicate());

                    sound.setLooping(true); // Set looping
                    sound_duplicate.setLooping(true);

                    sound.seekTo(_sound.getSound_sec()+1000);
                    sound_duplicate.seekTo(_sound.getSound_sec());

                    sound.setVolume(_sound.getVolume(),_sound.getVolume());
                    sound_duplicate.setVolume(_sound.getVolume(),_sound.getVolume());

                    soundListiner.put(_sound,sound);
                    soundListinerDiplacite.put(_sound,sound_duplicate);
                    Log.d("SIZE",soundListiner.size()+"");

                    if(_sound.isEnabled()) {
                        sound.start();
                        sound_duplicate.start();
                    }
                }
            });
            t.start();
        }
    }
    public void deleteSound(Sound _sound){
        for (Map.Entry<Sound,MediaPlayer> map:soundListiner.entrySet())
        {
            if(map.getKey().getName().equals(_sound.getName())){
                map.getValue().stop();
                soundListinerDiplacite.get(map.getKey()).stop();
                Log.d("TAG","stop");
                soundListiner.remove(_sound);
                soundListinerDiplacite.remove(_sound);
                return;
            }
        }
    }
    public void playSound(Sound _sound){
        for (Map.Entry<Sound,MediaPlayer> map:soundListiner.entrySet())
        {
            if(map.getKey().getName().equals(_sound.getName())){

                map.getValue().seekTo(map.getKey().getSound_sec()+1000);
                soundListinerDiplacite.get(map.getKey()).seekTo(_sound.getSound_sec());

                map.getValue().start();
                soundListinerDiplacite.get(map.getKey()).start();

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
                soundListinerDiplacite.get(map.getKey()).stop();
                map.getValue().prepareAsync();
                soundListinerDiplacite.get(map.getKey()).prepareAsync();
                map.getKey().setEnabled(false);
                return;
            }
        }
    }
    public void stopAllSound(){
        for (Map.Entry<Sound,MediaPlayer> map: soundListiner.entrySet()) {
            map.getKey().setSound_sec(map.getValue().getCurrentPosition());
            map.getValue().stop();
            soundListinerDiplacite.get(map.getKey()).stop();
            map.getValue().prepareAsync();
            soundListinerDiplacite.get(map.getKey()).prepareAsync();
            map.getKey().setEnabled(false);
        }
    }
    public void playAllSound(){
        for (Map.Entry<Sound,MediaPlayer> map: soundListiner.entrySet()) {
            map.getValue().seekTo(map.getKey().getSound_sec()+1000);
            soundListinerDiplacite.get(map.getKey()).seekTo(map.getKey().getSound_sec());
            map.getValue().start();
            soundListinerDiplacite.get(map.getKey()).start();
        }
    }
    public void changeVolume(Sound _sound){
        for (Map.Entry<Sound,MediaPlayer> map:soundListiner.entrySet())
        {
            if(map.getKey().getName().equals(_sound.getName())){
                map.getValue().setVolume(_sound.getVolume(),_sound.getVolume());
                soundListinerDiplacite.get(map.getKey()).setVolume(_sound.getVolume(),_sound.getVolume());
                map.getKey().setVolume(_sound.getVolume());
                Log.d("Volume",_sound.getVolume()+"");
                return;
            }
        }
    }
}
