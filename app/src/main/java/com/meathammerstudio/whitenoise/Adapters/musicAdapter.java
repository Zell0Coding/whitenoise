package com.meathammerstudio.whitenoise.Adapters;

import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.recyclerview.widget.RecyclerView;

import com.meathammerstudio.whitenoise.Controllers.PlayMusicFragment;
import com.meathammerstudio.whitenoise.Models.Sound;
import com.meathammerstudio.whitenoise.R;
import com.meathammerstudio.whitenoise.Utills.ItemTouchHelperAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class musicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  implements ItemTouchHelperAdapter {


    public interface updateButton{
        void update(String name, boolean enable);
    }
    private updateButton update;


    private static final int HEADER = 0;
    private static final int NORMAL_ITEM = 1;
    private List<Sound> sounds;

    public musicAdapter(updateButton _update){
        update = _update;
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof SoundView){
            Sound sound = getItem(position);
            final SoundView view = (SoundView)holder;

            view.sound_picture.setImageResource(sound.getPath_img());

            if(sound.isEnabled()){
                view.sound_enable.setImageResource(R.drawable.ic_feather_pause_circle);
            }else{
                view.sound_enable.setImageResource(R.drawable.ic_outline_play_circle_filled_white);
            }

            view.sound_picture.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(getItem(position).isEnabled()){
                        view.sound_enable.setImageResource(R.drawable.ic_outline_play_circle_filled_white);
                        getItem(position).setEnabled(false);
                        update.update(getItem(position).getName(),false);
                    }else{
                        view.sound_enable.setImageResource(R.drawable.ic_feather_pause_circle);
                        getItem(position).setEnabled(true);
                        update.update(getItem(position).getName(),true);
                    }
                }
            });

        }

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
        update.update(sounds.get(position).getName(),false);
        sounds.remove(position);
        notifyItemRemoved(position);
    }

    private Sound getItem(int position){
        return sounds.get(position);
    }
    public void addNewSong(Sound _sound){
        for(int i = 1; i < sounds.size();i++){
            if(sounds.get(i).getName().equals(_sound.getName())){
                return;
            }
        }
        sounds.add(_sound);
        notifyItemChanged(sounds.size()-1);
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
        ImageView sound_enable;
        AppCompatSeekBar volume;

         SoundView(@NonNull View itemView){
            super(itemView);
             sound_picture = itemView.findViewById(R.id.sound_image);
            sound_enable = itemView.findViewById(R.id.sound_indicator);
            volume = itemView.findViewById(R.id.sound_volume);

        }

    }

}
