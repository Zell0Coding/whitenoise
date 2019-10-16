package com.meathammerstudio.whitenoise.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.meathammerstudio.whitenoise.Models.Timer;
import com.meathammerstudio.whitenoise.R;
import com.meathammerstudio.whitenoise.Utills.ItemTouchHelperAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class timerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter {

    private Context mContext;
    private List<Timer> timers;

    public timerAdapter(Context _context){
        mContext = _context;
        timers = new ArrayList<>();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.timer_item,parent,false);
        return new item(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        final item Item = (item)holder;

        Item.time.setText(timers.get(position).getTime());

        if(timers.get(position).isEnable()){
            Item.mSwitchCompat.setChecked(true);
            Item.mSwitchCompat.setThumbResource(R.drawable.switch_enable_thumb);
            Item.mSwitchCompat.setTrackResource(R.drawable.switch_track);
        }else{
            Item.mSwitchCompat.setChecked(false);
            Item.mSwitchCompat.setThumbResource(R.drawable.switch_disable_thumb);
            Item.mSwitchCompat.setTrackResource(R.drawable.switch_disable);
        }

        Item.mSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Item.mSwitchCompat.setThumbResource(R.drawable.switch_enable_thumb);
                    Item.mSwitchCompat.setTrackResource(R.drawable.switch_track);
                    ReSelectItem(holder.getAdapterPosition(),true);
                }else{
                    Item.mSwitchCompat.setThumbResource(R.drawable.switch_disable_thumb);
                    Item.mSwitchCompat.setTrackResource(R.drawable.switch_disable);
                    ReSelectItem(holder.getAdapterPosition(),false);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return timers.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(timers, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {

        timers.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, timers.size());
    }

    public void addTimer(Timer timer){
        timers.add(timer);
        notifyDataSetChanged();
    }

    private void ReSelectItem(int position, boolean isEnable){
        timers.get(position).setEnable(isEnable);
        if(isEnable){
            for(int i = 0; i < timers.size();i++){
                if(i!=position){
                    timers.get(i).setEnable(false);
                    notifyItemChanged(i);
                }
            }
        }
    }

    class item extends RecyclerView.ViewHolder{

        TextView time;
        SwitchCompat mSwitchCompat;

         item(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.timer_time);
            mSwitchCompat = itemView.findViewById(R.id.timer_switch);
        }
    }
}
