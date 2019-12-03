package com.ponicamedia.android.whitenoise.Adapters;

import android.Manifest;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.gridlayout.widget.GridLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ponicamedia.android.whitenoise.Models.CasheSounds;
import com.ponicamedia.android.whitenoise.Models.Sound;
import com.ponicamedia.android.whitenoise.R;
import com.ponicamedia.android.whitenoise.Utills.Manager;
import com.ponicamedia.android.whitenoise.Utills.Utill;
import com.ponicamedia.android.whitenoise.Utills.i_helper;

import java.util.ArrayList;
import java.util.List;

public class PagesAdapter extends PagerAdapter implements i_helper.clickMusic {

    public interface getSound{
        List<Sound> getSounds();
        boolean hasPremium();
        void clickOnSound(Sound sound);
    }

    private Context mContext;
    private GridLayout[] page = new GridLayout[3] ;
    private getSound sounds;

    public PagesAdapter(Context mContext,getSound sounds) {
        this.mContext = mContext;
        this.sounds = sounds;
    }

    @Override
    public int getCount() {
        return page.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        List<Sound> temp = sounds.getSounds();


        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final GridLayout itemView = (GridLayout)inflater.inflate(R.layout.page_layout,container,false);
        int min = 0, max = 0;

        switch (position){
            case 0:
                min = 0;
                max = 8;
                break;
            case 1:
                min = 9;
                max = 17;
                break;
            case 2:
                min = 18;
                max = 26;
                break;
        }
        for (int i = min; i <= max; i++){

            View view = inflater.inflate(R.layout.play_item,itemView,false);
            Sound vol = temp.get(i);

            ImageButton button = view.findViewById(R.id.button);
            ImageView indicator = view.findViewById(R.id.indicator);

            int indicator_path;
            if(vol.isEnabled()){
                 indicator_path = Utill.PAUSE_INDICATOR;
            }else if(vol.isPremium() && !sounds.hasPremium()){
                indicator_path =  Utill.CLOSE_INDICATOR;
            }else{
                indicator_path =  Utill.PLAY_INDICATOR;
            }

            button.setImageDrawable(mContext.getResources().getDrawable(vol.getPath_img()));
            indicator.setImageDrawable(mContext.getResources().getDrawable(indicator_path));


            soundClick(vol,button,indicator);
            view.setTag(vol.getName());
            itemView.addView(view);
            Log.d("DEBUG",vol.isEnabled() + vol.getName());
        }
        page[position] = itemView;
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public void soundClick(final Sound sound, final ImageButton button, final ImageView img) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sounds.clickOnSound(sound);
            }

        });
    }

    public void updateSoundState(String name, boolean state){
        try {
            int indicator = (state) ? R.drawable.ic_feather_pause_circle : R.drawable.ic_outline_play_circle_filled_white;
            for (GridLayout gridLayout : page){
                if (gridLayout!=null){
                    int count = gridLayout.getChildCount();
                    for (int i = 0; i < count; i++){
                        if(gridLayout.getChildAt(i).getTag().equals(name)){
                            ImageView img = gridLayout.getChildAt(i).findViewById(R.id.indicator);
                            img.setImageResource(indicator);
                            notifyDataSetChanged();

                            return;
                        }
                    }
                }
            }

        }catch (RuntimeException e){

        }
    }
}
