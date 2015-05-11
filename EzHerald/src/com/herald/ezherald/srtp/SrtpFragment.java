package com.herald.ezherald.srtp;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragment;
/**
 * Created by Lj on 2014/10/29.
 */
public class SrtpFragment  extends SherlockFragment{
    //public static Score score;
    FragmentA fragA=new FragmentA();
    FragmentB fragB=new FragmentB();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle saved){
        //super.onCreate(savedInstanceState);

        ActionBar bar = getSherlockActivity(). getSupportActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.setTitle("Srtp");
        ActionBar.Tab tab1 = bar.newTab();
        ActionBar.Tab tab2 = bar.newTab();

        tab1.setText("得分");
        tab2.setText("项目");

        tab1.setTabListener(new MyTabListener());
        tab2.setTabListener(new MyTabListener());

        bar.addTab(tab1);
        bar.addTab(tab2);
       // score=new Score(getActivity(),fragA);

//        return inflater.inflate(R.layout.srtp_activity_main,group,false);
        return null;
    }

    private class MyTabListener implements ActionBar.TabListener
    {

        @Override
        public void onTabSelected(Tab tab, FragmentTransaction ft) {

            Fragment frag;
            switch(tab.getPosition()){
                case 0:
                    frag = new FragmentA();
                    break;
                case 1:
                    frag = new FragmentB();
                    break;
                default:
                    Log.w("error","no such a tag in srtp");
                    return;
            }
            ft.replace(android.R.id.content, frag);
        }

        @Override
        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTabReselected(Tab tab, FragmentTransaction ft) {
            // TODO Auto-generated method stub

        }
    }
}