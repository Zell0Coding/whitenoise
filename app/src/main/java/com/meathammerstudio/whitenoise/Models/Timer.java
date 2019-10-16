package com.meathammerstudio.whitenoise.Models;

public class Timer {

    private String time;
    private boolean enable;


    public Timer(String time, boolean enable) {
        this.time = time;
        this.enable = enable;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public boolean isEnable() {
        return enable;
    }
    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
