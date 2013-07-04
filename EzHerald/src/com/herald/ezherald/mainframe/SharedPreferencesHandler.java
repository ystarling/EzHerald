package com.herald.ezherald.mainframe;

import java.util.HashSet;
import java.util.Set;

import android.content.SharedPreferences;

/**
 * 解决Android 3.0以下版本Preferences中没有getStringSet这个方法的问题
 * @author BorisHe
 *
 */
public class SharedPreferencesHandler {
	final static String regularEx = "|";

	public static Set<String> getStringSet(SharedPreferences prefs, String key,
			Set<String> defValues) {
		String str = prefs.getString(key, "");
		if (!str.isEmpty()) {
			String[] values = str.split(regularEx);
			if (defValues == null) {
				defValues = new HashSet<String>();
				for (String value : values) {
					if (!value.isEmpty()) {
						defValues.add(value);
					}
				}
			}
		}
		return defValues;
	}

	public static SharedPreferences.Editor putStringSet(
			SharedPreferences.Editor ed, String key, Set<String> values) {
		String str = "";
		if (values != null | !values.isEmpty()) {
			Object[] objects = values.toArray();
			for (Object obj : objects) {
				str += obj.toString();
				str += regularEx;
			}
			ed.putString(key, str);
		}
		return ed;
	}
}
