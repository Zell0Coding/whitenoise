package com.meathammerstudio.whitenoise.Controllers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;

import com.meathammerstudio.whitenoise.Models.Sound;
import com.meathammerstudio.whitenoise.Models.SoundListiner;
import com.meathammerstudio.whitenoise.R;
import com.meathammerstudio.whitenoise.Utills.Manager;
import com.meathammerstudio.whitenoise.Utills.Utill;
import com.meathammerstudio.whitenoise.Services.musicServices;

public class MainActivity extends AppCompatActivity{

    private Manager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mManager = Manager.getInstance();
        mManager.getSoundListiner().setContext(getApplicationContext());


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        mManager.setWidth(width);


        Toolbar mActionBarToolbar =  findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);

        addFragment(Utill.MUSIC_FRAGMENT);


        Intent sound = new Intent(getApplicationContext(), musicServices.class );
        startService(sound);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    private void addFragment(String fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (fragment){
            case Utill.MUSIC_FRAGMENT:

                PlayMusicFragment playMusicFragment = new PlayMusicFragment();
                fragmentTransaction.replace(R.id.container,playMusicFragment);
                break;
        }
        fragmentTransaction.commit();
    }

}
