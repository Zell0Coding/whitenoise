package com.meathammerstudio.whitenoise.Controllers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.meathammerstudio.whitenoise.R;
import com.meathammerstudio.whitenoise.Utills.Utill;
import com.meathammerstudio.whitenoise.Services.musicServices;

public class MainActivity extends AppCompatActivity implements PlayMusicFragment.playsound{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mActionBarToolbar =  findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);

        addFragment(Utill.MUSIC_FRAGMENT);

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
                fragmentTransaction.addToBackStack("music");
                break;
        }
        fragmentTransaction.commit();
    }


    @Override
    public void play(String name) {
        Intent sound = new Intent(getApplicationContext(), musicServices.class );
        sound.putExtra("name",name);
        startService(sound);
    }
}
