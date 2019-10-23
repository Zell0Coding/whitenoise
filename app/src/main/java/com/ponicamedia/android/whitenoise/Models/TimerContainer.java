package com.ponicamedia.android.whitenoise.Models;

import java.util.ArrayList;
import java.util.List;

public class TimerContainer {

    public List<Timer> mTimerList = new ArrayList<>();

    public void addTimer(Timer timer){
        mTimerList.add(timer);
    }

}
