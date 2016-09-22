package com.cs652.dermaapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DiagnoseActivity extends ActionBarActivity { 	
	private Button btnTakePic;
	private Button btnSelectPic;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diagnose);
		
		btnTakePic = (Button) findViewById(R.id.btn_take_pic);
		btnSelectPic = (Button) findViewById(R.id.btn_select_pic);
		
		
		btnTakePic.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(DiagnoseActivity.this, CameraActivity.class);
				startActivity(intent);
				DiagnoseActivity.this.finish();
				
			}
		});
		
		btnSelectPic.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(),"Button Pressed, hasn't been implemented yet", 
						   Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent();
			intent.setClass(DiagnoseActivity.this, WelcomeActivity.class);
			startActivity(intent);
			DiagnoseActivity.this.finish();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
		

}

