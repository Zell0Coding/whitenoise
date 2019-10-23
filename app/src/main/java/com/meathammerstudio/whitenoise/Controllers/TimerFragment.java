package com.meathammerstudio.whitenoise.Controllers;

import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
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

public class TimerFragment extends Fragment implements View.OnClickListener, i_helper.i_timer, SwipeToDeleteCallback.RecyclerItemTouchHelperListener {

    private ImageButton addTimer;
    private ItemTouchHelper mItemTouchHelper;
    private RecyclerView timerRecycler;
    private timerAdapter timerAdapter;
    private CoordinatorLayout mCoordinatorLayout;
    private Manager mManager;

    private TextView timerText;

    private i_helper.i_timer_servies services;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        services = (i_helper.i_timer_servies) context;
    }

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
        timerText = view.findViewById(R.id.timer_text);
        timerAdapter = new timerAdapter(getContext(),this);
        mCoordinatorLayout = view.findViewById(R.id.coordinator_layout);
        timerRecycler = view.findViewById(R.id.timer_recycler);
        timerRecycler.setItemAnimator(new DefaultItemAnimator());
        timerRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        timerRecycler.setAdapter(timerAdapter);


        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new SwipeToDeleteCallback(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(timerRecycler);



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
            Log.d("TIMER","сохранение для :" + _timers.get(i).getHours() + ":" + _timers.get(i).getMinute());
        }
        Gson gson = new Gson();
        String json_data = gson.toJson(timerContainer);
        StorageManager.writeToFile(Utill.TIMER_SELECTED,json_data,getContext());
    }

    @Override
    public void enableTimer(Timer timer) {

        services.startTimer(timer);
        timerText.setVisibility(View.VISIBLE);

    }

    @Override
    public void disableTimer() {
        services.stopTimer();
        timerText.setVisibility(View.GONE);
    }

    public TimerContainer loadTimers(){

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


    public void updateTimer(String text){
        timerText.setText(text);
        if(timerText.getVisibility()==View.GONE) timerText.setVisibility(View.VISIBLE);
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



    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof timerAdapter.item) {
            // get the removed item name to display it in snack bar

            // backup of removed item for undo purpose
            final Timer deletedItem = timerAdapter.getItem(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            timerAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(mCoordinatorLayout, "Removed from timers!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    timerAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(getResources().getColor(R.color.colorText));
            snackbar.show();
        }
    }


}
