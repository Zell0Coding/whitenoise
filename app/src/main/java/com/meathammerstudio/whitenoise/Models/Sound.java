package com.meathammerstudio.whitenoise.Models;

public class Sound {

    private String name;
    private int path_img;
    private int path_sound;
    private double volume;
    private boolean enabled;


    public Sound(String _name,int _path_img,int _path_sound, double _volume, boolean _enable){
        this.name = _name;
        this.path_img = _path_img;
        this.volume = _volume;
        this.enabled = _enable;
        this.path_sound = _path_sound;
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


    public void setName(String name) {
        this.name = name;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
