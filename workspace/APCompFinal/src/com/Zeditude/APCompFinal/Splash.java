package com.Zeditude.APCompFinal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * @author Al-Zahir Rahemtulla
 *
 */
public class Splash extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		new Handler().postDelayed(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent mainIntent = new Intent(Splash.this, Main.class);
				Splash.this.startActivity(mainIntent);
				Splash.this.finish();
			}
			
		}, 2000);
	}
}