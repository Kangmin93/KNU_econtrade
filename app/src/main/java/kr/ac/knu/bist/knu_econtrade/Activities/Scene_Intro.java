package kr.ac.knu.bist.knu_econtrade.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import kr.ac.knu.bist.knu_econtrade.R;

/**
 * Created by neu on 16. 8. 29.
 *
 * Scene_Intro
 * First activity when application has been initialized.
 * Next scene activity will be Scene_Login after being loaded all of components.
 *
 */
public class Scene_Intro extends Activity {

    Handler Handler_Local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*no title bar, and status bar*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_intro_intro);

        Handler_Local = new Handler(); //딜래이를 주기 위해 핸들러 생성
        Handler_Local.postDelayed(millirun, 2000);
    }

    Runnable millirun = new Runnable() {
        @Override
        public void run() {
            Intent Local_Intent = new Intent(Scene_Intro.this, Scene_Main.class);
            startActivity(Local_Intent); finish();

            // animation effect
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Handler_Local.removeCallbacks(millirun);
    }
}
