package com.herald.ezherald.wifi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.herald.ezherald.R;

import java.lang.reflect.Field;

/**
 * Created by xie on 7/2/2014.
 */
public class WifiFloatWindow extends LinearLayout {
    public static int width;
    public static int height;
    private WindowManager.LayoutParams params;
    private static WindowManager windowManager;
    private float xInView,yInView,xDownScreen,yDownScreen,xInScreen, yInScreen;
    private boolean isOpen;
    private int statusBarHeight;

    public WifiFloatWindow(Context context) {
        super(context);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.wifi_float_window_small,this);
        View view = findViewById(R.id.small_window_layout);
        width = view.getLayoutParams().width;
        height = view.getLayoutParams().height;

    }

    public void setParams(WindowManager.LayoutParams params) {
        this.params = params;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xInView = event.getX();
                yInView = event.getY();
                xInScreen=xDownScreen = event.getRawX();
                yInScreen =yDownScreen = event.getRawY()-getStatusBarHeight();
                break;
            case MotionEvent.ACTION_MOVE:
                xInScreen = event.getRawX();
                yInScreen = event.getRawY()-getStatusBarHeight();
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:
                if (xDownScreen == xInScreen && yDownScreen == yInScreen) {
                    toggleInfo();
                }
                break;
            default:
                break;


        }
        return true;
    }

    public void changeToLoginMode(){
        View small = findViewById(R.id.small),big=findViewById(R.id.big);
        small.setVisibility(GONE);
        big.setVisibility(VISIBLE);
    }
    public void changeToNotloginMode(){
        View small = findViewById(R.id.small),big=findViewById(R.id.big);
        small.setVisibility(VISIBLE);
        big.setVisibility(GONE);
    }
    public void toggleInfo() {
        if(isOpen) {
          isOpen = false;
          changeToNotloginMode();

        } else {
          isOpen = true;
          changeToLoginMode();
        }
    }

    private void updateViewPosition() {
        params.x = (int)(xInScreen-xInView);
        params.y = (int)(yInScreen-yInView);
        windowManager.updateViewLayout(this,params);
    }

    private int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }
}
