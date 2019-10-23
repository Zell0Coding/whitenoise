package com.ponicamedia.android.whitenoise.Models;

import java.util.ArrayList;
import java.util.List;

public class SoundContainer {

    public List<Sound> mSoundList = new ArrayList<>();

    public void addSound(Sound sound){
        mSoundList.add(sound);
    }

}
