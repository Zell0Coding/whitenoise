package com.meathammerstudio.whitenoise.Models;

public class Timer {

    private int hours;
    private int minute;
    private boolean enable;


    public Timer(int hours,int minutes, boolean enable) {
        this.hours = hours;
        this.minute = minutes;
        this.enable = enable;
    }

    public int getHours() {
        return hours;
    }
    public void setHours(int hours) {
        this.hours = hours;
    }
    public int getMinute() {
        return minute;
    }
    public void setMinute(int minute) {
        this.minute = minute;
    }
    public boolean isEnable() {
        return enable;
    }
    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
