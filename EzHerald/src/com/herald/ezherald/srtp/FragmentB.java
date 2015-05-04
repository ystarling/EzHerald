package com.herald.ezherald.srtp;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.herald.ezherald.R;
import com.herald.ezherald.account.Authenticate;
import com.herald.ezherald.account.TyxAccountActivity;
import com.herald.ezherald.account.UserAccount;

import java.util.ArrayList;

/**
 * Created by Administrator on 2014/10/29.
 */
public class FragmentB extends  Fragment{
    public Score score;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle saved){
        //show();
        return inflater.inflate(R.layout.srtp_fragment_b, group,false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        score = new Score(getActivity(),this);
        show();
        //tableLayout=( LinearLayout)getActivity().findViewById(R.id.layout_main);

    }

    private void Update(){

    }

    public void show(){
        if(score.getScore()==0){
            onFailed();
        }
        else if(score.getScore()!=0){
            onSuccess();
        }
    }

    public  void onSuccess(){
        Activity act = getActivity();
        ArrayList project =score.getProject();
        LinearLayout tableLayout=( LinearLayout)act.findViewById(R.id.layout_main);
        int k = tableLayout.getChildCount();

        //tableLayout.removeAllViews();
        int m = tableLayout.getChildCount();
        for(int t=0;t<m;t++) {
            tableLayout.removeViewAt(t);
        }

        for(int i=0;i<project.size();i=i+3){
            LayoutInflater inflater = LayoutInflater.from(act);
            View convertView = inflater.inflate(R.layout.srtp_template,null);
            LinearLayout tableLayout1 = (LinearLayout)convertView.findViewById(R.id.Table);
            TextView scoreView = (TextView)tableLayout1.findViewById(R.id.score);
            TextView projectView =(TextView)tableLayout1.findViewById(R.id.project);
            scoreView.setText(project.get(i).toString());
            projectView.setText(project.get(i+1).toString());
            tableLayout.addView(tableLayout1);
        }
        Log.w("number",String.valueOf(tableLayout.getChildCount()));
       // tableLayout.removeViewAt(0);
    }

    public void onFailed(){
        Activity act = getActivity();
        if(act!=null){
            Toast.makeText(act,"请先更新哦~",Toast.LENGTH_LONG).show();
        }
    }

}