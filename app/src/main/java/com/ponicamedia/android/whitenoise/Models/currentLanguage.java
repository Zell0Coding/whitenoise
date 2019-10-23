package com.ponicamedia.android.whitenoise.Models;

public class currentLanguage {

    private String language_abbr;

    public currentLanguage(){ }

    public currentLanguage(String language_abbr) {
        this.language_abbr = language_abbr;
    }

    public String getLanguage_abbr() {
        return language_abbr;
    }
    public void setLanguage_abbr(String language_abbr) {
        this.language_abbr = language_abbr;
    }
}
