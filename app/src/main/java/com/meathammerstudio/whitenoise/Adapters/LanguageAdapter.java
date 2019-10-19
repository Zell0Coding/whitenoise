package com.meathammerstudio.whitenoise.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meathammerstudio.whitenoise.Models.Languages;
import com.meathammerstudio.whitenoise.R;
import com.meathammerstudio.whitenoise.Utills.Manager;
import com.meathammerstudio.whitenoise.Utills.i_helper;

import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER = 0;
    private static final int NORMAL_ITEM = 1;

    private List<Languages.language> mLanguages;
    private Context mContext;
    private i_helper.i_language mI_language;
    private Manager current;


    public LanguageAdapter(Context _context, Manager currentLang, i_helper.i_language _language){
        mContext = _context;
        mI_language = _language;
        current = currentLang;
        Languages languages = new Languages();
        mLanguages = languages.getLanguages();
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.language_text_recycler,parent,false);
            return new labelItem(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.language_recycler_item,parent,false);
            return new languageItem(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof languageItem){

            final int pos_ = position;

            languageItem item = (languageItem)holder;
            item.language_img.setImageResource(mLanguages.get(position).getImg());
            item.language_label.setText(mLanguages.get(position).getName());
            if(mLanguages.get(position).getAbbr().equals(current.getCurrentLang())){
                item.language_enable.setVisibility(View.VISIBLE);
            }else{
                item.language_enable.setVisibility(View.GONE);
            }

            item.mLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectLanguage(pos_);
                }
            });
        }
    }

    private void selectLanguage(int position){
        for(int i = 0; i < mLanguages.size();i++){
           if(i!=position) mLanguages.get(i).setEnable(false);
        }
        mLanguages.get(position).setEnable(true);
        mI_language.selectLanguage(mLanguages.get(position));
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mLanguages.size();
    }

    class labelItem extends RecyclerView.ViewHolder{

        labelItem(View view){
            super(view);
        }

    }
    class languageItem extends RecyclerView.ViewHolder{

        LinearLayout mLinearLayout;
        ImageView language_img;
        TextView language_label;
        ImageView language_enable;

        languageItem(View view){
            super(view);
            mLinearLayout = view.findViewById(R.id.lang);
            language_img = view.findViewById(R.id.language_img);
            language_label = view.findViewById(R.id.language_label);
            language_enable = view.findViewById(R.id.language_enable);
        }
    }

}
