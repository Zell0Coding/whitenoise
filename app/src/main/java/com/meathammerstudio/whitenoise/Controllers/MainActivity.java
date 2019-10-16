package com.meathammerstudio.whitenoise.Controllers;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;

import com.meathammerstudio.whitenoise.Models.Sound;
import com.meathammerstudio.whitenoise.Models.SoundListiner;
import com.meathammerstudio.whitenoise.R;
import com.meathammerstudio.whitenoise.Utills.Manager;
import com.meathammerstudio.whitenoise.Utills.Utill;
import com.meathammerstudio.whitenoise.Services.musicServices;

public class MainActivity extends AppCompatActivity{

    private Manager mManager;
    private ActionBar mActionBar;

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
        mActionBar = getSupportActionBar();

        mActionBar.setDisplayShowTitleEnabled(false);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        PlayMusicFragment playMusicFragment = new PlayMusicFragment();
        fragmentTransaction.replace(R.id.container,playMusicFragment);
        fragmentTransaction.commit();

        Intent sound = new Intent(getApplicationContext(), musicServices.class );
        startService(sound);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.timer:
                addFragment(Utill.TIMER_FRAGMENT);
                break;
            case R.id.sleep:
                //
                break;
            case R.id.settings:
                //
                break;
            case android.R.id.home:
                addFragment(Utill.MUSIC_FRAGMENT);
                break;
        }

        return true;
    }

    private void addFragment(String fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (fragment){
            case Utill.MUSIC_FRAGMENT:

                PlayMusicFragment playMusicFragment = new PlayMusicFragment();
                fragmentTransaction.replace(R.id.container,playMusicFragment);
                mActionBar.setDisplayHomeAsUpEnabled(false);
                mActionBar.setDisplayShowHomeEnabled(false);
                mActionBar.setDisplayShowTitleEnabled(false);

                break;
            case Utill.TIMER_FRAGMENT:
                TimerFragment TimerFragment = new TimerFragment();
                fragmentTransaction.replace(R.id.container,TimerFragment);
                mActionBar.setDisplayHomeAsUpEnabled(true);
                mActionBar.setDisplayShowHomeEnabled(true);
                mActionBar.setDisplayShowTitleEnabled(true);
                mActionBar.setTitle(R.string.timer);

                break;
        }
        fragmentTransaction.commit();
    }

}
