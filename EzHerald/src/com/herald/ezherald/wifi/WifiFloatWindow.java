package com.herald.ezherald.wifi;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private boolean isBigWindowOpen;
    private int statusBarHeight;
    private View view,small,big;


    public WifiFloatWindow(Context context) {
        super(context);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.wifi_float_window_small,this);
        view = findViewById(R.id.small_window_layout);
        width = view.getLayoutParams().width;
        height = view.getLayoutParams().height;
        small = findViewById(R.id.small);
        big=findViewById(R.id.big);

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
                    toggleSize();
                }
                break;
            default:
                break;


        }
        return true;
    }

    //切换到已经登陆的状态
    public void changeToLoginMode(){
        small.setBackgroundColor(Color.GREEN);
        big.setBackgroundColor(Color.GREEN);
        ((TextView)big.findViewById(R.id.info)).setText("SEU-WLAN已登录");
    }
    //切换到未登录的状态
    public void changeToNotLoginMode(){
        small.setBackgroundColor(Color.BLUE);
        big.setBackgroundColor(Color.BLUE);
        ((TextView)big.findViewById(R.id.info)).setText("SEU-WLAN未登录");
    }
    //切换到登录信息错误状态
    public void changeToLoginFailedMode(String errorMessage) {
        small.setBackgroundColor(Color.RED);
        big.setBackgroundColor(Color.RED);
        ((TextView)big.findViewById(R.id.info)).setText(errorMessage);
    }
    public void toggleSize() {
        if(isBigWindowOpen) {
            isBigWindowOpen = false;
            big.setVisibility(GONE);
            small.setVisibility(VISIBLE);

        } else {
            isBigWindowOpen = true;
            big.setVisibility(VISIBLE);
            small.setVisibility(GONE);
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
