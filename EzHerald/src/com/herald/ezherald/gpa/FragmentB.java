package com.herald.ezherald.gpa;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.herald.ezherald.R;
import com.herald.ezherald.account.Authenticate;
import com.herald.ezherald.account.UserAccount;
import com.herald.ezherald.api.APIAccount;

public class FragmentB extends Fragment {
    private ExpandableListView expandableListView;
    private TextView txtGpa;

    private Button btnUpdate, btnCalc, btnRemoveOptional, btnSelectAll;
    private APIAccount user;
    ProgressDialog progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group,
                             Bundle saved) {
        return inflater.inflate(R.layout.gpa_frag_b, group, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        user = new APIAccount(getActivity());
        progress = new ProgressDialog(getActivity());
        progress.setTitle("正在获取,可能时间较长");
        //progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        final GpaAdapter adapter = new GpaAdapter(getActivity(), progress, user);
        if (adapter.getGroupCount() == 0) {
            Toast.makeText(getActivity(), "请先更新数据", Toast.LENGTH_LONG).show();
        }
        txtGpa = (TextView) getActivity().findViewById(R.id.txt_gpa);
        expandableListView = (ExpandableListView) getActivity().findViewById(R.id.eList);

        expandableListView.setAdapter(adapter);
        btnUpdate = (Button) getActivity().findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                progress.show();
                adapter.update();
            }
        });
        btnCalc = (Button) getActivity().findViewById(R.id.btn_calc);
        btnCalc.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    txtGpa.setText(String.format("所选绩点为:%.2f", adapter.getGpaInfo()
                            .calcAverage()));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getActivity(), "还没有选择课程", Toast.LENGTH_SHORT).show();
                    txtGpa.setText("所选绩点为:0.00");
                    e.printStackTrace();
                }
            }

        });

        btnRemoveOptional = (Button) getActivity().findViewById(
                R.id.btn_remove_optional);
        btnRemoveOptional.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                adapter.removeOptional();
            }
        });

        btnSelectAll = (Button) getActivity().findViewById(R.id.btn_select_all);
        btnSelectAll.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                adapter.selectAll();
            }

        });

    }

}
