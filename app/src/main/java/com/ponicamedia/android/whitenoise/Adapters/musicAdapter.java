package com.ponicamedia.android.whitenoise.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.recyclerview.widget.RecyclerView;

import com.ponicamedia.android.whitenoise.Models.Sound;
import com.ponicamedia.android.whitenoise.R;
import com.ponicamedia.android.whitenoise.Utills.ItemTouchHelperAdapter;
import com.ponicamedia.android.whitenoise.Utills.i_helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class musicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  implements ItemTouchHelperAdapter {

    public interface updateButton{
         void update(String name, boolean enable);
    }
    private updateButton update;
    private i_helper.i_sound sound_helper;

    private static final int HEADER = 0;
    private static final int NORMAL_ITEM = 1;

    private List<Sound> sounds;

    public musicAdapter(updateButton _update, i_helper.i_sound _sound_helper){
        update = _update;
        sound_helper = _sound_helper;
        sounds = new ArrayList<>();
        sounds.add(null);
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return HEADER;
        }else{
            return NORMAL_ITEM;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       if(getItemViewType(viewType) == HEADER){
           View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_text,parent,false);
           return new labelView(view);
       }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sound_item,parent,false);
            return new SoundView(view);
       }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof SoundView){
            final Sound sound = getItem(position);
            final SoundView view = (SoundView)holder;
            final int _pos = position;

            int vol = (int)(sound.getVolume()*100);

            view.sound_picture.setImageResource(sound.getPath_img());
            view.volume.setProgress(vol);

            if(sound.isEnabled()){
                view.sound_enable.setImageResource(R.drawable.ic_feather_pause_circle);
            }else{
                view.sound_enable.setImageResource(R.drawable.ic_outline_play_circle_filled_white);
            }

            view.sound_picture.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(sound.isEnabled()){
                        view.sound_enable.setImageResource(R.drawable.ic_outline_play_circle_filled_white);
                        sound.setEnabled(false);
                        update.update(sound.getName(),false);
                        sound_helper.stopSound(sound);
                    }else{
                        view.sound_enable.setImageResource(R.drawable.ic_feather_pause_circle);
                        sound.setEnabled(true);
                        update.update(sound.getName(),true);
                        sound_helper.playSound(sound);
                    }
                }
            });

            view.sound_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onItemDismiss(_pos);

                }
            });

            view.volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    float vol = (float)progress/100;
                    sound.setVolume(vol);
                    sound_helper.changeVolume(sound);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

        }

    }

    public List<Sound> getItems(){
        return sounds;
    }

    public void allStop(){
        for(int i = 0; i < sounds.size();i++){
            if(sounds.get(i)!=null){
                sounds.get(i).setEnabled(false);
                update.update(sounds.get(i).getName(),false);
            }
        }
        notifyDataSetChanged();
    }

    public void allPlay(){
        for(int i = 0; i < sounds.size();i++){

            if(sounds.get(i)!=null){
                sounds.get(i).setEnabled(true);
                update.update(sounds.get(i).getName(),true);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return sounds.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(sounds, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        if (position==0)
            return;

        try{
            Sound sound = sounds.get(position);
            sounds.remove(position);
            sound_helper.deleteSound(sound);
            update.update(sound.getName(),false);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, sounds.size());
        }catch (NullPointerException e){

        }
    }

    public boolean getItem(Sound sound){
        for(int i = 1; i < sounds.size();i++){
            if(sounds.get(i).getName().equals(sound.getName())){
                return true;
            }
        }
        return false;
    }

    private Sound getItem(int position){
        return sounds.get(position);
    }
    public void addNewSong(Sound _sound, boolean reCreate){

        int current_id = 0;
        boolean include = false;

        for(int i = 1; i < sounds.size();i++){
            Log.d("NAME",sounds.get(i).getName() + " " + _sound.getName());
            if(sounds.get(i).getName().equals(_sound.getName())){
                include = true;
                current_id = i;
                Log.d("NAME",sounds.get(i).getName() + "== " + _sound.getName());
                break;
            }
        }
        if(!include){
            sounds.add(_sound);
            sound_helper.addSound(_sound, reCreate);
            update.update(_sound.getName(),true);

            //notifyItemChanged(current_id);
            notifyDataSetChanged();

            return;
        }else{
            if(sounds.get(current_id).isEnabled()){
                sounds.get(current_id).setEnabled(false);
                update.update(sounds.get(current_id).getName(),false);
                sound_helper.stopSound(sounds.get(current_id));
            }else{
                sounds.get(current_id).setEnabled(true);
                update.update(sounds.get(current_id).getName(),true);
                sound_helper.playSound(sounds.get(current_id));
            }
            notifyItemChanged(current_id);
        }
    }

    class labelView extends RecyclerView.ViewHolder{

        TextView label;

         labelView(@NonNull View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.text_recycler);

        }
    }
    class SoundView extends RecyclerView.ViewHolder{

        ImageButton sound_picture;
        ImageButton sound_delete;
        ImageView sound_enable;
        AppCompatSeekBar volume;

         SoundView(@NonNull View itemView){
            super(itemView);
            sound_picture = itemView.findViewById(R.id.sound_image);
            sound_enable = itemView.findViewById(R.id.sound_indicator);
            sound_delete = itemView.findViewById(R.id.sound_delete);
            volume = itemView.findViewById(R.id.sound_volume);

        }

    }

}
