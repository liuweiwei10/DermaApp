package com.cs652.dermaapp;

import android.content.Context;  
import android.graphics.Point;  
import android.util.DisplayMetrics;  
import android.util.Log;  
  
public class DisplayUtil {  
    private static final String TAG = "DermaApp";  
    /** 
     * convert dip to px 
     * @param context 
     * @param dipValue 
     * @return 
     */  
    public static int dip2px(Context context, float dipValue){              
        final float scale = context.getResources().getDisplayMetrics().density;                   
        return (int)(dipValue * scale + 0.5f);           
    }       
      
    /** 
     * convert px to dip
     * @param context 
     * @param pxValue 
     * @return 
     */  
    public static int px2dip(Context context, float pxValue){                  
        final float scale = context.getResources().getDisplayMetrics().density;                   
        return (int)(pxValue / scale + 0.5f);           
    }   
      
    /** 
     * get screen height and width
     * @param context 
     * @return 
     */  
    public static Point getScreenMetrics(Context context){  
        DisplayMetrics dm =context.getResources().getDisplayMetrics();  
        int w_screen = dm.widthPixels;  
        int h_screen = dm.heightPixels;  
        Log.i(TAG, "Screen---Width = " + w_screen + " Height = " + h_screen + " densityDpi = " + dm.densityDpi);  
        return new Point(w_screen, h_screen);  
          
    }  
      
    /** 
     * get ratio of the screen height and width
     * @param context 
     * @return 
     */  
    public static float getScreenRate(Context context){  
        Point P = getScreenMetrics(context);  
        float H = P.y;  
        float W = P.x;  
        return (H/W);  
    }  
}  