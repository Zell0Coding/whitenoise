package com.ponicamedia.android.whitenoise.Controllers;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

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
import com.ponicamedia.android.whitenoise.Adapters.SwipeToDeleteCallback;
import com.ponicamedia.android.whitenoise.Adapters.timerAdapter;
import com.ponicamedia.android.whitenoise.Models.Timer;
import com.ponicamedia.android.whitenoise.Models.TimerContainer;
import com.ponicamedia.android.whitenoise.R;
import com.ponicamedia.android.whitenoise.Utills.Manager;
import com.ponicamedia.android.whitenoise.Utills.StorageManager;
import com.ponicamedia.android.whitenoise.Utills.Utill;
import com.ponicamedia.android.whitenoise.Utills.i_helper;

import java.util.List;

public class TimerFragment extends Fragment implements View.OnClickListener, i_helper.i_timer, SwipeToDeleteCallback.RecyclerItemTouchHelperListener {

    private ImageButton addTimer;
    private ItemTouchHelper mItemTouchHelper;
    private RecyclerView timerRecycler;
    private timerAdapter timerAdapter;
    private CoordinatorLayout mCoordinatorLayout;
    private Manager mManager;

    private TextView timerTextHours;
    private TextView timerTextDots;
    private TextView timerTextMinute;

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

        timerTextHours = view.findViewById(R.id.timer_hours);
        timerTextDots = view.findViewById(R.id.timer_dots);
        timerTextMinute = view.findViewById(R.id.timer_minute);

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
        timerTextHours.setVisibility(View.VISIBLE);
        timerTextDots.setVisibility(View.VISIBLE);
        timerTextMinute.setVisibility(View.VISIBLE);


    }

    @Override
    public void disableTimer() {
        services.stopTimer();
        timerTextHours.setVisibility(View.GONE);
        timerTextDots.setVisibility(View.GONE);
        timerTextMinute.setVisibility(View.GONE
        );
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


    public void updateTimer(String hours, String minute, int dots){
        timerTextHours.setText(hours);
        timerTextMinute.setText(minute);

        if(timerTextHours.getVisibility()==View.GONE){
            timerTextHours.setVisibility(View.VISIBLE);
            timerTextMinute.setVisibility(View.VISIBLE);
            timerTextDots.setVisibility(View.VISIBLE);
        }

        if(dots==1) {
            timerTextDots.setText(":");
        }else{
            timerTextDots.setText("");
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
