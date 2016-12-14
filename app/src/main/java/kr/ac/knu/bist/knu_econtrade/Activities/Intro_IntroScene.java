package kr.ac.knu.bist.knu_econtrade.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import kr.ac.knu.bist.knu_econtrade.R;

/**
 * Created by Vertx on
 */
//test

public class Intro_IntroScene extends Activity {

    Handler Handler_Local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        int Local_Flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setFlags(Local_Flag, Local_Flag);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_intro_intro);

        Handler_Local = new Handler();
        Handler_Local.postDelayed(Method_Millirun, 2 * 1000);

        LinearLayout Intro_Layout = (LinearLayout) findViewById(R.id.intro_layout);
        Intro_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler_Local.removeCallbacks(Method_Millirun);
                ProceedIntent();
            }
        });
    }

    Runnable Method_Millirun = new Runnable() {
        @Override
        public void run() {
            ProceedIntent();

            // Animation Effect
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    };

    private void ProceedIntent() {
        Intent Local_Intent = new Intent(Intro_IntroScene.this, Intro_LoginScene.class);
        startActivity(Local_Intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Handler_Local.removeCallbacks(Method_Millirun);
    }
}
