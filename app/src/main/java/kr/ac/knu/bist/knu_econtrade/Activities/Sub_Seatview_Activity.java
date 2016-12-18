package kr.ac.knu.bist.knu_econtrade.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import kr.ac.knu.bist.knu_econtrade.R;

public class Sub_Seatview_Activity extends AppCompatActivity {

    private WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_seatview);
        webview = (WebView)findViewById(R.id.seat_webview);
        Intent intent = getIntent();
        String url = intent.getExtras().getString("url");
        Log.e("url",url);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setVerticalScrollBarEnabled(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setSupportZoom(true);
        webview.setInitialScale(150);
        webview.loadUrl(url);

    }
}
