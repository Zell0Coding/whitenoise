package com.meathammerstudio.whitenoise.Utills;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.meathammerstudio.whitenoise.Models.Sound;

public interface i_helper {

    interface i_sound{
        void addSound(Sound _sound);
        void playSound(Sound _sound);
        void stopSound(Sound _sound);
        void deleteSound(Sound _sound);
        void changeVolume(Sound _sound);
    }

}
