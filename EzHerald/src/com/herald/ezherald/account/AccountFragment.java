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
	        //��XML�е�ListView����ΪItem������
		UserAccount IDCardAccount = Authenticate.getIDcardUser(getSherlockActivity());
		UserAccount tyxAccount = Authenticate.getTyxUser(getSherlockActivity());
		UserAccount libAccount = Authenticate.getLibUser(getSherlockActivity());
		String IDCardString = "[δ��¼] һ��ͨ�˺�";
		String tyxString = "[δ��¼] ����ϵ�˺�";
		String libString = "[δ��¼] ͼ����˺�";
		
		if(IDCardAccount!=null){
			IDCardString = "[�ѵ�¼] һ��ͨ�˺�";
		}
		if(tyxAccount!=null){
			tyxString = "[�ѵ�¼] ����ϵ�˺�";
		}
		if(libAccount!=null){
			libString = "[�ѵ�¼]ͼ����˺�";
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
