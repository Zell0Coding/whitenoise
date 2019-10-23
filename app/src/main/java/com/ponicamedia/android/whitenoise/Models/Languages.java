package com.ponicamedia.android.whitenoise.Models;

import com.ponicamedia.android.whitenoise.Utills.Utill;

import java.util.ArrayList;
import java.util.List;

public class Languages {

    private List<language> mLanguages;
    public Languages(){
        mLanguages = new ArrayList<>();
        language text = new language(0,null,null,false);
        language Russia = new language(Utill.RUSSIA,"Русский","ru",false);
        language English = new language(Utill.ENGLISH,"English","en",false);
        language Deutsch = new language(Utill.DEUTSCH,"Deutsch","de",false);
        language Espanol = new language(Utill.ESPANOL,"Español","es",false);
        language Portugues = new language(Utill.PORTUGUES,"Portugués","pt",false);
        //language Hindi = new language(Utill.HINDI,"Hindi","in",false);
        language Chinese = new language(Utill.CHIENES,"Chinese","zh",false);
        language Indonesian = new language(Utill.INDONESIAN,"Indonesian","in",false);
        language Turkish = new language(Utill.TURKISH,"Turkish","tr",false);
        language Japanese = new language(Utill.JAPANESE,"Japanese","ja",false);

        mLanguages.add(text);
        mLanguages.add(Russia);
        mLanguages.add(English);
        mLanguages.add(Deutsch);
        mLanguages.add(Espanol);
        mLanguages.add(Portugues);
        //mLanguages.add(Hindi);
        mLanguages.add(Chinese);
        mLanguages.add(Indonesian);
        mLanguages.add(Turkish);
        mLanguages.add(Japanese);

    }
    public List<language> getLanguages() {
        return mLanguages;
    }

     public class language{

        private int img;
        private String name;
        private String abbr;
        boolean enable;

         public language(int img, String name,String _abbr, boolean enable) {
             this.img = img;
             this.name = name;
             this.abbr = _abbr;
             this.enable = enable;
         }

         public int getImg() {
             return img;
         }
         public String getName() {
             return name;
         }
         public boolean isEnable() {
             return enable;
         }
         public void setEnable(boolean enable) {
             this.enable = enable;
         }
         public String getAbbr() {
             return abbr;
         }
     }

}

