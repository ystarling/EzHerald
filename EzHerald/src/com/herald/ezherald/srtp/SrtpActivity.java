package com.herald.ezherald.srtp;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.BaseFrameActivity;
/**
 * Created by LJ on 2014/10/29.
 */
public class SrtpActivity extends BaseFrameActivity{
    Fragment mContentFrag;
    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub


        mContentFrag = new SrtpFragment();
        super.SetBaseFrameActivity(mContentFrag);
        super.onCreate(savedInstanceState);


    }

    /* (non-Javadoc)
     * @see com.actionbarsherlock.app.SherlockActivity#onCreateOptionsMenu(com.actionbarsherlock.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    /* (non-Javadoc)
     * @see com.actionbarsherlock.app.SherlockActivity#onOptionsItemSelected(com.actionbarsherlock.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
		/*
		 * 上侧Title位置的按钮点击相应
		 */
        return super.onOptionsItemSelected(item);
    }
}