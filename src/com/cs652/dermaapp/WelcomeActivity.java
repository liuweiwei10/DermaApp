package com.cs652.dermaapp;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class WelcomeActivity extends ActionBarActivity {
	
	private Button btnStart;
	private Button btnHistory;
	private Button btnPictures;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);

		btnStart = (Button) findViewById(R.id.btn_start);
		btnHistory = (Button) findViewById(R.id.btn_history);
		btnPictures = (Button) findViewById(R.id.btn_pictures);
		
		btnStart.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(WelcomeActivity.this, DiagnoseActivity.class);
				startActivity(intent);
				WelcomeActivity.this.finish();
				
			}
		});
		
		btnHistory.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(),"Button Pressed, hasn't been implemented yet", 
						   Toast.LENGTH_SHORT).show();
			}
		});
		
		btnPictures.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(),"Button Pressed, hasn't been implemented yet", 
						   Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
