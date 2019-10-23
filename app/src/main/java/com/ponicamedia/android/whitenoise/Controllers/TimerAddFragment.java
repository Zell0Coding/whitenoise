package com.ponicamedia.android.whitenoise.Controllers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ponicamedia.android.whitenoise.Models.Timer;
import com.ponicamedia.android.whitenoise.Models.TimerContainer;
import com.ponicamedia.android.whitenoise.R;
import com.ponicamedia.android.whitenoise.Utills.StorageManager;
import com.ponicamedia.android.whitenoise.Utills.Utill;

public class TimerAddFragment extends Fragment {

    private TimePicker mTimePicker;
    private ImageButton mImageButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timer_add_fragment,container,false);

        mTimePicker = view.findViewById(R.id.timer_picker);
        mImageButton = view.findViewById(R.id.add_new_timer);

        mTimePicker.setCurrentHour(0);
        mTimePicker.setCurrentMinute(15);

        mTimePicker.setIs24HourView(true);

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTimer();
                getFragmentManager().popBackStack();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void saveTimer(){
        int hours = mTimePicker.getCurrentHour();
        int minutes = mTimePicker.getCurrentMinute();
        Timer timer = new Timer(hours,minutes,false);

        TimerContainer timers = loadTimers();
        if(timers!=null){
            timers.addTimer(timer);
        }else{
            timers = new TimerContainer();
            timers.addTimer(timer);
        }

        Gson gson = new Gson();
        String json_data = gson.toJson(timers);
        StorageManager.writeToFile(Utill.TIMER_SELECTED,json_data,getContext());
    }

    private TimerContainer loadTimers(){

        try {
            String data = StorageManager.readFromFile(Utill.TIMER_SELECTED, getContext());
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            TimerContainer timerContainer = gson.fromJson(data,TimerContainer.class);
            return timerContainer;
        }catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }
    }




}
