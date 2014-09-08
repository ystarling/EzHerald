package com.herald.ezherald.settingframe;

import android.content.Context;
import android.preference.Preference;
import android.preference.SwitchPreference;
import android.util.AttributeSet;

import com.herald.ezherald.wifi.WifiFloatWindowManager;
import com.herald.ezherald.wifi.WifiReceiver;

/**
 * Created by xie on 9/4/2014.
 */
public class WifiFloatWindowPreference extends SwitchPreference implements Preference.OnPreferenceChangeListener{


    public WifiFloatWindowPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnPreferenceChangeListener(this);
    }
    public WifiFloatWindowPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOnPreferenceChangeListener(this);
    }
    public WifiFloatWindowPreference(Context context) {
        super(context);
        setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if( (Boolean)newValue == false){
            WifiFloatWindowManager.removeWindow(getContext());
        }else{
            new WifiReceiver().onReceive(getContext(),null);
        }
        return true;
    }
}
