package com.meathammerstudio.whitenoise.Models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class sleepModel {

    private boolean isEnable;
    private int sleepingHour;
    private int sleepingMinute;
    private List<String> days;

    public sleepModel(){
        isEnable = false;
        days = new ArrayList<>();
        //sleepingHour = Calendar.getInstance().get(Calendar.HOUR);
        //sleepingMinute = Calendar.getInstance().get(Calendar.MINUTE);
    }

    public boolean isEnable() {
        return isEnable;
    }
    public void setEnable(boolean _state){isEnable = _state;}
    public void changeDay(String day){
        for(int i = 0; i < days.size(); i++){
            if(days.get(i).equals(day)){
                days.remove(i);
                return;
            }
        }
        days.add(day);
    }
    public List<String> getDays() {
        return days;
    }
    public int getSleepingHour() {
        return sleepingHour;
    }
    public void setSleepingHour(int sleepingHour) {
        this.sleepingHour = sleepingHour;
    }
    public int getSleepingMinute() {
        return sleepingMinute;
    }
    public void setSleepingMinute(int sleepingMinute) {
        this.sleepingMinute = sleepingMinute;
    }
}
