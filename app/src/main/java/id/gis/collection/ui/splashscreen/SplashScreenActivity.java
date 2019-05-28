package id.gis.collection.ui.splashscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import id.gis.collection.R;
import id.gis.collection.ui.login.LoginActivity;
import id.gis.collection.ui.main.MainActivity;
import id.gis.collection.utils.AppConstant;
import id.gis.collection.utils.IqbalUtils;

public class SplashScreenActivity extends AppCompatActivity {

    private final static int MSG_CONTINUE = 1234;
    private final static int DELAY = 1500;
    private final String TAG = "SPLASH_SCREEN";
    private CustomHandler mCustomHandler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        setFullScreen();
        mCustomHandler = new CustomHandler(this);
        mCustomHandler.sendEmptyMessageDelayed(MSG_CONTINUE, DELAY);
    }

    private void setFullScreen() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    protected void onDestroy() {
        mCustomHandler.removeMessages(MSG_CONTINUE);
        super.onDestroy();
    }

    class CustomHandler extends Handler {
        private Activity splashActivity;

        public CustomHandler(Activity splashActivity) {
            this.splashActivity = splashActivity;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_CONTINUE:
                    _continue();
                    break;
            }
        }

        private void _continue() {
            Intent in = null;
            String userState = IqbalUtils.readPreference(getApplicationContext(), "isUserLogged", AppConstant.USER_NOTLOGGED);
            switch (userState) {
                case AppConstant.USER_LOGGED:
                    in = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(in);
                    finish();
                    break;
                case AppConstant.USER_NOTLOGGED:
                    in = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(in);
                    finish();
                    break;
            }
        }

        private void _continue(int id) {
            Intent in = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(in);
            finish();
        }
    }

}
