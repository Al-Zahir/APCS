package com.Zeditude.APCompFinal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Menu extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		
		Button singP = (Button)findViewById(R.id.singlePlayer);
		Button multP = (Button)findViewById(R.id.multiPlayer);
		
		final Bundle basket = new Bundle();
		final TextView loading = (TextView)findViewById(R.id.loadingMenu);
		
		singP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	loading.setVisibility(TextView.VISIBLE);
            	basket.putBoolean("AI", true);
            	startActivity(new Intent(Menu.this, Main.class).putExtras(basket));
            }
        });
		
		multP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	loading.setVisibility(TextView.VISIBLE);
            	basket.putBoolean("AI", false);
            	startActivity(new Intent(Menu.this, Main.class).putExtras(basket));
            }
        });
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

}
