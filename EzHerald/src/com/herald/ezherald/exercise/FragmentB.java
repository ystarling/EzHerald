package com.herald.ezherald.exercise;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.herald.ezherald.R;

public class FragmentB extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup group,
			Bundle saved) {
		return inflater.inflate(R.layout.exercise_frag_b, group, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		RunTimesInfo runTimesInfo = new RunTimesInfo(getActivity());
		if (runTimesInfo.isSet()) {
			TextView txtTimes = (TextView) getActivity().findViewById(
					R.id.txt_Times);
			EditText edtAdjust = (EditText) getActivity().findViewById(
					R.id.edtTxt_adjust);
			txtTimes.setText(runTimesInfo.getTimes() + " + "
					+ edtAdjust.getText());
		} else {
			runTimesInfo.update();
		}
	}
}
