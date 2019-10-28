package com.ponicamedia.android.whitenoise.Models;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class SoundListiner  {

    private Map<Sound, MediaPlayer> soundListiner;
    private Map<Sound, MediaPlayer> soundListinerDiplacite;
    private Context mContext;


    /* ---------------------- new variant ------------------ */


    public static SoundListiner create(Context context){
        return new SoundListiner(context);
    }


    private SoundListiner(Context context){
        this.mContext = context;
        soundListiner = new HashMap<>();
        soundListinerDiplacite = new HashMap<>();
    }

    public void addSound(final Sound _sound){

        for (Map.Entry<Sound,MediaPlayer> entry: soundListiner.entrySet())
        {
            if(entry.getKey().getName().equals(_sound.getName())){
                Log.d("Уже добавлен","+");
                return;
            }
        }

       Thread thread = new Thread((new Runnable() {
           @Override
           public void run() {
               MediaPlayer main = createSound(_sound); // создаем основной трек
               MediaPlayer duplicate = createSoundDuplicate(_sound); //создаем дубликат

               Sound mainSound = new Sound(_sound);
               Sound duplicateSound = new Sound(_sound);

               mainSound.setSound_sec(1000);
               duplicateSound.setSound_sec(0);

               soundListiner.put(mainSound,main);
               soundListinerDiplacite.put(duplicateSound,duplicate);
           }
       }));
        thread.start();

    }
    public void deleteSound(final Sound _sound){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                deleteMainSound(_sound);
                deleteSoundDuplicate(_sound);
            }
        });
        thread.start();
    }
    public void playSound(final Sound _sound){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                playMainSound(_sound);
                playDuplicateSound(_sound);
            }
        });
        thread.start();
    }
    public void stopSound(final Sound _sound){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                stopMainSound(_sound);
                stopDuplicateSound(_sound);
            }
        });
        thread.start();
    }
    public void stopAllSoundTimer(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                stopAllMainSoundTimer();
                stopAllDuplicateSoundTimer();
            }
        });
        thread.start();
    }
    public void stopAllSound(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                stopAllMainSound();
                stopAllDuplicateSound();
            }
        });
        thread.start();
    }
    public void playAllSound(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                playAllMainSound();
                playAllDuplicateSound();
            }
        });
        thread.start();
    }
    public void changeVolume(final Sound _sound){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                changeVolumeMain(_sound);
                changeVolumeDuplicate(_sound);
            }
        });
        thread.start();

}

    /*
    *
    * СОЗДАНИЕ НОВЫХ ТРЕКОВ
    *
    */

    private MediaPlayer createSound(final Sound _sound){
        AssetFileDescriptor afd = mContext.getResources().openRawResourceFd(_sound.getPath_sound());
        final MediaPlayer mediaPlayer = new MediaPlayer();

        try{
            mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            mediaPlayer.setLooping(true);
            mediaPlayer.setVolume(_sound.getVolume(),_sound.getVolume());
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.seekTo(1000);
                    if(_sound.isEnabled()){
                        mediaPlayer.start();
                    }
                }
            });
            mediaPlayer.prepareAsync();
        }catch (IOException e) {
            e.printStackTrace();
        }

        return mediaPlayer;
    }
    private MediaPlayer createSoundDuplicate(final Sound _sound){
        AssetFileDescriptor afd = mContext.getResources().openRawResourceFd(_sound.getPath_sound_duplicate());
        final MediaPlayer mediaPlayer = new MediaPlayer();

        try{
            mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            mediaPlayer.setLooping(true);
            mediaPlayer.setVolume(_sound.getVolume(),_sound.getVolume());
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.seekTo(0);
                    if(_sound.isEnabled()){
                        mediaPlayer.start();
                    }

                }
            });
            mediaPlayer.prepareAsync();
        }catch (IOException e) {
            e.printStackTrace();
        }

        return mediaPlayer;
    }

    /*
     *
     * УДАЛЕНИЕ ТРЕКОВ
     *
     */

    private void deleteMainSound(Sound _sound){

        Sound remove_this = null;

        for(Map.Entry<Sound,MediaPlayer> entry : soundListiner.entrySet()){
            if(entry.getKey().getName().equals(_sound.getName())){
                entry.getValue().stop();
                entry.getValue().reset();
                entry.getValue().release();
                entry.setValue(null);
                remove_this = entry.getKey();
                break;
            }
        }
        soundListiner.remove(remove_this);
    }
    private void deleteSoundDuplicate(Sound _sound){
        Sound remove_this = null;

        for(Map.Entry<Sound,MediaPlayer> entry : soundListinerDiplacite.entrySet()){
            if(entry.getKey().getName().equals(_sound.getName())){
                entry.getValue().stop();
                entry.getValue().reset();
                entry.getValue().release();
                entry.setValue(null);
                remove_this = entry.getKey();
                break;
            }
        }
        soundListinerDiplacite.remove(remove_this);
    }


    /*
     *
     * ЗАПУСК ТРЕКА
     *
     */

    private void playMainSound(Sound _sound){
        for (Map.Entry<Sound,MediaPlayer> entry : soundListiner.entrySet()){
            if(entry.getKey().getName().equals(_sound.getName())){
                entry.getValue().seekTo(entry.getKey().getSound_sec());
                entry.getValue().setVolume(entry.getKey().getVolume(),entry.getKey().getVolume());
                entry.getKey().setVol_for_fade(entry.getKey().getVolume());
                entry.getValue().start();
                entry.getKey().setEnabled(true);
            }
        }
    }
    private void playDuplicateSound(Sound _sound){
        for (Map.Entry<Sound,MediaPlayer> entry : soundListinerDiplacite.entrySet()){
            if(entry.getKey().getName().equals(_sound.getName())){
                entry.getValue().seekTo(entry.getKey().getSound_sec());
                entry.getValue().setVolume(entry.getKey().getVolume(),entry.getKey().getVolume());
                entry.getKey().setVol_for_fade(entry.getKey().getVolume());
                entry.getValue().start();
                entry.getKey().setEnabled(true);
            }
        }
    }

    /*
     *
     * ОСТАНОВКА ТРЕКА
     *
     */

    private void stopMainSound(Sound _sound){
        for (Map.Entry<Sound,MediaPlayer> entry : soundListiner.entrySet()){
            if(entry.getKey().getName().equals(_sound.getName())){

                entry.getKey().setSound_sec(entry.getValue().getCurrentPosition());
                entry.getValue().pause();
                entry.getKey().setEnabled(false);

            }
        }
    }
    private void stopDuplicateSound(Sound _sound){
        for (Map.Entry<Sound,MediaPlayer> entry : soundListinerDiplacite.entrySet()){
            if(entry.getKey().getName().equals(_sound.getName())){

                entry.getKey().setSound_sec(entry.getValue().getCurrentPosition());
                entry.getValue().pause();
                entry.getKey().setEnabled(false);

            }
        }
    }

    /*
     *
     * ОСТАНОВКА ВСЕХ ТРЕКОВ
     *
     */

    private void  stopAllMainSoundTimer(){

        for (Map.Entry<Sound,MediaPlayer> entry: soundListiner.entrySet())
        {
            /*entry.getKey().setSound_sec(entry.getValue().getCurrentPosition());
            entry.getValue().pause();
            entry.getKey().setEnabled(false);*/
            fadeOutSoundMain(entry.getKey());
        }

    }
    private void  stopAllDuplicateSoundTimer(){

        for (Map.Entry<Sound,MediaPlayer> entry: soundListinerDiplacite.entrySet())
        {
            /*entry.getKey().setSound_sec(entry.getValue().getCurrentPosition());
            entry.getValue().pause();
            entry.getKey().setEnabled(false);*/
            fadeOutSoundDuplicate(entry.getKey());

        }

    }

    private void  stopAllMainSound(){

        for (Map.Entry<Sound,MediaPlayer> entry: soundListiner.entrySet())
        {
            entry.getKey().setSound_sec(entry.getValue().getCurrentPosition());
            entry.getValue().pause();
            entry.getKey().setEnabled(false);

        }

    }
    private void  stopAllDuplicateSound(){

        for (Map.Entry<Sound,MediaPlayer> entry: soundListinerDiplacite.entrySet())
        {
            entry.getKey().setSound_sec(entry.getValue().getCurrentPosition());
            entry.getValue().pause();
            entry.getKey().setEnabled(false);

        }

    }

    private void fadeOutSoundMain(final Sound _sound){

        final MediaPlayer mediaPlayer = soundListiner.get(_sound);
        _sound.setSound_sec(mediaPlayer.getCurrentPosition());
        _sound.setEnabled(false);

        final Timer timer = new Timer(true);;
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if(mediaPlayer.isPlaying() && !_sound.isEnabled()){
                    fadeAudio(_sound,mediaPlayer);
                }else{
                    timer.cancel();
                }
            }
        };
        timer.schedule(timerTask, 0, 50);

    }
    private void fadeOutSoundDuplicate(final Sound _sound){

        final MediaPlayer mediaPlayer = soundListinerDiplacite.get(_sound);
        _sound.setSound_sec(mediaPlayer.getCurrentPosition());
        _sound.setEnabled(false);

        final Timer timer = new Timer(true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if(mediaPlayer.isPlaying()&& !_sound.isEnabled()){
                    fadeAudio(_sound,mediaPlayer);
                }else{
                    timer.cancel();
                }
            }
        };
        timer.schedule(timerTask, 0, 50);

    }
    private void fadeAudio(Sound vol, MediaPlayer _sound){

        Log.d("ТУТ",vol.getVol_for_fade()+"");


        if(vol.getVol_for_fade()-0.01f > 0){
            _sound.setVolume(vol.getVol_for_fade()-0.01f,vol.getVol_for_fade()-0.01f);
            vol.setVol_for_fade(vol.getVol_for_fade()-0.01f);
        }else{
            _sound.pause();
        }

    }

    /*
     *
     * ЗАПУСК ВСЕХ ТРЕКОВ
     *
     */

    private void  playAllMainSound(){

        for (Map.Entry<Sound,MediaPlayer> entry: soundListiner.entrySet())
        {
            entry.getValue().seekTo(entry.getKey().getSound_sec());
            entry.getValue().setVolume(entry.getKey().getVolume(),entry.getKey().getVolume());
            entry.getKey().setVol_for_fade(entry.getKey().getVolume());
            entry.getValue().start();
            entry.getKey().setEnabled(true);
        }

    }
    private void  playAllDuplicateSound(){

        for (Map.Entry<Sound,MediaPlayer> entry: soundListinerDiplacite.entrySet())
        {
            entry.getValue().seekTo(entry.getKey().getSound_sec());
            entry.getValue().setVolume(entry.getKey().getVolume(),entry.getKey().getVolume());
            entry.getKey().setVol_for_fade(entry.getKey().getVolume());
            entry.getValue().start();
            entry.getKey().setEnabled(true);
        }

    }

    /*
     *
     * ИЗМЕНЕНИЕ ГРОМКОСТИ
     *
     */

    private void changeVolumeMain(Sound _sound){

        for(Map.Entry<Sound,MediaPlayer> entry : soundListiner.entrySet()){
            if(entry.getKey().getName().equals(_sound.getName())){
                entry.getValue().setVolume(_sound.getVolume(),_sound.getVolume());
                entry.getKey().setVolume(_sound.getVolume());
                entry.getKey().setVol_for_fade(_sound.getVolume());
                Log.d("Volume",_sound.getVolume()+"");
                return;
            }
        }
    }
    private void changeVolumeDuplicate(Sound _sound){

        for(Map.Entry<Sound,MediaPlayer> entry : soundListinerDiplacite.entrySet()){
            if(entry.getKey().getName().equals(_sound.getName())){
                entry.getValue().setVolume(_sound.getVolume(),_sound.getVolume());
                entry.getKey().setVolume(_sound.getVolume());
                entry.getKey().setVol_for_fade(_sound.getVolume());
                Log.d("Volume",_sound.getVolume()+"");
                return;
            }
        }
    }
}
