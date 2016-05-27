package com.eserviss.passenger.main;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.egnyt.eserviss.R;

public class BounceLogoScreen extends Activity {
	WebView image_bounce;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bounce_logo_screen);
		image_bounce=(WebView)findViewById(R.id.imageWebView);
		
		//----------------For to bounce---------------------------------
		image_bounce.loadUrl("file:///android_asset/ESER.gif"); 
		
		//----------------Exit bounce-----------------------------------
		WebSettings settings = image_bounce.getSettings();
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		
		//-------------Thread--------------------------------------------
		Thread timerThread = new Thread(){
            public void run() {
                try {
                    sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                	
                	Intent i = new Intent(getApplicationContext(), ResideMenuActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        timerThread.start();
	}
	//------------------Thread Exit--------------------------------------------
	
}
