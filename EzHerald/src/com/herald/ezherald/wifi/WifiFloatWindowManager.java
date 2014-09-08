package com.herald.ezherald.wifi;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
/**
 * Created by xie on 7/2/2014.
 */
public class WifiFloatWindowManager {
    public static WifiFloatWindow getWindowView() {
        return windowView;
    }

    private static WifiFloatWindow windowView;
    private static LayoutParams windowLayoutParams;
    private static WindowManager mWindowManager;
    public static WifiFloatWindow getWindow(Context context){
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (windowView == null) {
            windowView = new WifiFloatWindow(context);
            if(windowLayoutParams == null) {
                windowLayoutParams = new LayoutParams();
                windowLayoutParams.type = LayoutParams.TYPE_PHONE;
                windowLayoutParams.format = PixelFormat.RGBA_8888;
                windowLayoutParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL|LayoutParams.FLAG_NOT_FOCUSABLE;
                windowLayoutParams.gravity = Gravity.LEFT|Gravity.TOP;
                windowLayoutParams.width = WifiFloatWindow.width;
                windowLayoutParams.height = WifiFloatWindow.height;
                windowLayoutParams.x = screenWidth;
                windowLayoutParams.y = (int)(screenHeight*0.6);
            }
            windowView.setParams(windowLayoutParams);
            windowManager.addView(windowView, windowLayoutParams);
        }
        return windowView;
    }

    public static void removeWindow(Context context) {
        if(windowView !=null){
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(windowView);
            windowView = null;
        }
    }

    private static WindowManager getWindowManager(Context context) {
        if(mWindowManager == null ){
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }


}
