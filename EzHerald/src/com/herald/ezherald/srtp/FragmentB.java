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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle saved){
        return inflater.inflate(R.layout.srtp_fragment_b, group,false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        show();
    }

    private void Update(){

    }

    public void show(){
        if(FragmentA.score.getScore()==0){
            onFailed();
        }
        else if(FragmentA.score.getScore()!=0){
            onSuccess();
        }
    }

    public  void onSuccess(){
        Activity act = getActivity();
        ArrayList project = FragmentA.score.getProject();
        TableLayout tableLayout=(TableLayout)getActivity().findViewById(R.id.layout);
        for(int i=0;i<project.size();i=i+3){
            TableRow tableRow=(TableRow)getActivity().findViewById(R.id.table);
            TextView score = (TextView)tableRow.findViewById(R.id.score1);
            TextView type = (TextView)tableRow.findViewById(R.id.text1);
            score.setText(project.get(i).toString());
            type.setText(project.get(i+1).toString());
//            tableLayout.addView(tableRow);
        }
    }

    public void onFailed(){
        Activity act = getActivity();
        if(act!=null){
            Toast.makeText(act,"更新失败",Toast.LENGTH_LONG).show();
        }
    }

}
