package com.ponicamedia.android.whitenoise.Controllers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ponicamedia.android.whitenoise.Models.sleepModel;
import com.ponicamedia.android.whitenoise.R;
import com.ponicamedia.android.whitenoise.Services.AlertReceiver;
import com.ponicamedia.android.whitenoise.Utills.Manager;
import com.ponicamedia.android.whitenoise.Utills.StorageManager;
import com.ponicamedia.android.whitenoise.Utills.Utill;

import java.util.Calendar;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;

public class AsleepFragment extends Fragment implements View.OnClickListener {

    private Manager mManager;
    private sleepModel mSleepModel;
    private boolean canSave = false;

    private TimePicker mTimePicker;
    private SwitchCompat mSwitchCompat;

    private Button dayMonday;
    private Button dayTuesday;
    private Button dayWednesday;
    private Button dayThursday;
    private Button dayFriday;
    private Button daySaturday;
    private Button daySunday;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mManager = Manager.getInstance();
        mSleepModel = loadAsleep();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sleep_layout,container,false);
        mTimePicker = view.findViewById(R.id.timer_picker);
        mSwitchCompat = view.findViewById(R.id.asleep_state);
        dayMonday = view.findViewById(R.id.day_monday);
        dayTuesday = view.findViewById(R.id.day_tuesday);
        dayWednesday = view.findViewById(R.id.day_wednesday);
        dayThursday = view.findViewById(R.id.day_thursday);
        dayFriday = view.findViewById(R.id.day_friday);
        daySaturday = view.findViewById(R.id.day_saturday);
        daySunday = view.findViewById(R.id.day_sunday);

        dayMonday.setOnClickListener(this);
        dayTuesday.setOnClickListener(this);
        dayWednesday.setOnClickListener(this);
        dayThursday.setOnClickListener(this);
        dayFriday.setOnClickListener(this);
        daySaturday.setOnClickListener(this);
        daySunday.setOnClickListener(this);

        mTimePicker.setIs24HourView(true);
        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                if(canSave)
                {
                    mSleepModel.setSleepingHour(hourOfDay);
                    mSleepModel.setSleepingMinute(minute);
                    saveAsleep();
                }
            }
        });

        mSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                mSleepModel.setEnable(isChecked);
                reCreateSwitch(isChecked);
                saveAsleep();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reCreateSwitch(mSleepModel.isEnable());
        reCreateButtons(mSleepModel.getDays());
        reCreateTime();
    }

    private void reCreateSwitch(boolean isEnable){
        if(isEnable){
            mSwitchCompat.setThumbResource(R.drawable.switch_enable_thumb);
            mSwitchCompat.setTrackResource(R.drawable.switch_track);
            mSwitchCompat.setChecked(true);
        }else{
            mSwitchCompat.setThumbResource(R.drawable.switch_disable_thumb);
            mSwitchCompat.setTrackResource(R.drawable.switch_disable);
            mSwitchCompat.setChecked(false);
            cancelAlarm();
        }
    }
    private void defaultButton(){
        dayMonday.setBackgroundResource(R.drawable.day_button_background_non_include);
        dayMonday.setTextAppearance(getContext(),R.style.DayButtonNonInclude);
        dayTuesday.setBackgroundResource(R.drawable.day_button_background_non_include);
        dayTuesday.setTextAppearance(getContext(),R.style.DayButtonNonInclude);
        dayWednesday.setBackgroundResource(R.drawable.day_button_background_non_include);
        dayWednesday.setTextAppearance(getContext(),R.style.DayButtonNonInclude);
        dayThursday.setBackgroundResource(R.drawable.day_button_background_non_include);
        dayThursday.setTextAppearance(getContext(),R.style.DayButtonNonInclude);
        dayFriday.setBackgroundResource(R.drawable.day_button_background_non_include);
        dayFriday.setTextAppearance(getContext(),R.style.DayButtonNonInclude);
        daySaturday.setBackgroundResource(R.drawable.day_button_background_non_include);
        daySaturday.setTextAppearance(getContext(),R.style.DayButtonNonInclude);
        daySunday.setBackgroundResource(R.drawable.day_button_background_non_include);
        daySunday.setTextAppearance(getContext(),R.style.DayButtonNonInclude);
    }
    private void reCreateButtons(List<String> buttons){
        defaultButton();
        for(int i = 0; i < buttons.size(); i++){
            switch (buttons.get(i)){
                case "Mo":
                    dayMonday.setBackgroundResource(R.drawable.day_button_background_include);
                    dayMonday.setTextAppearance(getContext(),R.style.DayButtonInclude);
                    break;
                case "Tu":
                    dayTuesday.setBackgroundResource(R.drawable.day_button_background_include);
                    dayTuesday.setTextAppearance(getContext(),R.style.DayButtonInclude);
                    break;
                case "We":
                    dayWednesday.setBackgroundResource(R.drawable.day_button_background_include);
                    dayWednesday.setTextAppearance(getContext(),R.style.DayButtonInclude);
                    break;
                case "Th":
                    dayThursday.setBackgroundResource(R.drawable.day_button_background_include);
                    dayThursday.setTextAppearance(getContext(),R.style.DayButtonInclude);
                    break;
                case "Fr":
                    dayFriday.setBackgroundResource(R.drawable.day_button_background_include);
                    dayFriday.setTextAppearance(getContext(),R.style.DayButtonInclude);
                    break;
                case "Sa":
                    daySaturday.setBackgroundResource(R.drawable.day_button_background_include);
                    daySaturday.setTextAppearance(getContext(),R.style.DayButtonInclude);
                    break;
                case "Su":
                    daySunday.setBackgroundResource(R.drawable.day_button_background_include);
                    daySunday.setTextAppearance(getContext(),R.style.DayButtonInclude);
                    break;
            }
        }
    }
    private void reCreateTime(){
        mTimePicker.setCurrentHour(mSleepModel.getSleepingHour());
        mTimePicker.setCurrentMinute(mSleepModel.getSleepingMinute());
        canSave = true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.day_monday:
                mSleepModel.changeDay("Mo");
                break;
            case R.id.day_tuesday:
                mSleepModel.changeDay("Tu");
                break;
            case R.id.day_wednesday:
                mSleepModel.changeDay("We");
                break;
            case R.id.day_thursday:
                mSleepModel.changeDay("Th");
                break;
            case R.id.day_friday:
                mSleepModel.changeDay("Fr");
                break;
            case R.id.day_saturday:
                mSleepModel.changeDay("Sa");
                break;
            case R.id.day_sunday:
                mSleepModel.changeDay("Su");
                break;
        }
        saveAsleep();
        reCreateButtons(mSleepModel.getDays());
    }

    private sleepModel loadAsleep(){

        try {
            String data = StorageManager.readFromFile(Utill.ASLEEP, getContext());
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            sleepModel sleepModel = gson.fromJson(data,sleepModel.class);
            if(sleepModel==null){
                sleepModel = new sleepModel();
            }
            return sleepModel;
        }catch (NullPointerException e){
            e.printStackTrace();
            return new sleepModel();
        }
    }

    private void saveAsleep(){
        Gson gson = new Gson();
        String json_data = gson.toJson(mSleepModel);
        StorageManager.writeToFile(Utill.ASLEEP,json_data,getContext());
        if(mSleepModel.isEnable() && canSave)createNotification();
    }


    private void createNotification (){
        cancelAlarm(); // удаляем прошлый аларм
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlertReceiver.class);
        /*  Создаем время и дни для уведомления  */
        for(int i = 0; i < mSleepModel.getDays().size(); i++){

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY , mSleepModel.getSleepingHour());
            calendar.set(Calendar.MINUTE , mSleepModel.getSleepingMinute());
            calendar.set(Calendar.SECOND ,0);

            switch (mSleepModel.getDays().get(i)){
                case "Mo":
                    calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
                    Log.d("ALARM", mSleepModel.getDays().get(i));
                    break;
                case "Tu":
                    calendar.set(Calendar.DAY_OF_WEEK,Calendar.TUESDAY);
                    break;
                case "We":
                    calendar.set(Calendar.DAY_OF_WEEK,Calendar.WEDNESDAY);
                    break;
                case "Th":
                    calendar.set(Calendar.DAY_OF_WEEK,Calendar.THURSDAY);
                    break;
                case "Fr":
                    calendar.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
                    break;
                case "Sa":
                    calendar.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
                    break;
                case "Su":
                    calendar.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
                    break;
            }
            // ----------------------------------------

            if (calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.DATE, 7);
            }

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), i, intent, PendingIntent.FLAG_ONE_SHOT);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),7 * 24 * 3600 * 1000, pendingIntent);
            Log.d("ALARM","Alarm Created for day" + (calendar.getTime().toString()));
        }

    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlertReceiver.class);
        for(int i = 0; i < 7; i ++){
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), i, intent, PendingIntent.FLAG_ONE_SHOT);
            alarmManager.cancel(pendingIntent);
        }

        Log.d("ALARM","Alarm canceled");
    }
}

