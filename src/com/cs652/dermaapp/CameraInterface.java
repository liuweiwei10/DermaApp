package com.cs652.dermaapp;

import java.io.IOException;  
import java.util.List;  
  




import com.cs652.dermaapp.CamParaUtil;  
import com.cs652.dermaapp.FileUtil;  
import com.cs652.dermaapp.ImageUtil;  
  




import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;  
import android.graphics.BitmapFactory;  
import android.graphics.PixelFormat;  
import android.hardware.Camera;  
import android.hardware.Camera.PictureCallback;  
import android.hardware.Camera.ShutterCallback;  
import android.hardware.Camera.Size;  
import android.util.Log;  
import android.view.SurfaceHolder;  
  
public class CameraInterface {  
    private static final String TAG = "DermaApp";  
    private Camera mCamera;  
    private Camera.Parameters mParams;  
    private boolean isPreviewing = false;  
    private float mPreviwRate = -1f;  
    private static CameraInterface mCameraInterface;  
    private String curPicName;
    private Context ctx;
  
    public interface CamOpenOverCallback{  
        public void cameraHasOpened();  
    }  
  
    private CameraInterface(){  
  
    }  
    
    public static synchronized CameraInterface getInstance(){  
        if(mCameraInterface == null){  
            mCameraInterface = new CameraInterface();  
        }  
        return mCameraInterface;  
    }  

    public void doOpenCamera(CamOpenOverCallback callback){  
        Log.i(TAG, "Camera open....");  
        mCamera = Camera.open();  
        Log.i(TAG, "Camera open over....");  
        callback.cameraHasOpened();  
    }  
 
    public void doStartPreview(SurfaceHolder holder, float previewRate){  
        Log.i(TAG, "doStartPreview...");  
        if(isPreviewing){  
            mCamera.stopPreview();  
            return;  
        }  
        if(mCamera != null){  
  
            mParams = mCamera.getParameters();  
            mParams.setPictureFormat(PixelFormat.JPEG);
            CamParaUtil.getInstance().printSupportPictureSize(mParams);  
            CamParaUtil.getInstance().printSupportPreviewSize(mParams);  

            Size pictureSize = CamParaUtil.getInstance().getPropPictureSize(  
                    mParams.getSupportedPictureSizes(),previewRate, 800);  
            mParams.setPictureSize(pictureSize.width, pictureSize.height);  
            Size previewSize = CamParaUtil.getInstance().getPropPreviewSize(  
                    mParams.getSupportedPreviewSizes(), previewRate, 800);  
            mParams.setPreviewSize(previewSize.width, previewSize.height);  
  
            mCamera.setDisplayOrientation(90);  
  
            CamParaUtil.getInstance().printSupportFocusMode(mParams);  
            List<String> focusModes = mParams.getSupportedFocusModes();  
            if(focusModes.contains("continuous-video")){  
                mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);  
            }  
            mCamera.setParameters(mParams);   
  
            try {  
                mCamera.setPreviewDisplay(holder);  
                mCamera.startPreview();
            } catch (IOException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
  
            isPreviewing = true;  
            mPreviwRate = previewRate;  
  
            mParams = mCamera.getParameters();   
        }  
    }  

    public void doStopCamera(){  
        if(null != mCamera)  
        {  
            mCamera.setPreviewCallback(null);  
            mCamera.stopPreview();   
            isPreviewing = false;   
            mPreviwRate = -1f;  
            mCamera.release();  
            mCamera = null;       
        }  
    }  

    public void doTakePicture(Context context){  
    	ctx = context;
        if(isPreviewing && (mCamera != null)){  
            mCamera.takePicture(mShutterCallback, null, mJpegPictureCallback);  
        }  
    }  
  
    ShutterCallback mShutterCallback = new ShutterCallback()   
    {  
        public void onShutter() {  
            // TODO Auto-generated method stub  
            Log.i(TAG, "myShutterCallback:onShutter...");  
        }  
    };  
    PictureCallback mRawCallback = new PictureCallback()   
    {  
  
        public void onPictureTaken(byte[] data, Camera camera) {  
            // TODO Auto-generated method stub  
            Log.i(TAG, "myRawCallback:onPictureTaken...");  
  
        }  
    };  
    
    PictureCallback mJpegPictureCallback = new PictureCallback()   
    {  
        public void onPictureTaken(byte[] data, Camera camera) {  
            // TODO Auto-generated method stub  
            Log.i(TAG, "myJpegCallback:onPictureTaken...");  
            Bitmap b = null;  
            if(null != data){  
                b = BitmapFactory.decodeByteArray(data, 0, data.length);
                mCamera.stopPreview();  
                isPreviewing = false;  
            }  
            if(null != b)  
            {  
                Bitmap rotaBitmap = ImageUtil.getRotateBitmap(b, 90.0f);  
                String path = FileUtil.saveBitmap(rotaBitmap);    
				SharedPreferences sp = ctx.getSharedPreferences("sp", 0);
				SharedPreferences.Editor editor = sp.edit();
				editor.putString("cur_pic_path", path);
				editor.commit();
            }  
            mCamera.startPreview();  
            isPreviewing = true; 
			Intent intent = new Intent();
			intent.setClass(ctx, PreviewPicture.class);
			ctx.startActivity(intent);
			((Activity)ctx).finish();
        }  
    };  
  
  
}  