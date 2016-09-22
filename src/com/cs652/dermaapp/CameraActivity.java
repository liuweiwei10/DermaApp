package com.cs652.dermaapp;

import com.cs652.dermaapp.CameraInterface;
import com.cs652.dermaapp.CameraInterface.CamOpenOverCallback;
import com.cs652.dermaapp.CameraSurfaceView;
import com.cs652.dermaapp.DisplayUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;


public class CameraActivity extends Activity implements CamOpenOverCallback{
	private final String TAG = "DermaApp";
	 CameraSurfaceView surfaceView = null;  
	 ImageButton btnTakePic;  
	 float previewRate = -1f;  
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		Thread openThread = new Thread(){  
            @Override  
            public void run() {  
                // TODO Auto-generated method stub  
                CameraInterface.getInstance().doOpenCamera(CameraActivity.this);  
            }             
        };  
        openThread.start();  
        setContentView(R.layout.camera);  
        initUI();  
        initViewParams();  
          
        btnTakePic.setOnClickListener(new BtnListeners());  
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent();
			intent.setClass(CameraActivity.this, DiagnoseActivity.class);
			startActivity(intent);
			CameraActivity.this.finish();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

	 private void initUI(){  
	        surfaceView = (CameraSurfaceView)findViewById(R.id.camera_surfaceview);  
	        btnTakePic = (ImageButton)findViewById(R.id.btn_take_pic);  
	    }  
	    private void initViewParams(){  
	        LayoutParams params = surfaceView.getLayoutParams();  
	        Point p = DisplayUtil.getScreenMetrics(this);  
	        params.width = p.x;  
	        params.height = p.y;  
	        previewRate = DisplayUtil.getScreenRate(this);  
	        surfaceView.setLayoutParams(params);  
	  
	        //set ImageButton size
	        LayoutParams p2 = btnTakePic.getLayoutParams();  
	        p2.width = DisplayUtil.dip2px(this, 80);  
	        p2.height = DisplayUtil.dip2px(this, 80);;        
	        btnTakePic.setLayoutParams(p2);   
	  
	    }  
	  
	    @Override  
	    public void cameraHasOpened() {  
	        // TODO Auto-generated method stub  
	        SurfaceHolder holder = surfaceView.getSurfaceHolder();  
	        CameraInterface.getInstance().doStartPreview(holder, previewRate);  
	    }  
	    private class BtnListeners implements OnClickListener{  
	  
	        @Override  
	        public void onClick(View v) {  
	            // TODO Auto-generated method stub  
	            switch(v.getId()){  
	            case R.id.btn_take_pic:  
	                CameraInterface.getInstance().doTakePicture(CameraActivity.this);  
	/*    			Intent intent = new Intent();
					intent.setClass(CameraActivity.this, PreviewPicture.class);
					startActivity(intent);
					CameraActivity.this.finish();*/
	                break;  
	            default:break;  
	            }  
	        }  
	    }   
}
