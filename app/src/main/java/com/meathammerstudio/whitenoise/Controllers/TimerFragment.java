package com.meathammerstudio.whitenoise.Controllers;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.meathammerstudio.whitenoise.Adapters.SwipeToDeleteCallback;
import com.meathammerstudio.whitenoise.Adapters.timerAdapter;
import com.meathammerstudio.whitenoise.Models.Timer;
import com.meathammerstudio.whitenoise.Models.TimerContainer;
import com.meathammerstudio.whitenoise.R;
import com.meathammerstudio.whitenoise.Services.timeServices;
import com.meathammerstudio.whitenoise.Utills.Manager;
import com.meathammerstudio.whitenoise.Utills.StorageManager;
import com.meathammerstudio.whitenoise.Utills.Utill;
import com.meathammerstudio.whitenoise.Utills.i_helper;

import java.util.Calendar;
import java.util.List;

public class TimerFragment extends Fragment implements View.OnClickListener, i_helper.i_timer {

    private ImageButton addTimer;
    private ItemTouchHelper mItemTouchHelper;
    private RecyclerView timerRecycler;
    private timerAdapter timerAdapter;
    private Manager mManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mManager = Manager.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.timer_fragment,container,false);

        addTimer = view.findViewById(R.id.add_timer);
        addTimer.setOnClickListener(this);
        timerAdapter = new timerAdapter(getContext(),this);

        timerRecycler = view.findViewById(R.id.timer_recycler);
        timerRecycler.setItemAnimator(new DefaultItemAnimator());
        timerRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        timerRecycler.setAdapter(timerAdapter);


        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ItemTouchHelper.Callback callback = new SwipeToDeleteCallback(timerAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(timerRecycler);

        re_CreateTimers();

    }

    private void re_CreateTimers(){
        TimerContainer timerContainer = loadTimers();
        if(timerContainer!=null){
            Log.d("тут","тут");
            for(int i = 0; i < timerContainer.mTimerList.size();i++){
                timerAdapter.addTimer(timerContainer.mTimerList.get(i));
                Log.d("addNew",timerContainer.mTimerList.get(i).getHours()+"");
            }
        }
    }

    // ------------------ СОХРАНЕНИЕ И ЗАГРУЗКА -------------------------------

    @Override
    public void updateTimer(List<Timer> _timers) {
        TimerContainer timerContainer = new TimerContainer();
        for(int i = 0; i < _timers.size(); i++){
            timerContainer.addTimer(_timers.get(i));
        }
        Gson gson = new Gson();
        String json_data = gson.toJson(timerContainer);
        StorageManager.writeToFile(Utill.TIMER_SELECTED,json_data,getContext());
    }

    @Override
    public void enableTimer(Timer timer) {
        mManager.setCurrent_timer_hour(timer.getHours());
        mManager.setCurrent_timer_minute(timer.getMinute());
        getActivity().startService(new Intent(getActivity(),timeServices.class));
    }

    @Override
    public void disableTimer() {
        try{
            getActivity().stopService(new Intent(getActivity(),timeServices.class));
        }catch (NullPointerException e){
            e.printStackTrace();
        }
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

    // --------------------------------------------------------------------


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_timer:

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                TimerAddFragment timerAddFragmen = new TimerAddFragment();
                fragmentTransaction.replace(R.id.container,timerAddFragmen);
                fragmentTransaction.addToBackStack("timer_fragment");
                fragmentTransaction.commit();

                break;
        }
    }



}
