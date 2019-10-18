package com.meathammerstudio.whitenoise.Utills;

import com.meathammerstudio.whitenoise.Models.SoundListiner;

public class Manager {

    private static Manager instance;
    private SoundListiner mSoundListiner;

    private int width;
    private int height;

    private int current_timer_hour;
    private int current_timer_minute;

    private Manager(){mSoundListiner = new SoundListiner();}

    public static Manager getInstance(){ // #3
        if(instance == null){		//если объект еще не создан
            instance = new Manager();	//создать новый объект
        }
        return instance;		// вернуть ранее созданный объект
    }


    public SoundListiner getSoundListiner() {
        return mSoundListiner;
    }

    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }

    public int getCurrent_timer_hour() {
        return current_timer_hour;
    }
    public void setCurrent_timer_hour(int current_timer_hour) {
        this.current_timer_hour = current_timer_hour;
    }
    public int getCurrent_timer_minute() {
        return current_timer_minute;
    }
    public void setCurrent_timer_minute(int current_timer_minute) {
        this.current_timer_minute = current_timer_minute;
    }
}
