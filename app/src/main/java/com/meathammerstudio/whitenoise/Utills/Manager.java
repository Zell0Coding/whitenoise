package com.meathammerstudio.whitenoise.Utills;

import com.meathammerstudio.whitenoise.Models.SoundListiner;

public class Manager {

    private static Manager instance;
    private SoundListiner mSoundListiner;

    private int width;
    private int height;

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

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
