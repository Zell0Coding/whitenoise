package com.meathammerstudio.whitenoise.Controllers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meathammerstudio.whitenoise.Adapters.SwipeToDeleteCallback;
import com.meathammerstudio.whitenoise.Adapters.timerAdapter;
import com.meathammerstudio.whitenoise.Models.Timer;
import com.meathammerstudio.whitenoise.R;

public class TimerFragment extends Fragment {

    private ImageButton addTimer;
    private ItemTouchHelper mItemTouchHelper;
    private RecyclerView timerRecycler;
    private timerAdapter timerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.timer_fragment,container,false);

        addTimer = view.findViewById(R.id.add_timer);

        timerAdapter = new timerAdapter(getContext());

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

        timerAdapter.addTimer(new Timer("21:17",true));
        timerAdapter.addTimer(new Timer("21:22",false));
        timerAdapter.addTimer(new Timer("19:13",false));

    }
}
