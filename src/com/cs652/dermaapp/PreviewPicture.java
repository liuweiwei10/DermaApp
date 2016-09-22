package com.cs652.dermaapp;

import java.io.BufferedReader;
import java.io.File;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class PreviewPicture  extends Activity{
	private final String TAG ="DermaApp";
	private ImageView imagePhoto;
	private Button imbOK;
	private Button imbDiscard;
	private File imageFile;
	private ProgressDialog  pdialog;
	private String url = Configure.getURL();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preview_picture);
		
        imagePhoto = (ImageView) findViewById(R.id.im_photo);
        imbOK = (Button) findViewById(R.id.btn_ok);
        imbDiscard = (Button) findViewById(R.id.btn_discard);
        
		SharedPreferences sp = getSharedPreferences("sp", 0);
		String photoPath = sp.getString("cur_pic_path", "");	
		Log.d(TAG,"current picture:"  + photoPath);
		
	   imageFile = new File (photoPath);
		if (imageFile.exists()){
		     Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
		     BitmapDrawable drawable = new BitmapDrawable(this.getResources(), bitmap);
		     imagePhoto.setImageDrawable(drawable);
		}
		
		imbDiscard.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {				
				imageFile.delete();
				SharedPreferences sp = getSharedPreferences("sp", 0);
				Editor editor = sp.edit();
				editor.putString("cur_pic_path", "");
				editor.commit();
				Intent intent = new Intent();
				intent.setClass(PreviewPicture.this, CameraActivity.class);
				startActivity(intent);
				PreviewPicture.this.finish();
			}
		});
		
		imbOK.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {				
				new HttpAsyncTask().execute(url);
			}
		});
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			imageFile.delete();
			SharedPreferences sp = getSharedPreferences("sp", 0);
			Editor editor = sp.edit();
			editor.putString("cur_pic_path", "");
			editor.commit();
			Intent intent = new Intent();
			intent.setClass(PreviewPicture.this, CameraActivity.class);
			startActivity(intent);
			PreviewPicture.this.finish();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	public static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;
		inputStream.close();
		return result;

	}
	public String sendFileToServer(File file, String url) {
		InputStream inputStream = null;
		String result = "";
		try {
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost(url);
		    InputStreamEntity reqEntity = new InputStreamEntity(
		            new FileInputStream(file), -1);
		    reqEntity.setContentType("binary/octet-stream");
		    reqEntity.setChunked(true); // Send in multiple parts if needed
		    httppost.setEntity(reqEntity);
		    HttpResponse httpResponse = httpclient.execute(httppost);
		    if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK)
				Log.v(TAG, "connect successfully");
			else
				Log.v(TAG, "status code" + httpResponse.getStatusLine().getStatusCode());
			inputStream = httpResponse.getEntity().getContent();
			if (inputStream != null) {
				result = convertInputStreamToString(inputStream);
				Log.d(TAG, "httpResponse:" + result);
			} 
			else{
				Log.d(TAG, "httpResponse is null");
			}
			inputStream.close();

		} catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    } catch (ClientProtocolException e) {
	        e.printStackTrace();
	    } catch (ConnectTimeoutException e) {
	        Log.e("CONN TIMEOUT", e.toString());
	    } catch (SocketTimeoutException e) {
	        Log.e("SOCK TIMEOUT", e.toString());
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (Exception e) {
	        Log.e("OTHER EXCEPTIONS", e.toString());
	    }
		return result;
	}
	
	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {
		      return(sendFileToServer(imageFile, urls[0]));			   
			//return "success"; //enable this to debug
		}

		protected void onProgressUpdate(Void... progress) {
			  pdialog = ProgressDialog.show(PreviewPicture.this, null, "Diagnosing,please wait...", true, false);  
			
		}
	
		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			 Log.d(TAG, "result:" + result);
			 if(result.trim().equals(""))
				 {
				 if(pdialog != null) 
				      pdialog.dismiss();
				 Toast.makeText(getApplicationContext(), "Fail to connect to the server.",
						  Toast.LENGTH_SHORT).show();
				 }
			 else
			 {
				    // Do something with the result
					Intent intent = new Intent();
					intent.setClass(PreviewPicture.this, ResultActivity.class);
					startActivity(intent);
					PreviewPicture.this.finish();
					
			 }
		}
	}
}

