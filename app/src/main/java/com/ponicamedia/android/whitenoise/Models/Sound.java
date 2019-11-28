package com.ponicamedia.android.whitenoise.Models;

import android.widget.ImageButton;
import android.widget.ImageView;

public class Sound {


    private String name;
    private int path_img;
    private int path_sound;
    private int path_sound_duplicate;
    private int sound_sec;
    private float volume;
    private float vol_for_fade;
    private boolean enabled;
    private boolean isPremium;

    public ImageButton getButton() {
        return button;
    }

    public void setButton(ImageButton button) {
        this.button = button;
    }

    public ImageView getIndicator() {
        return indicator;
    }

    public void setIndicator(ImageView indicator) {
        this.indicator = indicator;
    }

    private ImageButton button;
    private ImageView indicator;


    public Sound(Sound sound){
        this.name = sound.name;
        this.path_img = sound.path_img;
        this.volume = sound.volume;
        this.enabled = sound.enabled;
        this.path_sound = sound.path_sound;
        this.path_sound_duplicate = sound.path_sound_duplicate;
        this.sound_sec = sound.sound_sec;
        this.vol_for_fade = sound.vol_for_fade;
    }

    public Sound(String _name,int _path_img,int _path_sound, int path_sound_duplicate, float _volume, boolean _enable, int _sound_sec, boolean isPremium){
        this.name = _name;
        this.path_img = _path_img;
        this.volume = _volume;
        vol_for_fade = _volume;
        this.enabled = _enable;
        this.path_sound = _path_sound;
        this.path_sound_duplicate = path_sound_duplicate;
        this.sound_sec = _sound_sec;
        this.isPremium = isPremium;
    }

    public float getVol_for_fade() {
        return vol_for_fade;
    }

    public void setVol_for_fade(float vol_for_fade) {
        this.vol_for_fade = vol_for_fade;
    }

    public int getPath_sound_duplicate() {
        return path_sound_duplicate;
    }

    public void setPath_sound_duplicate(int path_sound_duplicate) {
        this.path_sound_duplicate = path_sound_duplicate;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }

    public int getPath_img() {
        return path_img;
    }

    public void setPath_img(int path_img) {
        this.path_img = path_img;
    }

    public int getPath_sound() {
        return path_sound;
    }

    public void setPath_sound(int path_sound) {
        this.path_sound = path_sound;
    }

    public String getName() {
        return name;
    }

    public int getSound_sec() {
        return sound_sec;
    }

    public void setSound_sec(int sound_sec) {
        this.sound_sec = sound_sec;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
        this.vol_for_fade = volume;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
