package com.herald.ezherald.settingframe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.herald.ezherald.R;

/**
 * Created by xie on 6/28/2014.
 */
public class SettingsActivity extends PreferenceActivity {
    public static final String SHOW_ACTIVITY_SELECTION = "show_activity_selectin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        addPreferencesFromResource(R.xml.preferences);
        PreferenceManager.setDefaultValues(this,R.xml.default_preference, false);
        SharedPreferences pref = getPreferenceManager().getSharedPreferences();
        pref.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                Log.v("", key);
            }
        });
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        boolean showActivitySelection = intent.getBooleanExtra(SHOW_ACTIVITY_SELECTION,false);
        if(showActivitySelection){
            PreferenceScreen preferenceScreen = (PreferenceScreen) findPreference("pref_screen");
            int pos = findPreference("activity").getOrder();
            preferenceScreen.onItemClick(null,null,pos,0);


        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
