package com.herald.ezherald.treehole;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.BaseFrameActivity;
import com.herald.ezherald.R;
import com.herald.ezherald.account.AccountFragment;
import com.herald.ezherald.emptyclassroom.EmptyClassroomFragment;
import com.herald.ezherald.exercise.ExerciseFragment;

/**
 * Created by lenovo on 2014/10/26.
 * @author
 * @since 2014.10.26
 * 先声树洞的Activity
 *
 */
public class TreeholeActivity extends BaseFrameActivity {
    android.support.v4.app.Fragment mContentFrag;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
//        mContentFrag = new ExerciseFragment();
//        super.SetBaseFrameActivity(mContentFrag);
//        super.onCreate(savedInstanceState);
        mContentFrag = new TreeholeFragment();
        super.SetBaseFrameActivity(mContentFrag);
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        return super.onOptionsItemSelected(item);
    }


}
