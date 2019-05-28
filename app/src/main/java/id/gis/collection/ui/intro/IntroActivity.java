package id.gis.collection.ui.intro;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

import id.gis.collection.R;
import id.gis.collection.ui.login.LoginActivity;
import id.gis.collection.utils.CustomSlide;

/**
 * Created by dell on 19/08/18.
 */

public class IntroActivity extends AppIntro2 {
    private final String PREFKEY_FIRST_START = "id.gis.collection.PREFKEY_FIRST_START";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(CustomSlide.newInstance(R.layout.slide1));
        addSlide(CustomSlide.newInstance(R.layout.slide2));
        addSlide(CustomSlide.newInstance(R.layout.slide3));

        showSkipButton(false);
        showStatusBar(false);
        setSlideOverAnimation();
        setIndicatorColor(getResources().getColor(R.color.accentPressed), getResources().getColor(R.color.colorAccent));
//        setBarColor(getResources().getColor(R.color.colorAccent));

        // Here we load a string array with a camera permission, and tell the library to request permissions on slide 2
        askForPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.WAKE_LOCK, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 2);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putBoolean(PREFKEY_FIRST_START, false)
                .apply();
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putBoolean(PREFKEY_FIRST_START, false)
                .apply();
        finish();
    }
}
