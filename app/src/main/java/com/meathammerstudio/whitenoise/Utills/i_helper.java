package com.meathammerstudio.whitenoise.Utills;
import com.meathammerstudio.whitenoise.Models.Languages;
import com.meathammerstudio.whitenoise.Models.Sound;
import com.meathammerstudio.whitenoise.Models.Timer;

import java.util.List;

public interface i_helper {

    interface i_sound{
        void addSound(Sound _sound, boolean re_create);
        void playSound(Sound _sound);
        void stopSound(Sound _sound);
        void deleteSound(Sound _sound);
        void changeVolume(Sound _sound);
    }

    interface i_timer{
        void updateTimer(List<Timer> _timers);
        void enableTimer(Timer timer);
        void disableTimer();
    }

    interface i_language{
        void selectLanguage(Languages.language language);
    }

    interface i_timer_servies{
        void startTimer(Timer timer);
        void stopTimer();
    }

}
