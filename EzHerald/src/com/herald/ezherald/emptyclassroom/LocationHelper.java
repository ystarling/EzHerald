package com.herald.ezherald.emptyclassroom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

/**
 * 获得位置信息（九龙湖/丁家桥/四牌楼/未知）
 * 
 * @author BorisHe
 * 
 */
public class LocationHelper {
	private Context mContext;

	public LocationHelper(Context context) {
		mContext = context;
	}

	/**
	 * 返回当前位置
	 * 
	 * @return spl/djq/jlh/all(无法精确定位)/unknown(服务没开)
	 */
	public String getCurrentCampusLocation() {
		String currLoc = "unknown";
		LocationManager locationManager = (LocationManager) mContext
				.getSystemService(Context.LOCATION_SERVICE);
		if (!locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			return currLoc;
		}
		// LocationProvider provider =
		// locationManager.getProvider(LocationManager.NETWORK_PROVIDER);
		Location location = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (null == location) {
			location = locationManager
					.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
		}
		if (location != null && location.getAccuracy() < 5000) {
			// 精度够了
			double longitude = location.getLongitude(); // 经度
			double latitude = location.getLatitude(); // 纬度
			int nearestId = getNearestCampusId(longitude, latitude);
			switch (nearestId) {
			case 0:
				return "jlh";
			case 1:
				return "spl";
			case 2:
				return "djq";
			default:
				return "all";
			}
		}

		return currLoc;
	}

	/**
	 * 
	 * @param longitude
	 * @param latitude
	 * @return jlh=0, spl=1, djq=2
	 */
	private int getNearestCampusId(double longitude, double latitude) {
		double[] loc_jlh = { 118.8195, 31.8873 };
		double[] loc_spl = { 118.7943, 32.0560 };
		double[] loc_djq = { 118.7756, 32.0748 };
		float[] dists = new float[3];

		float[] results = new float[5];
		Location.distanceBetween(latitude, longitude, loc_jlh[1], loc_jlh[0],
				results);
		dists[0] = results[0];
		Location.distanceBetween(latitude, longitude, loc_spl[1], loc_spl[0],
				results);
		dists[1] = results[0];
		Location.distanceBetween(latitude, longitude, loc_djq[1], loc_djq[0],
				results);
		dists[2] = results[0];

		int min_id = -1;
		float min_val = Float.MAX_VALUE;
		for (int i = 0; i < 3; i++) {
			if (dists[i] < min_val) {
				min_val = dists[i];
				min_id = i;
			}
		}

		return min_id;
	}

	/**
	 * 暂时无用
	 * 
	 * @return
	 */
	private Criteria getCriteria() {
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
		criteria.setAltitudeRequired(false);

		return criteria;
	}

	/**
	 * 开启系统的位置设置
	 */
	private void enableLocationSettings() {
		Intent settingsIntent = new Intent(
				Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		mContext.startActivity(settingsIntent);
	}

	private class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			if (mContext == null)
				return;
			SharedPreferences prefs = mContext.getSharedPreferences(
					"ec_campus", Context.MODE_PRIVATE);
			Editor editor = prefs.edit();
			int locId = getNearestCampusId(location.getLongitude(),
					location.getLatitude());
			String loc = "all";
			switch (locId) {
			case 0:
				loc = "jlh";
			case 1:
				loc = "spl";
			case 2:
				loc = "djq";
			default:
				loc = "all";
			}
			editor.putString("campus", loc);
			editor.commit();
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

	}
}
