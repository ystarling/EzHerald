/**
 * 
 */
package com.herald.ezherald.library;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;

/**
 * @author BIG_SEA
 *	Tab—°‘Ò
 */
class libraryFragmentTabSelect implements ActionBar.TabListener{

	/**
	 * 
	 */

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		Fragment libr_frg;
		if(tab.getPosition()==0){
			libr_frg=new LibraryFragment();
		}
		else{
			libr_frg=new libraryFragmentNews();
		}
		ft.replace(android.R.id.content, libr_frg);
		
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
