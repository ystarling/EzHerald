package com.herald.ezherald.account;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.R;

public class AccountFragment extends SherlockFragment{
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle saved){ 	
		
		View view = inflater.inflate(R.layout.account_activity_main, group, false);
	        //绑定XML中的ListView，作为Item的容器
		UserAccount IDCardAccount = Authenticate.getIDcardUser(getSherlockActivity());
		UserAccount tyxAccount = Authenticate.getTyxUser(getSherlockActivity());
		UserAccount libAccount = Authenticate.getLibUser(getSherlockActivity());
		String IDCardString = "[未登录] 一卡通账号";
		String tyxString = "[未登录] 体育系账号";
		String libString = "[未登录] 图书馆账号";
		
		if(IDCardAccount!=null){
			IDCardString = "[已登录] 一卡通账号";
		}
		if(tyxAccount!=null){
			tyxString = "[已登录] 体育系账号";
		}
		if(libAccount!=null){
			libString = "[已登录]图书馆账号";
		}
		
	    String lv_arr[]={
				IDCardString,
				tyxString,
				libString,
				};
		
	        ListView list = (ListView) view.findViewById(R.id.MyListView);
	        list.setAdapter(new ArrayAdapter<String>(getSherlockActivity(),R.layout.account_activity_main_listview , lv_arr));
	        
	        
	        
	        
	        list.setOnItemClickListener(new OnItemClickListener() {
	        	@Override
	            public void onItemClick(AdapterView<?> parent, View view,
	                int position, long id) {
	                
	                switch (position) {
	                  case 0:
	                   Intent newActivity0 = new Intent(getSherlockActivity(),IDCardAccountActivity.class);     
	                   startActivity(newActivity0);
	                  break;
	                  case 1:
	                   Intent newActivity1 = new Intent(getSherlockActivity(),TyxAccountActivity.class);     
	                   startActivity(newActivity1);
	                  break;
	                  case 2:
		                   Intent newActivity2 = new Intent(getSherlockActivity(),LibAccountActivity.class);     
		                   startActivity(newActivity2);
		                  break;
	                  default:
	                    // Nothing do!
	                }

	            }
	          });
			return view;
		
	}
}
