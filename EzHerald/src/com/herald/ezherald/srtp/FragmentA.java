package com.herald.ezherald.srtp;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.herald.ezherald.R;
import com.herald.ezherald.account.Authenticate;
import com.herald.ezherald.account.TyxAccountActivity;
import com.herald.ezherald.account.UserAccount;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 2014/10/29.
 */
public class FragmentA extends Fragment{

    private  Button updateButton;
    private TextView scoreText;
    private TextView UpdattimeText;
    private Score score;
    private UserAccount user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle saved) {
        return inflater.inflate(R.layout.srtp_fragment_a, group, false);
    }
        @Override
        public void onActivityCreated(Bundle savedInstanceState){
            super.onActivityCreated(savedInstanceState);

            user = Authenticate.getTyxUser(getActivity());
            if (null == user) {
                Intent login = new Intent();
                login.setClass(getActivity(), TyxAccountActivity.class);
                startActivity(login);
            }
            else{
                updateButton=(Button)getActivity().findViewById(R.id.srtp_update_button);
                scoreText=(TextView)getActivity().findViewById(R.id.srtp_score);
                UpdattimeText=(TextView)getActivity().findViewById(R.id.srtp_UpdateTime);
                score=new Score(getActivity(),this);
            }
            if(score.isSet()){
                show();
            }
            else
            {
                Activity act = getActivity();
                if(act!=null){
                    Toast.makeText(act, "还没有数据呦", Toast.LENGTH_LONG).show();
                }
            }
            updateButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateButton.setText("正在更新");
                    Update();
                }
            });

        }

    private void Update(){
        score.update(user);
    }

    public void show(){
            if(Score.DEFAULT_SCORE == score.getScore()){
                scoreText.setText("还没有数据哦");
            }
        else{
               scoreText.setText(score.getScore());
            }
        if(score.getUpdateTime()!=Score.DEFAULT_UPDATETIME){
            UpdattimeText.setText(score.getUpdateTime());
        }
        else{
            UpdattimeText.setText("未等新哦");
        }
    }

    public  void onSuccess(){
        updateButton.setText("更新");
        Activity act = getActivity();
        if(act != null){
            Toast.makeText(act, "更新成功", Toast.LENGTH_LONG).show();
            show();
        }
    }

    public void onFailed(){
        updateButton.setText("更新");
        Activity act = getActivity();
        if(act != null){
            Toast.makeText(act, "更新失败", Toast.LENGTH_LONG).show();
            show();
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        //TODO check login
    }

}
